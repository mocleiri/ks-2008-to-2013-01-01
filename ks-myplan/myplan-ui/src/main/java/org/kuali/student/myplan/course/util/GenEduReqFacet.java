package org.kuali.student.myplan.course.util;

import org.kuali.student.myplan.course.dataobject.CourseSearchItem;
import org.kuali.student.myplan.course.dataobject.FacetItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
*  Logic for building list of FacetItems and coding CourseSearchItems.
*/
public class GenEduReqFacet extends AbstractFacet {

    private final String NONE_GENED_FACET_KEY = "None";


    public GenEduReqFacet() {
        super();
    }

    @Override
    // TODO Irradicate this garbage. Quickfix. -- JO
    public List<FacetItem> getFacetItems() {
        List<FacetItem> list = super.getFacetItems();
        if( showNone )
        {
            FacetItem item = new FacetItem();
            item.setDisplayName( "None" );
            item.setKey( ";None;" );
            list.add( item );
        }
        return list;
    }

    boolean showNone = false;
    /**
     * {@inheritDoc}
     */
    @Override
    public void process(CourseSearchItem item) {

        String genEdString = item.getGenEduReq();
        boolean isUnknown = false;

        //  If no gen edu req info was set then setup for an "Unknown" facet.
        if (genEdString == null || genEdString.equals(CourseSearchItem.EMPTY_RESULT_VALUE_KEY) || genEdString.equals("")) {
            isUnknown = true;
            showNone = true;
//            genEdString = UNKNOWN_FACET_KEY;
            genEdString = "None";
        }

        /*
         *  TODO: UW SPECIFIC
         */
        //  Remove white space before tokenizing.
        genEdString = genEdString.replaceAll("\\s+", "");
        String k[] = genEdString.split(",");
        List<String> keys = new ArrayList<String>(Arrays.asList(k));
        for (String key : keys)
        {
            if (isNewFacetKey( FACET_KEY_DELIMITER + key + FACET_KEY_DELIMITER))
            {
                FacetItem fItem = new FacetItem();
                String displayName = null;
                //  Use the key as the display name if it wasn't set to "Unknown" above.
                if (isUnknown) {
                    displayName = UNKNOWN_FACET_DISPLAY_NAME;
                } else {
                    displayName = key;
                }
                fItem.setKey(FACET_KEY_DELIMITER + key + FACET_KEY_DELIMITER);
                fItem.setDisplayName(displayName);
                facetItems.add(fItem);
            }
        }

        //  Convert the list back into a string which can be matched against on the client.
        StringBuilder kb = new StringBuilder();
        for (String key : keys)
        {
            kb.append(FACET_KEY_DELIMITER).append(key).append(FACET_KEY_DELIMITER);
        }
        item.setGenEduReqFacetKey(kb.toString());
    }
}
