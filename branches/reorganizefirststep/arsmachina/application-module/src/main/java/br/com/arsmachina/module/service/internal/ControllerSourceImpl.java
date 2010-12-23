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

package br.com.arsmachina.module.service.internal;

import br.com.arsmachina.module.service.ControllerSourceContributionsValue;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.apache.tapestry5.ioc.util.StrategyRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.controller.Controller;
import br.com.arsmachina.module.factory.ControllerFactory;
import br.com.arsmachina.module.service.ControllerSource;
import org.apache.tapestry5.ioc.ObjectLocator;

/**
 * {@link ControllerSource} implementation.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class ControllerSourceImpl implements ControllerSource {

	final private static Logger LOGGER = LoggerFactory.getLogger(ControllerSourceImpl.class);
	
	@SuppressWarnings("unchecked")
	final private StrategyRegistry<ControllerSourceContributionsValue> registry;
	
	final private ControllerFactory controllerFactory;
	
	@SuppressWarnings("unchecked")
	final private Map<Class, Controller> additionalControllers = new HashMap<Class, Controller>();

        final private ObjectLocator locator;
	/**
	 * Single constructor.
	 * 
	 * @param registrations
	 */
	@SuppressWarnings("unchecked")
	public ControllerSourceImpl(Map<Class, ControllerSourceContributionsValue> registrations, ControllerFactory controllerFactory,
                ObjectLocator locator) {
		
		if (registrations == null) {
			throw new IllegalArgumentException("Parameter registrations cannot be null");
		}
		
		if (controllerFactory == null) {
			throw new IllegalArgumentException("Parameter controllerFactory cannot be null");
		}
		
		if (locator == null) {
			throw new IllegalArgumentException("Parameter locator cannot be null");
		}

		registry = StrategyRegistry.newInstance(ControllerSourceContributionsValue.class, registrations, true);
		this.controllerFactory = controllerFactory;
		this.locator = locator;
	}

	@SuppressWarnings("unchecked")
	public <T, K extends Serializable> Controller<T, K> get(Class<T> clasz) {
		
		Controller controller = null;
		ControllerSourceContributionsValue controllerContributionsValue = registry.get(clasz);
                if(controllerContributionsValue != null){
                    try {
                            controller = (Controller) locator.getService(controllerContributionsValue.getControllerDefinition());
                    }
                    catch (RuntimeException e) {
                            controller = (Controller) locator.autobuild(controllerContributionsValue.getControllerImplementation());
                    }
                }
		
		if (controller == null) {
			
			controller = additionalControllers.get(clasz);
			
			if (controller == null) {
				
				controller = controllerFactory.build(clasz);

				if (controller != null) {
					
					additionalControllers.put(clasz, controller);
					
					if (LOGGER.isInfoEnabled()) {
						LOGGER.info("Created default controller for " + clasz.getName());
					}
					
				}
				
			}
			
		}
		
		return controller;
		
	}
}
