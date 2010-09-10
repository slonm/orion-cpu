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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.apache.tapestry5.services.ApplicationStateManager;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.RequestFilter;
import org.apache.tapestry5.services.RequestHandler;
import org.apache.tapestry5.services.Response;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.User;
import ua.mihailslobodyanuk.utils.Defense;

/**
 * {@link RequestFilter} that sets the {@link User} application state object if Spring Security has
 * an authenticated user in a session.
 * Если аутентификация не выполнена, то устанавливается пользователь GUEST и у него не изменяется флаг isLoggedIn
 * @author Thiago H. de Paula Figueiredo
 */
public class UserASORequestFilter implements RequestFilter {

    final private static String USER_LOGOUT_LISTENER_ATTRIBUTE = "USER_LOGOUT_LISTENER";
    private final UserController userController;
    private final ApplicationStateManager applicationStateManager;

    /**
     * Single constructor of this class.
     *
     * @param userController an {@link UserController}. It cannot be null.
     * @param applicationStateManager an {@link ApplicationStateManager}. It cannot be null.
     */
    public UserASORequestFilter(UserController userController,
            ApplicationStateManager applicationStateManager) {
        this.userController = Defense.notNull(userController, "userController");
        this.applicationStateManager = Defense.notNull(applicationStateManager, "applicationStateManager");
    }

    @Override
    public boolean service(Request request, Response response, RequestHandler handler)
            throws IOException {

        User user = applicationStateManager.getIfExists(User.class);

        if (user == null || User.GUEST_USER.getLogin().equalsIgnoreCase(user.getLogin())) {

            final SecurityContext context = SecurityContextHolder.getContext();
            final Authentication authentication = context.getAuthentication();

            if (authentication != null && authentication instanceof AnonymousAuthenticationToken == false) {

                final String login = authentication.getName();
                if (login.equalsIgnoreCase(User.GUEST_USER.getLogin())) {
                    //TODO Добавить запрет входа гостя в SecurityContext
                } else {
                    user = userController.loadEverything(login);

                    if (user == null) {
                        throw new RuntimeException("Unknown logged user: " + login);
                    }

                    applicationStateManager.set(User.class, user);
                    UserLoggedOutListener listener = new UserLoggedOutListener(user, userController);
                    request.getSession(false).setAttribute(USER_LOGOUT_LISTENER_ATTRIBUTE, listener);

                    if (user.isLoggedIn() == false) {

                        user.setLoggedIn(true);
                        userController.update(user);

                    }

                }
            }
            if (user == null) {
                user = userController.loadEverything(User.GUEST_USER.getLogin());

                if (user == null) {
                    throw new RuntimeException(String.format("User '%s' not exists!", User.GUEST_USER.getLogin()));
                }

                applicationStateManager.set(User.class, user);
            }

        }

        return handler.service(request, response);

    }
}
