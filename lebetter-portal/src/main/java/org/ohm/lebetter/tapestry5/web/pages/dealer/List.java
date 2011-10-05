package org.ohm.lebetter.tapestry5.web.pages.dealer;

import org.apache.tapestry5.annotations.Cached;
import org.apache.tapestry5.annotations.Property;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.pages.base.AbstractBrowseBasePage;

public class List extends AbstractBrowseBasePage {

    @Property
    private DealerEntity oneDealer;

    @Property
    private String oneCity;

    @Cached
    public java.util.List<String> getCities() {
        return getServiceFacade().getDealerManager().getCities();
    }

    public java.util.List<DealerEntity> getDealers() {
        return getServiceFacade().getDealerManager().getAllReadyByCity(oneCity);
    }

}