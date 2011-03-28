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

package br.com.arsmachina.authentication.springsecurity;

import java.util.Set;

import org.springframework.dao.DataAccessException;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

import br.com.arsmachina.authentication.controller.UserController;
import br.com.arsmachina.authentication.entity.Permission;
import br.com.arsmachina.authentication.entity.User;

/**
 * {@link UserDetailsService} implementation using Generic Authentication and Tapestry 5.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class UserDetailsServiceImpl implements UserDetailsService {

	/**
	 * Obligatory suffix used in authority names by Acegi/Spring Security.
	 */
	public static final String SPRING_SECURITY_AUTHORITY_NAME_SUFFIX = "ROLE_";

	private UserController userController;

	private boolean allowSimultaneousLogins;

	/**
	 * For a given permission name, returns the authority (permission) created to Spring Security by
	 * {@link #loadUserByUsername(String)}. It is
	 * <code>SPRING_SECURITY_AUTHORITY_NAME_SUFFIX + permission.toUpperCase()</code>.
	 * 
	 * @param permissionName a {@link String}. It cannot be null.
	 * @return a {@link String}.
	 */
	public static String authorityName(String permissionName) {

		assert permissionName != null;
		
		if (permissionName.startsWith(SPRING_SECURITY_AUTHORITY_NAME_SUFFIX) == false) {
			permissionName = SPRING_SECURITY_AUTHORITY_NAME_SUFFIX + permissionName.toUpperCase();
		}
		
		return permissionName;

	}

	/**
	 * Single constructor of this class.
	 * 
	 * @param userController an {@link UserController}. It cannot be null.
	 */
	public UserDetailsServiceImpl(UserController userController, boolean allowSimultaneousLogins) {

		if (userController == null) {
			throw new IllegalArgumentException("Parameter userController cannot be null");
		}

		this.userController = userController;
		this.allowSimultaneousLogins = allowSimultaneousLogins;

	}

	/**
	 * @see UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException,
			DataAccessException {

		User user = userController.findByLogin(username);

		if (user == null) {
			throw new UsernameNotFoundException("User " + username + " not found.");
		}

		return createUserDetails(user);

	}

	/**
	 * Used by {@link #loadUserByUsername(String)} to creates and returns an {@link UserDetails}
	 * instance for a giver {@link User}.
	 * 
	 * @param user an {@link User}. It cannot be null.
	 * @return an {@link UserDetails}.
	 */
	protected UserDetails createUserDetails(User user) {

		org.springframework.security.userdetails.User userDetails;

		final String login = user.getLogin();
		final boolean enabled = user.isEnabled();
		final String password = user.getPassword();
		final boolean expired = user.isExpired();
		final boolean credentialsExpired = user.isCredentialsExpired();
		final boolean locked = user.isLocked()
				|| (allowSimultaneousLogins == false && user.isLoggedIn());

		final Set<Permission> permissions = user.getPermissions();

		GrantedAuthority[] authorities;

		authorities = new GrantedAuthorityImpl[permissions.size()];

		int i=0;
                for(Permission permission:permissions){
                    authorities[i++]=new GrantedAuthorityImpl(permission.getName());
                }
/*
                for (int i = 0; i < authorities.length; i++) {

			final String authorityName = authorityName(permissions.get(i).getName());
			authorities[i] = new GrantedAuthorityImpl(authorityName);

		}
*/
		userDetails = new org.springframework.security.userdetails.User(
				login, password, enabled, !expired, !credentialsExpired, !locked, authorities);

		return userDetails;

	}
	
}
