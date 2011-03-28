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

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ValueEncoder;
import br.com.arsmachina.tapestrycrud.encoder.Encoder;

/**
 * An {@link ActivationContextEncoder} implementation based on a {@link ValueEncoder}.
 * 
 * @author 
 * @param <T> the entity class.
 */
public class ValueEncoderBasedEncoder<T> implements Encoder<T> {

    final private ValueEncoder<T> valueEncoder;

    public ValueEncoderBasedEncoder(ValueEncoder<T> valueEncoder) {
        if (valueEncoder == null) {
            throw new IllegalArgumentException("Parameter primaryKeyEncoder cannot be null");
        }
        this.valueEncoder = valueEncoder;
    }

    @Override
    public T toValue(String clientValue) {
        return valueEncoder.toValue(clientValue);
    }

    @Override
    public String toClient(T value) {
        return valueEncoder.toClient(value);
    }

    @Override
    public Object toActivationContext(T object) {
        return toClient(object);
    }

    @Override
    public T toObject(EventContext context) {
        if (context.getCount() > 0) {
            return toValue(context.get(String.class, 0));
        }
        return null;
    }

    @Override
    public String toLabel(T object) {
        return String.valueOf(object);
    }
}
