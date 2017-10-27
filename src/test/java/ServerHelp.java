import com.rd.UserTracking;
import com.rd.UserTrackingException;
import com.rd.factory.UserTrackingFactory;
import com.rd.persistence.database.DatabaseService;
import com.rd.persistence.database.Transaction;
import com.rd.persistence.database.exception.DatabaseException;
import com.rd.persistence.database.exception.TransactionException;
import com.rd.persistence.model.configuration.ServerConfiguration;
import com.rd.util.OrderedProperties;
import io.undertow.Undertow;

import javax.persistence.EntityManager;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class ServerHelp {

    private UserTracking userTracking;
    private static Undertow undertowServer;

    public static DatabaseService startDatabaseService() throws URISyntaxException, DatabaseException {
        final URL resource = UserTracking.class.getResource("/hibernate.properties");

        final OrderedProperties properties = new OrderedProperties();
        try (final FileInputStream inputStream = new FileInputStream(new File(resource.toURI()))) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new DatabaseException(e);
        }
        properties.setProperty("hibernate.connection.url", "jdbc:hsqldb:mem:" + UUID.randomUUID().toString());

        return new DatabaseService(UserTracking.class.getSimpleName(), properties);
    }

    public UserTracking startServer(final DatabaseService databaseService) throws UserTrackingException, TransactionException, IOException, ExecutionException, InterruptedException {

        final int httpPort = Commons.findFirstAvailablePort(Commons.getPortRange().getMinimum(), Commons.getPortRange().getMaximum());
        final int httpServicePort = Commons.findFirstAvailablePort(Commons.getPortRange().getMinimum(), Commons.getPortRange().getMaximum());
        startUndertowServer(httpPort);

        final ServerConfiguration server = databaseService.submit(new Transaction<ServerConfiguration>() {
            @Override
            public final ServerConfiguration execute(final EntityManager entityManager) throws TransactionException {
                    final ServerConfiguration serverConfiguration = new ServerConfiguration("127.0.0.1", httpServicePort + 1);
                    entityManager.persist(serverConfiguration);
                    return serverConfiguration;
            }
        });

        userTracking = UserTrackingFactory.create(databaseService);
        userTracking.start().get();

        return userTracking;
    }

    public UserTracking getUserTracking() {
        return userTracking;
    }

    private static void startUndertowServer(final int firstAvailablePort) throws IOException {

        undertowServer = Undertow.builder()
                .addHttpListener(firstAvailablePort, "127.0.0.1")
                .build();
        undertowServer.start();
    }

    public void stopUndertowServer(){
        undertowServer.stop();
    }
}
