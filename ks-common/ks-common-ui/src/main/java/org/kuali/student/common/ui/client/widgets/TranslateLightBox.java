package org.kuali.student.common.ui.client.widgets;

import java.util.List;

import org.kuali.student.common.ui.client.widgets.KSButton;
import org.kuali.student.common.ui.client.widgets.KSLabel;
import org.kuali.student.common.ui.client.widgets.KSLightBox;
import org.kuali.student.common.ui.client.widgets.KSTextBox;
import org.kuali.student.common.ui.client.widgets.search.SearchResultsTable;
import org.kuali.student.r1.common.assembly.data.LookupMetadata;
import org.kuali.student.r1.common.assembly.data.Metadata;
import org.kuali.student.r1.common.search.dto.SearchRequest;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TranslateLightBox extends Composite{
    private KSLightBox lightbox;
    private KSButton closeButton, saveButton;
    private KSLabel afrikaansLabel, englishLabel;
    private KSTextBox afrikaansTextBox, englishTextBox;
    private SearchRequest searchRequest;
    private LookupMetadata lookupMetadata;
    
    public TranslateLightBox(String title, List<Metadata> fieldsMetadata) {
        VerticalPanel contents = new VerticalPanel();
        lightbox = new KSLightBox(title);
        afrikaansLabel.setText("Afrikaans");
        englishLabel.setText("English");
        saveButton = new KSButton("OK");
        closeButton = new KSButton("Cancel");
        
        this.searchRequest = searchRequest;
        this.lookupMetadata = lookupMetadata;

        
        
        contents.add(afrikaansLabel);
        contents.add(afrikaansTextBox);
        contents.add(englishLabel);
        contents.add(englishTextBox);
        contents.add(saveButton);
        contents.add(closeButton);
        
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        
        lightbox.setWidget(contents);
    }
    
    public TranslateLightBox() {
    	VerticalPanel contents = new VerticalPanel();
        lightbox = new KSLightBox("Test popup");
        afrikaansLabel.setText("Afrikaans");
        englishLabel.setText("English");
        saveButton = new KSButton("OK");
        closeButton = new KSButton("Cancel");

        contents.add(afrikaansLabel);
        contents.add(afrikaansTextBox);
        contents.add(englishLabel);
        contents.add(englishTextBox);
        contents.add(saveButton);
        contents.add(closeButton);
        
        closeButton.addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                hide();
            }
        });
        
        lightbox.setWidget(contents);
	}

	public void setSize(int width, int height) {
        lightbox.setSize(width, height);
    }
    
    public void show() {
        lightbox.show();
    }
    
    public void hide() {
        lightbox.hide();
    }
}
