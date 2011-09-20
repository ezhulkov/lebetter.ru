package org.ohm.lebetter.tapestry5.web.pages.dealer;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.apache.tapestry5.grid.GridDataSource;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBasePage;
import org.ohm.lebetter.tapestry5.web.util.datasource.GenericEntityGridDS;
import org.room13.mallcore.model.ObjectBaseEntity.Status;

public class List extends AbstractBasePage {

    @Property
    private DealerEntity oneDealer;

    @Cached
    public GridDataSource getDealers() {
        java.util.List<Long> ids = getServiceFacade().getDealerManager().getAllIds(Status.READY, "name", null, null);
        return new GenericEntityGridDS<DealerEntity>(ids, getServiceFacade().getDealerManager(), DealerEntity.class);
    }

}