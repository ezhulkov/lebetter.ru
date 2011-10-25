package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object.product;

import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.OnEvent;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductPhotoEntity;
import org.ohm.lebetter.spring.service.Constants.FileNames;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.util.StringUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProductPhotoList extends AbstractBaseComponent {

    @Property
    @Parameter(name = "product", required = true, allowNull = false)
    private ProductEntity product;

    @Property
    private ProductPhotoEntity onePhoto;

    @Inject
    @Property
    private Block listBlock;

    public List<ProductPhotoEntity> getPhotos() {
        return getServiceFacade().getProductPhotoManager().getAllByProduct(product);
    }

    @OnEvent(value = "submit", component = "listForm")
    public Block onDeleteSubmit() {
        Set<Long> ids = new HashSet<Long>();

        List params = getIOC().getRequest().getParameterNames();
        for (Object param : params) {
            if (param instanceof String && ((String) param).startsWith("2RM-")) {
                String name = (String) param;
                String value = getIOC().getRequest().getParameter(name);
                if ("on".equals(value)) {
                    String pId = name.substring("2RM-".length());
                    ids.add(Long.parseLong(pId));
                }
            }
        }

        getServiceFacade().getProductPhotoManager().removeByIdList(ids, getAuth().getUser());

        return listBlock;
    }

    @OnEvent(value = "submit", component = "inlineForm")
    public Block onRenameSubmit(Long pid) {
        Set<Long> ids = new HashSet<Long>();
        List params = getIOC().getRequest().getParameterNames();
        for (Object param : params) {
            if (param instanceof String && ((String) param).startsWith("KS-")) {
                String value = getIOC().getRequest().getParameter((String) param);
                ProductPhotoEntity photo = getServiceFacade().getProductPhotoManager().get(pid);
                photo.setName(value);
                getServiceFacade().getProductPhotoManager().save(photo, getAuth().getUser());
                break;
            }
        }

        getServiceFacade().getProductPhotoManager().removeByIdList(ids, getAuth().getUser());

        return listBlock;
    }

    public Block onActionFromSetMainAjax(Long pid) {
        ProductPhotoEntity photo = getServiceFacade().getProductPhotoManager().get(pid);
        getServiceFacade().getProductPhotoManager().setMainPhoto(photo);
        return listBlock;
    }

    public String getDeleteLink() {
        return getIOC().getResources().createEventLink("deleteAjax", new Object[]{"PARAM"}).toURI();
    }

    public Block onDeleteAjax(String pids) throws Exception {

        Set<Long> ids = new HashSet<Long>();

        if (!StringUtil.isEmpty(pids)) {
            String[] spids = pids.split("-");
            for (String spid : spids) {
                if (!StringUtil.isEmpty(spid)) {
                    ids.add(Long.parseLong(spid));
                }
            }
        }

        getServiceFacade().getProductPhotoManager().removeByIdList(ids, getAuth().getUser());

        return listBlock;
    }

    public String getImageUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(onePhoto, FileNames.SMALL_PHOTO.toString());
    }

}