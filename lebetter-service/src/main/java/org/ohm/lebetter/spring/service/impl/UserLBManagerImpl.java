package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.Constants;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.MailManager;
import org.ohm.lebetter.spring.service.UserLBManager;
import org.room13.mallcore.annotations.Permission;
import org.room13.mallcore.annotations.PermissionsCheckType;
import org.room13.mallcore.model.ImageAware;
import org.room13.mallcore.spring.service.impl.UserManagerImpl;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

public class UserLBManagerImpl
        extends UserManagerImpl<UserEntity, UserEntity>
        implements UserLBManager {

    @Override
    @Transactional
    @Permission(checkType = PermissionsCheckType.ACTION_AND_ENTITY, values = "DEL_USER")
    public void remove(UserEntity user, UserEntity caller) {

        //Unlink owned shops
        ownerDao.removeOwnerRelationsForUser(user);

        genericDao.remove(user);

    }

    public UserLBManagerImpl() {
        super(UserEntity.class);
    }

    @Override
    @Transactional
    public ImageAware.ImageStatus getImageStatus(UserEntity entity, UserEntity caller) {
        return super.getImageStatus(entity, caller);
    }

    @Override
    @Transactional
    public void setImageStatus(UserEntity obe, ImageAware.ImageStatus imageStatus,
                               UserEntity caller) {
        super.setImageStatus(obe, imageStatus, caller);
    }

    @Override
    @Transactional
    public void afterSuccesfullLogin(UserEntity user, HttpServletRequest req) {

        user.setDummy(false);
        user.setActivated(true);
        user.setActivationCode(null);

        super.afterSuccesfullLogin(user, req);

        save(user, null);

    }

    @Override
    protected void sendNewUserEmail(UserEntity user, String activateCode) {
        String serverUrl = Constants.SITE_ROOT_URL;

        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("user", user);
        params.put("link", serverUrl);
        params.put("alink", serverUrl + "/auth/activate/" + user.getRootId() + "/" + activateCode);

        ((MailManager) getServiceManager().getMailEngine()).
                sendMailMessage(user, MailManager.NEW_USER, params);
    }

    @Override
    protected void sendNewPasswordEmail(UserEntity user, String newPass) {
        HashMap<String, Object> params = new HashMap<String, Object>();

        params.put("user", user);
        params.put("newPassword", newPass);

        ((MailManager) getServiceManager().getMailEngine()).
                sendMailMessage(user, MailManager.CHANGE_PASSWD, params);
    }

}
