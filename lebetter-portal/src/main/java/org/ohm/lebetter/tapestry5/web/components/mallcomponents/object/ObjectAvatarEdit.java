package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.Block;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractBaseComponent;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.model.ImageAware.ImageStatus;
import org.room13.mallcore.model.impl.Ticket;
import org.room13.mallcore.spring.service.ImageAwareManager;

import java.util.Date;

import static org.room13.mallcore.spring.service.DataManager.FileNames.BIG_AVATAR_FILE;

public class ObjectAvatarEdit extends AbstractBaseComponent {

    @Property
    @Parameter(name = "object", required = true, allowNull = false)
    private ImageAware selectedObject;

    @Parameter(name = "avatarManager", required = true, allowNull = false)
    private ImageAwareManager avatarManager;

    @Parameter(name = "handler", required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String handler;

    @Property
    @Parameter(name = "title", required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL)
    private String title;

    @Property
    @Parameter(name = "maxFileSize", required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL, value = "1048576")
    private String maxFileSize;

    @Property
    @Parameter(name = "id", required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL,
               value = "oae")
    private String id;

    @Property
    @Parameter(name = "noAvatarURL", required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL,
               value = "/images/pic/no-avatar2.gif")
    private String noAvatarURL;

    @Parameter(name = "imageToShow", required = false, allowNull = false,
               defaultPrefix = BindingConstants.LITERAL,
               value = "big_avatar.jpg")
    private String imageToShow = BIG_AVATAR_FILE.getFileName();

    @Property(write = false)
    @Parameter(required = false, allowNull = false, value = "*.jpg;*.jpeg;",
               defaultPrefix = BindingConstants.LITERAL)
    private String allowedExt;

    public ImageAwareManager getAvatarManager() {
        return avatarManager;
    }

    public void setAvatarManager(ImageAwareManager avatarManager) {
        this.avatarManager = avatarManager;
    }

    public String getAvatarUrl() {
        return getServiceFacade().getDataManager().getDataFullURL(selectedObject, imageToShow);
    }

    public String getTicket() {
        return getServiceFacade().getUploadTicketService().lease(new Ticket(
                getAuth().getUser().getRootId(),
                selectedObject.getRootId(),
                selectedObject.getEntityCode(),
                getHandler().toUpperCase(), new Date()
        ));
    }

    public String getHandler() {
        return handler == null ? "avatar" : handler;
    }

    public Block onActionFromDelPhotoAjax(Long uid) {
        ImageAware object = (ImageAware) avatarManager.get(uid);
        if (object != null) {
            avatarManager.setImageStatus(object, ImageStatus.NOT_SET, getAuth().getUser());
        }
        return getAjaxBlock();
    }

    public String getFileFilters() {
        if (StringUtils.isBlank(allowedExt)) {
            return "Images(JPEG, GIF, PNG)|*.jpg:*.jpeg:*.gif:*.png";
        }
        String filters = allowedExt.replaceAll("\\;", ":");
        return "Images|" + (filters.endsWith(":") ?
                            filters = filters.substring(0, filters.length() - 1) : filters);
    }

}