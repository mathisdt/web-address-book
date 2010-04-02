package org.zephyrsoft.wab;

import nextapp.echo.app.*;
import nextapp.echo.webcontainer.*;

public class AddressBookServlet extends WebContainerServlet {
    private static final long serialVersionUID = -3145453145023629944L;

	public ApplicationInstance newApplicationInstance() {
        return new AddressBookApp();
    }
}
