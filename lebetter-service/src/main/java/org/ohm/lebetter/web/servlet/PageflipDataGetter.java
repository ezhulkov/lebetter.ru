package org.ohm.lebetter.web.servlet;

import org.apache.commons.io.IOUtils;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.ohm.lebetter.model.impl.entities.CategoryEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.spring.service.ServiceManager;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
import org.room13.mallcore.spring.ApplicationContextProvider;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 25.10.11
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class PageflipDataGetter extends HttpServlet {

    public static final RMLogger log = new RMLogger(PageflipDataGetter.class);

    private ServiceManager serviceManager;
    private VelocityEngine velocityEngine;
    private String template;

    @Override
    public void init(ServletConfig config) throws ServletException {
        ApplicationContext context = ApplicationContextProvider.getApplicationContext();
        serviceManager = (ServiceManager) context.getBean("serviceManager");
        velocityEngine = (VelocityEngine) context.getBean("velocityEngine");
        InputStream is = this.getClass().getClassLoader().getResourceAsStream("/config/pageflip/layout.vm");
        try {
            template = IOUtils.toString(is);
        } catch (Exception ex) {
            log.error("", ex);
        }
    }

    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String messageBody;
        try {
            Long cid = Long.parseLong(request.getParameter("cid"));
            CategoryEntity category = serviceManager.getCategoryManager().get(cid);

            List<Long> ids = serviceManager.getProductManager().getIdsByCategory(category, Status.READY);
            List<ProductEntity> products = serviceManager.getProductManager().getAll(ids);
            HashMap<String, Object> params = new HashMap<String, Object>();
            params.put("pages", products);

            StringWriter stringWriter = new StringWriter();
            velocityEngine.evaluate(new VelocityContext(params), stringWriter, "ERROR", new StringReader(template));
            messageBody = stringWriter.toString();
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(messageBody);
        response.flushBuffer();

    }

}
