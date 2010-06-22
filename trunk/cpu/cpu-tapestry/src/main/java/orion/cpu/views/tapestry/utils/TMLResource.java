package orion.cpu.views.tapestry.utils;

import java.io.IOException;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.AbstractResource;

import java.net.MalformedURLException;
import java.net.URL;
import orion.cpu.views.tapestry.utils.TMLURLStreamHandler.TMLURLConnection;

/**
 * A resource stored with in the URL by tml protocol.
 */
public class TMLResource extends AbstractResource {

    public TMLResource(String path) {
        super(path);
    }

    @Override
    public String toString() {
        return String.format("tml:///%s", getFile());
    }

    @Override
    protected Resource newResource(String path) {
        return new TMLResource(path);
    }

    @Override
    public URL toURL() {
        try {
            URL url = new URL("tml", "", -1, getFile());
            return URLexists(url) ? url : null;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean URLexists(URL url) {
        TMLURLConnection conn;
        try {
            conn = (TMLURLConnection) url.openConnection();
            return conn.exists();
        } catch (IOException ex) {
            return false;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TMLResource other = (TMLResource) obj;
        if (!this.getPath().equals(other.getPath())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 97 * hash + this.getPath().hashCode();
        return hash;
    }
}
