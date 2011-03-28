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
package br.com.arsmachina.authentication.springsecurity.internal;

import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.User;
import orion.cpu.security.services.ExtendedAuthorizer;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * Class that is notified when a session is invalidated (either by user logout or session timenout)
 * and then marks that {@link User} is not logged anymore.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class UserLoggedOutListener implements HttpSessionBindingListener {

    final private static Logger LOGGER = LoggerFactory.getLogger(UserLoggedOutListener.class);
    private User user;
    private UserController controller;
    private final ExtendedAuthorizer authorizer;

    /**
     * Single constructor of this class.
     *
     * @param user an {@link User}. It cannot be null.
     * @param controller an {@link UserController}. It cannot be null.
     */
    public UserLoggedOutListener(User user, UserController controller, ExtendedAuthorizer authorizer) {

        this.user = Defense.notNull(user, "user");
        this.controller = Defense.notNull(controller, "controller");
        this.authorizer = Defense.notNull(authorizer, "authorizer");

    }

    /**
     * Does nothing.
     *
     * @see javax.servlet.http.HttpSessionBindingListener#valueBound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueBound(HttpSessionBindingEvent event) {
    }

    /**
     * Sets the user's <code>loggedIn</code> property to <code>false</code> and invokes
     * {@link UserController}<code>.update()</code> on it.
     *
     * @see javax.servlet.http.HttpSessionBindingListener#valueUnbound(javax.servlet.http.HttpSessionBindingEvent)
     */
    public void valueUnbound(HttpSessionBindingEvent event) {

        if (user.isLoggedIn()) {

            authorizer.pushUserAndRole();
            authorizer.storeUserAndRole(User.SYSTEM_USER, null);
            user.setLoggedIn(false);
            controller.update(user);
            authorizer.popUserAndRole();

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("User " + user.getLogin() + " was logged out");
            }

        }

    }
}
