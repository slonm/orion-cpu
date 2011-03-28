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

import org.springframework.security.providers.encoding.Md5PasswordEncoder;

import br.com.arsmachina.authentication.controller.PasswordEncrypter;


/**
 * {@link PasswordEncrypter} implementation using {@link Md5PasswordEncoder}.
 * 
 * @author Thiago H. de Paula Figueiredo
 */
public class SpringSecurityMD5PasswordEncrypter implements PasswordEncrypter {

	private Md5PasswordEncoder encoder;

	private Object salt;

	/**
	 * Constructor that receives a salt value.
	 * 
	 * @param salt an {@link Object}.
	 */
	public SpringSecurityMD5PasswordEncrypter(Object salt) {

		this.salt = salt;
		encoder = new Md5PasswordEncoder();

	}

	/**
	 * No-arg constructor. No salt is used.
	 */
	public SpringSecurityMD5PasswordEncrypter() {
		this(null);
	}

	/**
	 * @see br.com.arsmachina.authentication.controller.PasswordEncrypter#encrypt(java.lang.String)
	 */
	public String encrypt(String password) {
		
		if (password.length() != 32) {
			password = encoder.encodePassword(password, salt); 
		}
		
		return password;
		
	}

}
