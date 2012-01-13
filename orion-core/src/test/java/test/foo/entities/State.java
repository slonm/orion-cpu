/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package test.foo.entities;

import javax.persistence.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.orion.core.persistence.AbstractEnumerationEntity;
import ua.orion.core.validation.Unique;

/**
 *
 * @author user
 */
@Entity
@Unique
public class State extends AbstractEnumerationEntity<State>{
    private static final Logger LOG=LoggerFactory.getLogger(State.class);
}
