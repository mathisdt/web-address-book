package org.zephyrsoft.wab;

import nextapp.echo.app.*;

public class AddressBookApp extends ApplicationInstance {
    private static final long serialVersionUID = 6414042684791930462L;

	public Window init() {
        Window window = new Window();

        ContentPane contentPane = new ContentPane();
        window.setContent(contentPane);

        Label label = new Label("Hello, world!");
        contentPane.add(label);
        
        return window;
    }
}