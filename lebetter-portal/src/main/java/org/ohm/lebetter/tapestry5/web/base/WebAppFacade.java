package org.ohm.lebetter.tapestry5.web.base;

import org.ohm.lebetter.tapestry5.web.services.ServiceFacade;
import org.room13.mallcore.log.RMLogger;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 14:29:35
 * To change this template use File | Settings | File Templates.
 */
public interface WebAppFacade {

    public WebAppAuthFacade getAuth();

    public WebAppIOCFacade getIOC();

    public WebAppBaseFacade getBase();

    //*
    //  Often used object for convinience
    //*

    public RMLogger getRMLogger();

    public ServiceFacade getServiceFacade();

}
