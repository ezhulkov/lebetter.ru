package org.ohm.lebetter.tapestry5.web.pages.base;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;

public abstract class AbstractBrowseBasePage extends AbstractBasePage {

    @Property
    @Inject
    private Block leftMenuBlock;

    @Property
    @Inject
    private Block breadCrumpsBlock;


}