package org.zephyrsoft.wab.util;

import nextapp.echo.app.*;
import nextapp.echo.app.Font.*;

public class EchoUtil {
	
	public static final int KEYSTROKE_SEND_INTERVAL = 50;
	
	public static void layoutAsButton(Button button) {
		button.setBackground(Color.LIGHTGRAY);
        button.setInsets(new Insets(7, 1));
        button.setBorder(new Border(new Extent(1), Color.DARKGRAY, Border.STYLE_OUTSET));
	}
	
	public static Column createSmallLabel(Component component, String labelText) {
		Column ret = new Column();
		ret.setInsets(new Insets(0));
		ret.setCellSpacing(new Extent(0));
		
		Row labelRow = new Row();
		labelRow.setInsets(new Insets(0));
		Label label = new Label(labelText);
		label.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, new Extent(9)));
		label.setForeground(Color.LIGHTGRAY);
		labelRow.add(label);
		ret.add(labelRow);
		Row componentRow = new Row();
		componentRow.setInsets(new Insets(0));
		componentRow.add(component);
		ret.add(componentRow);
		
		return ret;
	}
	
}
