package br.com.arsmachina.module.service;

import br.com.arsmachina.controller.Controller;
import java.io.Serializable;

/**
 *
 * @param <T>
 * @param <K>
 * @author sl
 */
public class ControllerSourceContributionsValue<T, K extends Serializable> {

    private final Class<? extends Controller<T, K>> controllerImplementation;
    private final Class<? extends Controller<T, K>> controllerDefinition;

    public ControllerSourceContributionsValue(Class<? extends Controller<T, K>> controllerImplementation, Class<? extends Controller<T, K>> controllerDefinition) {
        this.controllerImplementation = controllerImplementation;
        this.controllerDefinition = controllerDefinition;
    }

    public Class<? extends Controller<T, K>> getControllerDefinition() {
        return controllerDefinition;
    }

    public Class<? extends Controller<T, K>> getControllerImplementation() {
        return controllerImplementation;
    }
}
