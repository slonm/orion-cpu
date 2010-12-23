/*
 *  Copyright 2010 sl.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */
package br.com.arsmachina.module.service.internal;

import br.com.arsmachina.module.service.DataAwareObjectSource;
import br.com.arsmachina.module.service.DataAwareObjectSourceAdapter;
import java.util.Map;
import org.apache.tapestry5.ioc.internal.util.InheritanceSearch;
import org.apache.tapestry5.ioc.util.StrategyRegistry;

/**
 *
 * @author sl
 */
@SuppressWarnings("all")
public class DataAwareObjectSourceImpl implements DataAwareObjectSource {

    private final StrategyRegistry<DataAwareObjectSourceAdapter> registry;

    public DataAwareObjectSourceImpl(Map<Class, DataAwareObjectSourceAdapter> configuration) {
        registry = StrategyRegistry.newInstance(DataAwareObjectSourceAdapter.class, configuration, true);
    }

    @SuppressWarnings({"unchecked"})
    public <T> T get(Class<T> objectType, Class<?> type) {
        //FIXME Replace to Defense.notNull
        assert objectType != null;
        assert type != null;
        DataAwareObjectSourceAdapter<T> adapter;
        T result = null;
        //FIXME Dont use internal InheritanceSearch
        for (Class<?> t : new InheritanceSearch(objectType)) {
            adapter = registry.get(t);
            if (adapter != null && objectType.isAssignableFrom(adapter.getClass())) {
                result = adapter.get(type);
                break;
            }
        }
        return result;
    }

    public void clearCache() {
        registry.clearCache();
    }
}
