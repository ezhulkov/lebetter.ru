package org.ohm.lebetter.tapestry5.web.components.mallcomponents.layout;

import org.apache.tapestry5.annotations.Import;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 26.03.2009
 * Time: 13:04:15
 * To change this template use File | Settings | File Templates.
 */
@Import(
        stylesheet = {"proxy:/styles/lb-admin.css"},
        library = {"proxy:/scripts/swfobject.js",
                   "proxy:/scripts/superfish.js"}
)
public class OfficeLayout extends AbstractBaseComponent {


}