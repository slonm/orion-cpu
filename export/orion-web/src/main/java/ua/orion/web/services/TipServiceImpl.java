/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package ua.orion.web.services;

import javax.inject.Inject;
import org.apache.tapestry5.alerts.AlertManager;
import org.apache.tapestry5.alerts.Duration;
import org.apache.tapestry5.alerts.Severity;
import org.apache.tapestry5.ioc.Messages;
import org.slf4j.Logger;

/**
 *
 * @author slobodyanuk
 */
public class TipServiceImpl implements TipService {

    private AlertManager alertManager;
    private Messages messages;
    private Logger logger;

    public TipServiceImpl(AlertManager alertManager, Messages messages, Logger logger) {
        this.alertManager = alertManager;
        this.messages = messages;
        this.logger = logger;
    }

    @Override
    public void doWork(Runnable work, String message, Object... params) {
        try {
            work.run();
            alertManager.alert(Duration.TRANSIENT, Severity.INFO,
                    messages.format("message.success." + message, params));
        } catch (RuntimeException ex) {
            logger.debug(ex.getMessage());
            alertManager.alert(Duration.TRANSIENT, Severity.ERROR,
                    messages.format("message.error." + message, params));
        }
    }
}
