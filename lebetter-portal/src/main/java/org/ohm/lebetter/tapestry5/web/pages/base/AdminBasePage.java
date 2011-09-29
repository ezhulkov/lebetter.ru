package org.ohm.lebetter.tapestry5.web.pages.base;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 14:17:53
 * To change this template use File | Settings | File Templates.
 */
public abstract class AdminBasePage extends AbstractBasePage {

    @Property
    @Inject
    private Block breadCrumpsBlock;

}