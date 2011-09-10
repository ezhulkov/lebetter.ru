package org.ohm.lebetter.tapestry5.web.services;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.SymbolConstants;
import org.apache.tapestry5.func.F;
import org.apache.tapestry5.func.Mapper;
import org.apache.tapestry5.ioc.annotations.Symbol;
import org.apache.tapestry5.services.AssetSource;
import org.apache.tapestry5.services.javascript.JavaScriptStack;
import org.apache.tapestry5.services.javascript.StylesheetLink;

import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 14.04.2011
 * Time: 12:23:19
 * To change this template use File | Settings | File Templates.
 */
public class JQueryStack implements JavaScriptStack {

    private List<Asset> javaScriptStack;

    private final boolean productionMode;

    public JQueryStack(
            @Symbol(SymbolConstants.PRODUCTION_MODE)
            final boolean productionMode,
            final AssetSource assetSource) {

        this.productionMode = productionMode;

        final Mapper<String, Asset> pathToAsset = new Mapper<String, Asset>() {
            @Override
            public Asset map(String path) {
                return assetSource.getExpandedAsset(path);
            }
        };

        javaScriptStack = F
                .flow("proxy:/scripts/jquery-1.5.2.min.js")
                .map(pathToAsset).toList();

    }

    @Override
    public List<String> getStacks() {
        return Collections.emptyList();
    }

    @Override
    public List<Asset> getJavaScriptLibraries() {
        return javaScriptStack;
    }

    @Override
    public List<StylesheetLink> getStylesheets() {
        return Collections.emptyList();
    }

    @Override
    public String getInitialization() {
        return productionMode ? null : "Tapestry.DEBUG_ENABLED = true;";
    }
}
