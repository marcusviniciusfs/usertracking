import org.apache.commons.lang3.Range;

import java.io.IOException;
import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Commons {

    private static final Logger LOGGER = LoggerFactory.getLogger(Commons.class);

    public static Range<Integer> getPortRange() {
        final String property = System.getProperty("pacsng.it.portRange");
        if (property == null || property.isEmpty() || property.equals("${pacsng.it.portRange}")) {
            return Range.between(8080, 8200);
        }
        try {
            final String[] values = property.split("-");
            return Range.between(Integer.parseInt(values[0]), Integer.parseInt(values[1]));
        } catch (Exception e) {
            LOGGER.warn("Invalid value for system property 'pacsng.it.portRange'. Using default value: 8080-8200");
            return Range.between(8080, 8200);
        }
    }

    public static int findFirstAvailablePort(final int start, final int finish) throws IOException {
        for (int port = start; port < finish; port++) {
            try {
                final ServerSocket serverSocket = new ServerSocket(port);
                serverSocket.close();
                return port;
            } catch (IOException e) {
                // Do nothing
            }
        }
        throw new IOException("No port available between " + start + " and " + finish);
    }
}
