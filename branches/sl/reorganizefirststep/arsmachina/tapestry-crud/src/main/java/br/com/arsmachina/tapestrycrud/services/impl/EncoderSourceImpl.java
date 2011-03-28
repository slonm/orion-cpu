// Copyright 2008 Thiago H. de Paula Figueiredo
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package br.com.arsmachina.tapestrycrud.services.impl;

import java.util.Map;

import br.com.arsmachina.tapestrycrud.encoder.Encoder;
import br.com.arsmachina.tapestrycrud.services.EncoderFactory;
import br.com.arsmachina.tapestrycrud.services.EncoderSource;
import java.util.HashMap;
import org.apache.tapestry5.services.InvalidationListener;

/**
 * {@link EncoderSource} implementation.
 * 
 * @author sl
 */
@SuppressWarnings("unchecked")
public class EncoderSourceImpl implements EncoderSource, InvalidationListener {

    private final Map<Class, Encoder> cache = new HashMap<Class, Encoder>();
    final private EncoderFactory factory;

    /**
     * Single constructor.
     *
     * @param registrations
     */
    public EncoderSourceImpl(EncoderFactory encoderFactory) {
        this.factory = encoderFactory;
    }

    /**
     * @see br.com.arsmachina.tapestrycrud.services.EncoderSource#get(java.lang.Class)
     */
    public <T> Encoder<T> get(Class<T> type) {
        assert type != null;
        Encoder<T> result = cache.get(type);
        if (result == null) {
            result = factory.create(type);
            cache.put(type, result);
        }
        return result;
    }

    public void objectWasInvalidated() {
        cache.clear();
    }
}
