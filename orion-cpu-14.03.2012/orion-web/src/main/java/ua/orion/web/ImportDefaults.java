package ua.orion.web;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.annotations.SetupRender;
import org.apache.tapestry5.func.Worker;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.ioc.services.SymbolSource;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.plastic.*;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.TransformConstants;
import org.apache.tapestry5.services.javascript.JavaScriptSupport;
import org.apache.tapestry5.services.transform.ComponentClassTransformWorker2;
import org.apache.tapestry5.services.transform.TransformationSupport;


/**
 * Добавляет библиотеку JS и стиль CSS одноименный с компонентом/миксином/страницей
 *
 */
public class ImportDefaults implements ComponentClassTransformWorker2 {

    private final JavaScriptSupport javascriptSupport;
    private final SymbolSource symbolSource;
    private final AssetSource assetSource;

    public ImportDefaults(JavaScriptSupport javascriptSupport, SymbolSource symbolSource, AssetSource assetSource) {
        this.javascriptSupport = javascriptSupport;
        this.symbolSource = symbolSource;
        this.assetSource = assetSource;
    }

    public void transform(PlasticClass componentClass, TransformationSupport support, MutableComponentModel model) {
        String[] pkParts= componentClass.getClassName().split("\\.");
        String name= pkParts[pkParts.length-1];
        processClassAnnotationAtSetupRenderPhase(componentClass, model, name);
    }

    private void processClassAnnotationAtSetupRenderPhase(PlasticClass componentClass, MutableComponentModel model, String cls) {
        PlasticMethod setupRender = componentClass.introduceMethod(TransformConstants.SETUP_RENDER_DESCRIPTION);

        decorateMethod(componentClass, model, setupRender, cls);

        model.addRenderPhase(SetupRender.class);
    }

    private void decorateMethod(PlasticClass componentClass, MutableComponentModel model, PlasticMethod method, String cls) {
        decorateMethodWithOperation(componentClass, model, method, cls + ".js", new Worker<Asset>() {

            public void work(Asset asset) {
                javascriptSupport.importJavaScriptLibrary(asset);
            }
        });
        decorateMethodWithOperation(componentClass, model, method, cls + ".css", new Worker<Asset>() {

            public void work(Asset asset) {
                javascriptSupport.importStylesheet(asset);
            }
        });
    }

    private void decorateMethodWithOperation(PlasticClass componentClass, MutableComponentModel model,
            PlasticMethod method, String paths, Worker<Asset> operation) {

        String expandedPath = symbolSource.expandSymbols(paths);

        PlasticField assetListField = componentClass.introduceField(Asset.class,
                "defaultAsset_" + method.getDescription().methodName);

        initializeAssetsFromPaths(model.getBaseResource(), expandedPath, assetListField);

        addMethodAssetOperationAdvice(method, assetListField.getHandle(), operation);
    }

    private void initializeAssetsFromPaths(final Resource baseResource,
            final String expandedPath, final PlasticField assetsField) {
        assetsField.injectComputed(new ComputedValue<Asset>() {

            public Asset get(InstanceContext context) {
                ComponentResources resources = context.get(ComponentResources.class);
                try {
                    return assetSource.getAsset(baseResource, expandedPath, resources.getLocale());
                } catch (RuntimeException e) {
                    //Default asset not found
                    return null;
                }
            }
        });
    }

    private void addMethodAssetOperationAdvice(PlasticMethod method, final FieldHandle access,
            final Worker<Asset> operation) {
        method.addAdvice(new MethodAdvice() {

            public void advise(MethodInvocation invocation) {
                Asset asset = (Asset) access.get(invocation.getInstance());

                if (asset != null) {
                    operation.work(asset);
                }

                invocation.proceed();
            }
        });
    }
}
