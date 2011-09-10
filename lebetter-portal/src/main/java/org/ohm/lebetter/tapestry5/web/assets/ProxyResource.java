package org.ohm.lebetter.tapestry5.web.assets;

import org.apache.tapestry5.ioc.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Locale;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 15.04.2010
 * Time: 16:09:24
 * To change this template use File | Settings | File Templates.
 */
public class ProxyResource
        implements Resource {

    private String path;

    public ProxyResource(String path) {
        this.path = path;
    }

    @Override
    public boolean exists() {
        return true;
    }

    @Override
    public InputStream openStream() throws IOException {
        return toURL().openStream();
    }

    @Override
    public Resource forLocale(Locale locale) {
        return this;
    }

    @Override
    public Resource forFile(String path) {
        return new ProxyResource(path);
    }

    @Override
    public Resource withExtension(String extension) {
        return this;
    }

    @Override
    public String getFolder() {
        return "";
    }

    @Override
    public String getFile() {
        return "";
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public URL toURL() {
        String rId = "/org/room13/mallcore/tapestry5/web/assets/proxy.stub";
        return getClass().getClassLoader().getResource(rId);
    }

}