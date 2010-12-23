package orion.cpu.controllers.event;

import orion.cpu.controllers.event.base.AbstractAfterViewEv;

/**
 * Событие после вычисления количества
 * @author sl
 */
public class AfterCountAllEv extends AbstractAfterViewEv<Integer> {

    public AfterCountAllEv(Integer returnValue) {
        super(returnValue);
    }
}
