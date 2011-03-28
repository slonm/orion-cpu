package orion.tapestry.internal.utils;

import orion.tapestry.utils.DataURLConnection;
import java.io.IOException;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.internal.util.AbstractResource;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * A resource stored with in the URL by data://tml/ protocol.
 */
public class DataTMLResource extends AbstractResource {

    public DataTMLResource(String path) {
        super(path);
    }

    @Override
    public String toString() {
        return String.format("data://tml%s", getPath());
    }

    @Override
    protected Resource newResource(String path) {
        return new DataTMLResource(path);
    }

    @Override
    public URL toURL() {
        try {
            URL url = new URL("data", "tml", -1, getPath());
            return URLexists(url) ? url : null;
        } catch (MalformedURLException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static boolean URLexists(URL url) {
        DataURLConnection conn;
        try {
            conn = (DataURLConnection) url.openConnection();
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
        final DataTMLResource other = (DataTMLResource) obj;
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
