package orion.tapestry.utils;

import org.apache.tapestry5.ioc.internal.util.CollectionFactory;
import org.apache.tapestry5.ioc.services.ClasspathURLConverter;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.apache.tapestry5.internal.util.URLChangeTracker;

/**
 * Given a (growing) set of URLs, can periodically check to see if any of the underlying resources has changed. This
 * class is capable of using either millisecond-level granularity or second-level granularity. Millisecond-level
 * granularity is used by default. Second-level granularity is provided for compatibility with browsers vis-a-vis
 * resource caching -- that's how granular they get with their "If-Modified-Since", "Last-Modified" and "Expires"
 * headers.
 * Кроме функциональности трекера org.apache.tapestry5.internal.util.URLChangeTracker этот
 * трекер поддерживает протокол database:
 */
public class DataAndFileURLChangeTracker extends URLChangeTracker
{
    private final Map<URL, Long> URLToTimestamp = CollectionFactory.newConcurrentMap();

    /**
     * Creates a new URL change tracker, using either millisecond-level granularity or second-level granularity.
     *
     * @param classpathURLConverter used to convert URLs from one protocol to another
     * @param granularitySeconds whether or not to use second granularity (as opposed to millisecond granularity)
     */
    public DataAndFileURLChangeTracker(ClasspathURLConverter classpathURLConverter)
    {
        super(classpathURLConverter, false);
    }

    /**
     * Добавляет в трекер URL с протоколом data
     * @param url of the resource to add, or null if not known
     * @return the current timestamp for the URL (possibly rounded off for granularity reasons), or 0 if the URL is
     *         null
     */
    @Override
    public long add(URL url)
    {
        if (url == null) return 0;
        if (!url.getProtocol().equals("data")) return super.add(url);
        if (URLToTimestamp.containsKey(url)) return URLToTimestamp.get(url);
        long timestamp = readTimestamp(url);
        URLToTimestamp.put(url, timestamp);
        return timestamp;
    }

    /**
     * Clears all URL and timestamp data stored in the tracker.
     */
    @Override
    public void clear()
    {
        URLToTimestamp.clear();
        super.clear();
    }

    /**
     * Re-acquires the last updated timestamp for each URL and returns true if any timestamp has changed.
     */
    @Override
    public boolean containsChanges()
    {
        boolean result = false;

        for (Map.Entry<URL, Long> entry : URLToTimestamp.entrySet())
        {
            long newTimestamp = readTimestamp(entry.getKey());
            long current = entry.getValue();
            if (current == newTimestamp) continue;
            result = true;
            entry.setValue(newTimestamp);
        }

        return result||super.containsChanges();
    }

    /**
     * Returns the time that the specified file was last modified, possibly rounded down to the nearest second.
     */
    private long readTimestamp(URL url)
    {
        DataURLConnection conn;
        try {
            conn = (DataURLConnection) url.openConnection();
            if (!conn.exists()) return -1;
        } catch (IOException ex) {
            return -1;
        }
        return conn.getLastModified();
    }
}
