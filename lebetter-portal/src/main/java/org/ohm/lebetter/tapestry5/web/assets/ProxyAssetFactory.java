package org.ohm.lebetter.tapestry5.web.assets;

import org.apache.tapestry5.Asset;
import org.apache.tapestry5.ioc.Resource;
import org.apache.tapestry5.services.AssetFactory;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 15.04.2010
 * Time: 16:06:20
 * To change this template use File | Settings | File Templates.
 */
public class ProxyAssetFactory
        implements AssetFactory {

    @Override
    public Resource getRootResource() {
        return new ProxyResource("/");
    }

    @Override
    public Asset createAsset(final Resource resource) {
        return new Asset() {

            public Resource getResource() {
                return resource;
            }

            public String toClientURL() {
                return resource.getPath();
            }

        };
    }
}
