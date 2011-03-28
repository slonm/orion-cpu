package orion.cpu.views.desktoptest;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.Manifest;
import org.apache.tapestry5.ioc.IOCConstants;
import org.apache.tapestry5.ioc.RegistryBuilder;

/**
 *
 * @author sl
 */
public class TestUtilities {

    private TestUtilities() {
    }
    /**
     * Scans the classpath for JAR Manifests that contain the Tapestry-Module-Classes attribute and adds each
     * corresponding class to the RegistryBuilder. In addition, looks for a system property named "tapestry.modules" and
     * adds all of those modules as well. The tapestry.modules approach is intended for development.
     *
     * @param builder the builder to which modules will be added
     * @see SubModule
     * @see RegistryBuilder#add(String)
     */
    public static List<String> listDefaultModules()
    {
        List<String> ret = new ArrayList<String>();
        try
        {
            Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources("META-INF/MANIFEST.MF");

            while (urls.hasMoreElements())
            {
                URL url = urls.nextElement();

                ret.addAll(listModulesInManifest(url));
            }

            ret.addAll(splitToList(System.getProperty("tapestry.modules")));

        }
        catch (IOException ex)
        {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        return ret;
    }

    private static List<String> listModulesInManifest(URL url)
    {
        InputStream in = null;

        Throwable fail = null;
        List<String> ret = Collections.EMPTY_LIST;
        try
        {
            in = url.openStream();

            Manifest mf = new Manifest(in);

            in.close();

            in = null;

            String list = mf.getMainAttributes().getValue(IOCConstants.MODULE_BUILDER_MANIFEST_ENTRY_NAME);

            ret=splitToList(list);
        }
        catch (RuntimeException ex)
        {
            fail = ex;
        }
        catch (IOException ex)
        {
            fail = ex;
        }
        finally
        {
            close(in);
        }

        if (fail != null)
            throw new RuntimeException(String.format("Exception loading module(s) from manifest %s: %s",
                                                     url.toString(),
                                                     fail), fail);
        return ret;
    }

    static List<String> splitToList(String list)
    {
        if (list == null) return Collections.EMPTY_LIST;
        String[] classnames = list.split(",");
        return Arrays.asList(classnames);
    }

    /**
     * Closes an input stream (or other Closeable), ignoring any exception.
     *
     * @param closeable the thing to close, or null to close nothing
     */
    private static void close(Closeable closeable)
    {
        if (closeable != null)
        {
            try
            {
                closeable.close();
            }
            catch (IOException ex)
            {
                // Ignore.
            }
        }
    }
}
