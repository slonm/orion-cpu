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
package br.com.arsmachina.tapestrycrud.encoder;

import org.apache.tapestry5.EventContext;
import org.apache.tapestry5.ValueEncoder;

/**
 * Single interface used to define many encoding-related services. This interface extends
 * {@link ValueEncoder}
 * 
 * @param <T> the entity class related to this encoder.
 * 
 * @author sl
 */
public interface Encoder<T> extends
        ValueEncoder<T> {

    Object toActivationContext(T object);

    T toObject(EventContext context);

    String toLabel(T object);
}
