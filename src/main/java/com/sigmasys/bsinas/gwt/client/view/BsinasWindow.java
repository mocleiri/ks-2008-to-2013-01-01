package com.sigmasys.bsinas.gwt.client.view;

import com.extjs.gxt.ui.client.widget.Window;
import com.extjs.gxt.ui.client.widget.layout.FitLayout;
import com.google.gwt.user.client.Element;

/**
 * KSA Main window
 *
 * @author Michael Ivanov
 *         Date: 5/2/12
 */
public class BsinasWindow extends Window {

    public BsinasWindow() {
        setHeading("Kuali Student Accounts");
        setMinimizable(true);
        setMaximizable(true);
        setClosable(true);
        setSize(1000, 700);
        setLayout(new FitLayout());
    }

    @Override
    protected void onRender(Element parent, int pos) {
        super.onRender(parent, pos);
        add(new BsinasPanel());
    }
}