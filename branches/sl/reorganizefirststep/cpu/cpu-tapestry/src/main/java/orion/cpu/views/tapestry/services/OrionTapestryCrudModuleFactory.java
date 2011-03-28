package orion.cpu.views.tapestry.services;

import orion.cpu.views.tapestry.services.TapestryCrudModuleImpl;
import br.com.arsmachina.module.Module;
import br.com.arsmachina.tapestrycrud.module.TapestryCrudModule;
import br.com.arsmachina.tapestrycrud.services.TapestryCrudModuleFactory;
import org.apache.tapestry5.ioc.services.SymbolSource;

/**
 * Фабрика создает объекты TapestryCrudModuleImpl
 * @author sl
 */
public class OrionTapestryCrudModuleFactory implements TapestryCrudModuleFactory {

    private final SymbolSource symbolSource;

    public OrionTapestryCrudModuleFactory(SymbolSource symbolSource) {
        this.symbolSource = symbolSource;
    }

    @Override
    public TapestryCrudModule build(Module module) {
        return new TapestryCrudModuleImpl(module, symbolSource);
    }
}
