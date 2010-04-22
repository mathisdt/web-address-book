package org.zephyrsoft.wab;

import nextapp.echo.app.*;

public class AddressBookApp extends ApplicationInstance {
    private static final long serialVersionUID = 6414042684791930462L;

    private PdfProvider pdfProvider = null;
    
	public Window init() {
        Window window = new Window();

        ContentPane contentPane = new Screen(this);
        window.setContent(contentPane);
        window.setTitle("Web Address Book");
        
        // TODO create PdfProvider instance in the background?
        pdfProvider = new PdfProvider();
        
        return window;
    }

	public PdfProvider getPdfProvider() {
		return pdfProvider;
	}
}