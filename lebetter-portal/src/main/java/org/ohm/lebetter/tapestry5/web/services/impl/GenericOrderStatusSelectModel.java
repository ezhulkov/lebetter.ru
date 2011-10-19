package org.ohm.lebetter.tapestry5.web.services.impl;

import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.ClassPropertyAdapter;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.ohm.lebetter.model.impl.entities.OrderEntity.OrderStatus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class GenericOrderStatusSelectModel extends GenericSelectModel<OrderStatus> {

    public GenericOrderStatusSelectModel(boolean isAdmin, OrderStatus currentStatus,
                                         Messages messages,
                                         PropertyAccess access) {
        super(OrderStatus.class);
        super.messages = messages;
        list = new HashSet<OrderStatus>();
        if (isAdmin) {
            list = Arrays.asList(OrderStatus.values());
        } else {
            list.add(currentStatus);
        }
        listL = list;
        ClassPropertyAdapter adapter = access.getAdapter(persistentClass.getClass());
        idFieldAdapter = adapter.getPropertyAdapter("id");
        labelFieldAdapter = adapter.getPropertyAdapter("name");
    }

    @Override
    public List<OptionModel> getOptions() {
        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        for (OrderStatus stat : list) {
//            optionModelList.add(new OptionModelImpl(stat.toString(), messages.get(stat.toString())));
            optionModelList.add(new OptionModelImpl(stat.toString(), "123"));
        }
        return optionModelList;
    }

}
