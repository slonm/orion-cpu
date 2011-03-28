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
package br.com.arsmachina.tapestrycrud.services.impl;

import br.com.arsmachina.tapestrycrud.beanmodel.BeanModelCustomizer;
import java.util.*;
import org.apache.tapestry5.beaneditor.BeanModel;

/**
 *
 * @author sl
 */
public class BeanModelCustomizerChain implements BeanModelCustomizer {

    private final List<BeanModelCustomizer> customizers;

    public BeanModelCustomizerChain(List<BeanModelCustomizer> customizers) {
        this.customizers = Collections.unmodifiableList(customizers);
    }

    public BeanModel customizeModel(BeanModel model) {
        for (BeanModelCustomizer customizer : customizers) {
            model = customizer.customizeModel(model);
        }
        return model;
    }

    public BeanModel customizeEditModel(BeanModel model) {
        for (BeanModelCustomizer customizer : customizers) {
            model = customizer.customizeEditModel(model);
        }
        return model;
    }

    public BeanModel customizeDisplayModel(BeanModel model) {
        for (BeanModelCustomizer customizer : customizers) {
            model = customizer.customizeDisplayModel(model);
        }
        return model;
    }
}
