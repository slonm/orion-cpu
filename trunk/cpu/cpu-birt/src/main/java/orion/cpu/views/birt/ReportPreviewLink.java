package orion.cpu.views.birt;

import java.util.*;
import org.apache.tapestry5.Link;
import org.apache.tapestry5.ioc.internal.util.InternalUtils;
import org.apache.tapestry5.services.Response;
import ua.mihailslobodyanuk.utils.Defense;

/**
 *
 * @author sl
 */
public class ReportPreviewLink implements Link {

    private Map<String, String> parameters;
    private final String absoluteURI;
    private final Response response;
    private String anchor;

    public ReportPreviewLink(String absoluteURI, Map<String, String> parameters, Response response) {
        this.absoluteURI = absoluteURI;
        this.response = response;
        if (parameters != null && !parameters.isEmpty()) {
            this.parameters = new HashMap();
            this.parameters.putAll(parameters);
        }
    }

    @Override
    public void addParameter(String parameterName, String value) {
        Defense.notBlank(parameterName, "parameterName");
        Defense.notBlank(value, "value");
        if (parameters == null) {
            parameters = new HashMap();
        }
        parameters.put(parameterName, value);
    }

    @Override
    public String getAnchor() {
        return anchor;
    }

    @Override
    public List<String> getParameterNames() {
        return parameters==null?Collections.EMPTY_LIST:new ArrayList(parameters.keySet());
    }

    @Override
    public String getParameterValue(String name) {
        return parameters==null?null:parameters.get(name);
    }

    @Override
    public void setAnchor(String anchor) {
        this.anchor = anchor;
    }

    @Override
    public String toAbsoluteURI() {
        return appendAnchor(response.encodeURL(buildURI()));
    }

    @Override
    public String toRedirectURI() {
        return appendAnchor(response.encodeRedirectURL(buildURI()));
    }

    @Override
    public String toURI() {
        String path = buildURI();
        return appendAnchor(response.encodeURL(path));
    }

    private String appendAnchor(String path) {
        return InternalUtils.isBlank(anchor)
                ? path
                : path + "#" + anchor;
    }

    /**
     * Returns the value from {@link #toURI()}
     */
    @Override
    public String toString() {
        return toURI();
    }

    /**
     * Extends the absolute path with any query parameters. Query parameters are never added to a forForm link.
     *
     * @return absoluteURI appended with query parameters
     */
    private String buildURI() {
        StringBuilder builder = new StringBuilder(absoluteURI.length() * 2);

        builder.append(absoluteURI);

        String sep = "?";

        for (String name : getParameterNames()) {
            String value = parameters.get(name);

            builder.append(sep);

            // We assume that the name is URL safe and that the value will already have been URL
            // encoded if it is not known to be URL safe.

            builder.append(name);
            builder.append("=");
            builder.append(value);

            sep = "&";
        }

        return builder.toString();
    }

    public void removeParameter(String parameterName) {
        Defense.notBlank(parameterName, "parameterName");
        if (parameters != null) {
            parameters.remove(parameterName);
        }
    }

    public String getBasePath() {
        return absoluteURI;
    }

    public Link copyWithBasePath(String basePath) {
        ReportPreviewLink copy = new ReportPreviewLink(basePath, parameters, response);
        copy.anchor = anchor;
        return copy;
    }

    public Link addParameterValue(String parameterName, Object value) {
        addParameter(parameterName, value.toString());
        return this;
    }

    public String toAbsoluteURI(boolean secure) {
        return absoluteURI;
    }
}
