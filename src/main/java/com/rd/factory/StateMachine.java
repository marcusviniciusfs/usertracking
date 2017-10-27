package com.rd.factory;

import com.rd.UserTracking;
import com.rd.UserTrackingException;
import org.apache.commons.lang3.concurrent.ConcurrentUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;


class StateMachine {

    enum Command {
        RESTART,
        START,
        STOP
    }

    private static final Logger LOGGER = LoggerFactory.getLogger(StateMachine.class);

    private final ExecutorService executorService;

    private final UserTrackingImpl userTracking;

    private final LinkedBlockingQueue<Command> queue;

    private UserTracking.State state;

    private final StateChangeRunnable stateChangeRunnable;

    StateMachine(final UserTrackingImpl userTrackingImpl) {
        userTracking = userTrackingImpl;

        queue = new LinkedBlockingQueue<>();
        executorService = Executors.newSingleThreadExecutor(new ExecutorThreadFactory());
        stateChangeRunnable = new StateChangeRunnable();

        Runtime.getRuntime().addShutdownHook(new Thread(new ShutdownRunnable()));
    }

    final Future<Boolean> change(final Command command) {
        synchronized (this) {
            if (command != Command.START && state == null) {
                LOGGER.warn("Not {}", UserTracking.State.STARTED.name());
                return ConcurrentUtils.constantFuture(false);
            }
            if (command == Command.START && checkAlreadyStarted()) {
                LOGGER.warn("Already {}", UserTracking.State.STARTED.name());
                return ConcurrentUtils.constantFuture(false);
            }
            if (queue.contains(Command.STOP) || queue.contains(command)) {
                LOGGER.warn("Already {}", state);
                return ConcurrentUtils.constantFuture(false);
            }
            queue.add(command);
        }
        LOGGER.debug("Preparing to {}", command);
        return executorService.submit(stateChangeRunnable);
    }

    private boolean checkAlreadyStarted() {
        return state != null || queue.contains(Command.START);
    }

    final UserTracking.State state() {
        return state;
    }

    private void stop() throws UserTrackingException {
        state = UserTracking.State.STOPPING;
        executorService.shutdown();
        userTracking.doStop();
        state = UserTracking.State.STOPPED;
    }

    private class StateChangeRunnable implements Callable<Boolean> {

        @Override
        public Boolean call() throws UserTrackingException {
            final Command command = queue.peek();
            try {
                switch (command) {
                    case RESTART:
                        state = UserTracking.State.RESTARTING;
                        userTracking.doRestart();
                        state = UserTracking.State.STARTED;
                        break;

                    case START:
                        state = UserTracking.State.STARTING;
                        userTracking.doStart();
                        state = UserTracking.State.STARTED;
                        break;

                    case STOP:
                        stop();
                        break;

                    default:
                        throw new UserTrackingException("Invalid command");
                }
            } catch (UserTrackingException e) {
                executorService.shutdown();
                throw e;
            }
            queue.poll();
            return true;
        }
    }

    private static class ExecutorThreadFactory implements ThreadFactory {

        @Override
        public Thread newThread(final Runnable runnable) {
            return new Thread(runnable, UserTracking.class.getSimpleName() + " State Machine");
        }
    }

    private class ShutdownRunnable implements Runnable {
        @Override
        public void run() {
            try {
                change(Command.STOP).get();
            } catch (RejectedExecutionException e) {
                LOGGER.trace("", e);
            } catch (InterruptedException | ExecutionException e) {
                LOGGER.error("", e);
                Thread.currentThread().interrupt();
            }
        }
    }
}
