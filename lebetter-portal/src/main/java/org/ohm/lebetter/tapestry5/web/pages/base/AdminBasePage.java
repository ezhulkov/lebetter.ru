package org.ohm.lebetter.tapestry5.web.pages.base;

import org.apache.tapestry5.annotations.Import;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 14:17:53
 * To change this template use File | Settings | File Templates.
 */
@Import(
        stylesheet = {"proxy:/styles/niceforms-default.css",
                      "proxy:/styles/lb-main.css",
                      "proxy:/styles/lb-mbcontainer.css",
                      "proxy:/styles/default.css",
                      "proxy:/styles/style.css",
                      "proxy:/styles/ui.all.css",
                      "proxy:/styles/jquery.selectbox.css",
                      "proxy:/styles/jquery.fancybox-1.3.3.css",
                      "proxy:/styles/jquery.rating.css",
                      "proxy:/styles/jquery.ad-gallery.css",
                      "proxy:/styles/jquery.ssm.css",
                      "proxy:/styles/lb-default.css",
                      "proxy:/styles/lb-layouts.css"}
)
public abstract class AdminBasePage extends AbstractBasePage {

}