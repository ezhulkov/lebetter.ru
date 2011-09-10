package org.ohm.lebetter.tapestry5.web.mixins;

import org.apache.tapestry5.annotations.InjectContainer;
import org.apache.tapestry5.annotations.MixinAfter;
import org.apache.tapestry5.beaneditor.BeanModel;
import org.apache.tapestry5.beaneditor.PropertyModel;
import org.apache.tapestry5.corelib.components.Grid;

import java.util.List;

@MixinAfter
public class GridSortingDisabled {

    @InjectContainer
    private Grid grid;

    private void setupRender() {
        BeanModel model = grid.getDataModel();
        List<String> propertyNames = model.getPropertyNames();
        for (String propName : propertyNames) {
            PropertyModel propModel = model.get(propName);
            if (propModel.isSortable()) {
                propModel.sortable(false);
            }
        }
    }

}
