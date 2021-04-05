package org.zephyrsoft.wab;

import javax.servlet.annotation.WebServlet;

import nextapp.echo.app.ApplicationInstance;
import nextapp.echo.webcontainer.WebContainerServlet;

/**
 * servlet which creates new instances of the address book application (one for each user)
 */
@WebServlet("/app")
public class AddressBookServlet extends WebContainerServlet {
	private static final long serialVersionUID = -3145453145023629944L;

	@Override
	public ApplicationInstance newApplicationInstance() {
		return new AddressBookApp();
	}
}
