package org.zephyrsoft.wab.util;

import nextapp.echo.app.*;

public class EchoUtil {
	
	public static final int KEYSTROKE_SEND_INTERVAL = 50;
	
	public static void layoutAsButton(Button button) {
		button.setBackground(Color.LIGHTGRAY);
        button.setInsets(new Insets(7, 1));
        button.setBorder(new Border(new Extent(2), Color.DARKGRAY, Border.STYLE_OUTSET));
	}
	
}
