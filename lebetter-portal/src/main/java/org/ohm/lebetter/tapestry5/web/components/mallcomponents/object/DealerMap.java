package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.annotations.Component;
import org.apache.tapestry5.corelib.components.Hidden;
import org.ohm.lebetter.model.impl.entities.DealerEntity;
import org.ohm.lebetter.tapestry5.web.components.base.AbstractEditComponent;

public class DealerMap extends AbstractEditComponent {

    @Component(id = "lat", parameters = {"value=selectedObject.lat"})
    private Hidden propertyLatField;

    @Component(id = "lng", parameters = {"value=selectedObject.lng"})
    private Hidden propertyLngField;

    @Component(id = "zoom", parameters = {"value=selectedObject.zoom"})
    private Hidden propertyZoomField;

    public DealerEntity getSelectedObject() {
        return (DealerEntity) getSelectedObjectInternal();
    }

    public void setSelectedObject(DealerEntity object) {
        setSelectedObjectInternal(object);
    }

}