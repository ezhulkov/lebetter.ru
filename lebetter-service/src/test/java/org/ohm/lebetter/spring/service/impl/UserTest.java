package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.Constants.Actions;
import org.ohm.lebetter.Constants.Roles;
import org.ohm.lebetter.model.impl.entities.UserEntity;
import org.ohm.lebetter.spring.service.BaseTest;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 11.09.11
 * Time: 15:17
 * To change this template use File | Settings | File Templates.
 */
public class UserTest extends BaseTest {

    @BeforeMethod
    public void setUp() throws Exception {
        setUp(DEF_DATASET_NAME);
    }

    @Test
    public void testUsers() {

        Assert.assertTrue(serviceManager.getRoleManager().isRoleAssigned(admin, Roles.ROLE_ADMIN));
        Assert.assertTrue(serviceManager.getRoleManager().isRoleAssigned(admin, Roles.ROLE_HUMAN));
        Assert.assertFalse(serviceManager.getRoleManager().isRoleAssigned(admin, Roles.ROLE_MANAGER));
        Assert.assertTrue(serviceManager.getRoleManager().isActionGranted(admin, Actions.ADD_PRODUCT));
        Assert.assertTrue(serviceManager.getRoleManager().isActionGranted(admin, Actions.EDIT_PRODUCT));
        Assert.assertTrue(serviceManager.getRoleManager().isActionGranted(admin, Actions.DEL_PRODUCT));
        Assert.assertTrue(serviceManager.getRoleManager().isActionGranted(admin, Actions.ADD_USER));
        Assert.assertTrue(serviceManager.getRoleManager().isActionGranted(admin, Actions.EDIT_USER));
        Assert.assertTrue(serviceManager.getRoleManager().isActionGranted(admin, Actions.DEL_USER));

        UserEntity dealer = serviceManager.getUserManager().getNewInstance();
        serviceManager.getUserManager().create(dealer, admin, admin);

        serviceManager.getRoleManager().assignRoleToUser(dealer, serviceManager.getRoleManager().getRoleByCode(Roles.ROLE_DEALER));

        Assert.assertFalse(serviceManager.getRoleManager().isActionGranted(dealer, Actions.ADD_PRODUCT));
        Assert.assertFalse(serviceManager.getRoleManager().isActionGranted(dealer, Actions.EDIT_PRODUCT));
        Assert.assertFalse(serviceManager.getRoleManager().isActionGranted(dealer, Actions.DEL_PRODUCT));
        Assert.assertFalse(serviceManager.getRoleManager().isActionGranted(dealer, Actions.ADD_USER));
        Assert.assertFalse(serviceManager.getRoleManager().isActionGranted(dealer, Actions.EDIT_USER));
        Assert.assertFalse(serviceManager.getRoleManager().isActionGranted(dealer, Actions.DEL_USER));

    }

}
