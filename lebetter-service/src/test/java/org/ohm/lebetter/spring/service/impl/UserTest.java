package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.Constants.Roles;
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

    }

}
