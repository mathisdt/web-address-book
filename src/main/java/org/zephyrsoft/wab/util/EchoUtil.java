package org.zephyrsoft.wab.util;

import org.zephyrsoft.wab.*;
import nextapp.echo.app.*;

/**
 * utility for routine Echo3 tasks
 */
public class EchoUtil {

	public static Button createButton(String text, String tooltip, String image) {
		ResourceImageReference img = null;
		if (image!=null) {
			img = createImage(image);
		}
		return createButton(text, tooltip, img);
	}

	public static Button createButton(String text, String tooltip, ResourceImageReference image) {
		Button ret = new Button();
		layoutAsButton(ret);
		if (text!=null) {
			ret.setText(text);
		}
		if (tooltip!=null) {
			ret.setToolTipText(tooltip);
		}
		if (image!=null) {
			ret.setIcon(image);
			// set distance between image and text
			ret.setIconTextMargin(new Extent(5));
		}
		return ret;
	}

	private static void layoutAsButton(Button button) {
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

	public static ResourceImageReference createImage(String image) {
		return new ResourceImageReference(image, new Extent(Constants.ICON_PIXEL_SIZE), new Extent(Constants.ICON_PIXEL_SIZE));
	}

}
