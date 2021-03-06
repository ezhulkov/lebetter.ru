package org.ohm.lebetter.spring.service.impl;

import org.ohm.lebetter.model.impl.entities.OrderEntity;
import org.ohm.lebetter.model.impl.entities.OrderToProductEntity;
import org.ohm.lebetter.model.impl.entities.ProductEntity;
import org.ohm.lebetter.spring.service.BaseTest;
import org.room13.mallcore.model.ObjectBaseEntity.Status;
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
public class OrderTest extends BaseTest {

    @BeforeMethod
    public void setUp() throws Exception {
        setUp(DEF_DATASET_NAME);
    }

    @Test
    public void testOrders() {

        Assert.assertEquals(serviceManager.getOrderManager().getAll().size(), 0);
        Assert.assertEquals(serviceManager.getOrderManager().getCurrentOrder(admin, false), null);
        Assert.assertEquals(serviceManager.getOrderManager().getProducts(null).size(), 0);
        Assert.assertNotNull(serviceManager.getOrderManager().getCurrentOrder(admin, true));
        Assert.assertNotNull(serviceManager.getOrderManager().getCurrentOrder(admin, true));
        Assert.assertNotNull(serviceManager.getOrderManager().getCurrentOrder(admin, true));
        Assert.assertNotNull(serviceManager.getOrderManager().getCurrentOrder(admin, false));
        Assert.assertEquals(serviceManager.getOrderManager().getAll().size(), 1);

        ProductEntity prod = new ProductEntity();
        prod.setObjectStatus(Status.READY);
        prod.setPrice(10f);
        serviceManager.getProductManager().create(prod, null, admin);

        ProductEntity prod2 = new ProductEntity();
        prod2.setObjectStatus(Status.READY);
        prod2.setPrice(12f);
        serviceManager.getProductManager().create(prod2, null, admin);

        OrderEntity order = serviceManager.getOrderManager().getCurrentOrder(admin, false);
        OrderToProductEntity link1 = serviceManager.getOrderManager().addOrderToProductLink(prod.getId(), order, admin);
        OrderToProductEntity link2 = serviceManager.getOrderManager().addOrderToProductLink(prod2.getId(), order, admin);
        OrderToProductEntity link3 = serviceManager.getOrderManager().addOrderToProductLink(prod2.getId(), order, admin);

        Assert.assertEquals(serviceManager.getOrderManager().getProducts(order).size(), 3);
        Assert.assertEquals(serviceManager.getOrderManager().getOrderTotal(order), 34f);
        Assert.assertEquals(serviceManager.getOrderManager().getOrderTotal(order, 30), 24f);
        serviceManager.getOrderManager().deleteOrderToProductLink(link1);
        Assert.assertEquals(serviceManager.getOrderManager().getProducts(order).size(), 2);
        serviceManager.getOrderManager().deleteOrderToProductLink(link2);
        Assert.assertEquals(serviceManager.getOrderManager().getProducts(order).size(), 1);
        serviceManager.getOrderManager().deleteOrderToProductLink(link3);
        Assert.assertEquals(serviceManager.getOrderManager().getProducts(order).size(), 0);

    }

}