package org.ohm.lebetter.tapestry5.web.pages.po.pcatalog;

import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;

import java.util.LinkedList;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 16.09.11
 * Time: 18:59
 * To change this template use File | Settings | File Templates.
 */
public class Calc extends AbstractBasePage {

    private int till = 1;

    @Property
    private String oneItem;

    public void onActivate(int till) throws Exception {
        this.till = till;
    }

    public java.util.List<String> getItems() {
        java.util.List<String> result = new LinkedList<String>();
        for (int i = 1; i < till; i++) {
            String item = Integer.toString(i);
            if (item.length() == 1) {
                item = "00" + item;
            } else if (item.length() == 2) {
                item = "0" + item;
            }
            result.add(item);
        }
        return result;
    }


}