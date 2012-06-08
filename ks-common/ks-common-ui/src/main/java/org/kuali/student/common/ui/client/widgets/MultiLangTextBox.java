package org.kuali.student.common.ui.client.widgets;

import org.kuali.student.common.ui.client.widgets.KSButton;
import org.kuali.student.common.ui.client.widgets.KSTextBox;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class MultiLangTextBox extends Composite implements
      ClickHandler {

    private KSTextBox textBox = new KSTextBox();
    private KSButton translateButton = new KSButton();
    private TranslateLightBox popup = new TranslateLightBox();

    /**
     * Constructs an OptionalTextBox with the given caption on the check.
     * 
     * @param caption the caption to be displayed with the check box
     */
    public MultiLangTextBox() {
      HorizontalPanel panel = new HorizontalPanel();
      panel.add(textBox);
      panel.add(translateButton);

      translateButton.addClickHandler(this);
      
      // All composites must call initWidget() in their constructors.
      initWidget(panel);
    }

    public void onClick(ClickEvent event) {
    	popup.show();
    }

}
