package org.tynamo.security.testapp.pages.contributed;

import org.apache.tapestry5.ioc.annotations.Inject;
import org.tynamo.security.testapp.pages.AccessiblePage;

import javax.servlet.http.HttpServletRequest;

public class Index extends AccessiblePage
{

	@Inject
	private HttpServletRequest request;

	public String getRemoteUser()
	{
		return request.getRemoteUser();
	}

	public String getUserPrincipal()
	{
		return request.getUserPrincipal().getName();
	}

	public boolean isUserInRole(String role)
	{
		return request.isUserInRole(role);
	}

	public boolean isUserInRoleUser()
	{
		return isUserInRole("user");
	}


	public boolean isUserInRoleManager()
	{
		return isUserInRole("manager");
	}


}
