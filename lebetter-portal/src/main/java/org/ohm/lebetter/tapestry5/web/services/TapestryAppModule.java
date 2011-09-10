package org.ohm.lebetter.tapestry5.web.services;

import org.apache.tapestry5.Translator;
import org.apache.tapestry5.ioc.Configuration;
import org.apache.tapestry5.ioc.MappedConfiguration;
import org.apache.tapestry5.ioc.annotations.Contribute;
import org.apache.tapestry5.ioc.annotations.InjectService;
import org.apache.tapestry5.ioc.annotations.Local;
import org.apache.tapestry5.ioc.services.Coercion;
import org.apache.tapestry5.ioc.services.CoercionTuple;
import org.apache.tapestry5.ioc.services.ThreadLocale;
import org.apache.tapestry5.ioc.services.TypeCoercer;
import org.apache.tapestry5.services.AssetFactory;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.Context;
import org.apache.tapestry5.services.RequestExceptionHandler;
import org.apache.tapestry5.services.RequestGlobals;
import org.apache.tapestry5.services.ResponseRenderer;
import org.apache.tapestry5.services.TranslatorSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.JavaScriptStackSource;
import org.apache.tapestry5.util.StringToEnumCoercion;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.ohm.lebetter.tapestry5.web.assets.ProxyAssetFactory;
import org.ohm.lebetter.tapestry5.web.services.impl.ServiceFacadeImpl;
import org.ohm.lebetter.tapestry5.web.translator.DateToStringTranslator;
import org.ohm.lebetter.tapestry5.web.util.KSRequestExceptionHandler;
import org.room13.mallcore.spring.service.DeclinationManager;
import org.room13.mallcore.spring.service.DeclinationManager.WordGender;
import org.room13.mallcore.spring.service.RuDeclinationManager;
import org.slf4j.Logger;

import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 11.03.2009
 * Time: 12:54:26
 * To change this template use File | Settings | File Templates.
 */
public class TapestryAppModule {

    public TapestryAppModule() {
    }

    @Contribute(AssetSource.class)
    public static void contributeAssetSource(MappedConfiguration<String, AssetFactory> configuration,
                                             @InjectService("serviceManager")
                                             final ServiceManager serviceManager) {
        configuration.add("proxy", new ProxyAssetFactory());
    }

    @Contribute(JavaScriptStackSource.class)
    public static void contributeJavaScriptStackSource(
            MappedConfiguration<String, JavaScriptStack> configuration) {
        configuration.addInstance("jquery", JQueryStack.class);
    }

    public RequestExceptionHandler buildAppRequestExceptionHandler(RequestGlobals requestGlobals,
                                                                   ResponseRenderer renderer) {
        return new KSRequestExceptionHandler(requestGlobals, renderer);
    }

    public void contributeServiceOverride(MappedConfiguration<Class, Object> configuration,
                                          @Local RequestExceptionHandler handler) {
        configuration.add(RequestExceptionHandler.class, handler);
    }

    @Contribute(TypeCoercer.class)
    public static void contributeTypeCoercer(Configuration<CoercionTuple> configuration) {
        Coercion<String, WordGender> wordGenderCoercion =
                new StringToEnumCoercion<WordGender>(DeclinationManager.WordGender.class);
        configuration.add(new CoercionTuple<String, DeclinationManager.WordGender>(
                String.class, DeclinationManager.WordGender.class, wordGenderCoercion
        ));

        Coercion<String, RuDeclinationManager.Declination> declinationCoercion =
                new StringToEnumCoercion<RuDeclinationManager.Declination>(
                        RuDeclinationManager.Declination.class);
        configuration.add(new CoercionTuple<String, RuDeclinationManager.Declination>(
                String.class, RuDeclinationManager.Declination.class, declinationCoercion
        ));
    }

    public static ServiceFacade buildServiceFacade(final Logger log,
                                                   final Context context,
                                                   final ThreadLocale threadLocale,
                                                   @InjectService("serviceManager")
                                                   final ServiceManager serviceManager) {
        return new ServiceFacadeImpl(log, context, threadLocale, serviceManager);
    }

    @Contribute(TranslatorSource.class)
    public static void contributeTranslatorSource(MappedConfiguration<Class, Translator> configuration) {
        configuration.add(Date.class, new DateToStringTranslator());
    }

}
