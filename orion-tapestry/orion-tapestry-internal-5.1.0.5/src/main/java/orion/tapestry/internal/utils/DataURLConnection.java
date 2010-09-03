package orion.tapestry.internal.utils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * URLConnection implementation for <code>data</code> protocol
 * @author sl
 */
public abstract class DataURLConnection extends URLConnection {

    protected Object entity = null;

    public DataURLConnection(URL url) {
        super(url);
    }

    @Override
    public synchronized void connect() throws IOException {
        if (connected) {
            return;
        }
        entity = getEntity(url.getFile());
        connected = true;
    }

    abstract protected Object getEntity(String name);

    @Override
    public synchronized InputStream getInputStream() throws IOException {
        connect();
        return new ByteArrayInputStream(getInputBuffer());
    }

    abstract protected byte[] getInputBuffer() throws IOException;

    public boolean exists() {
        try {
            connect();
        } catch (IOException ex) {
        }
        return entity != null;
    }

    public final String getFile(String path) {
        int slashx = path.lastIndexOf('/');

        return path.substring(slashx + 1);
    }
}
