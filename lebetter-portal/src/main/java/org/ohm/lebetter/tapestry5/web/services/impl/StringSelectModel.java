package org.ohm.lebetter.tapestry5.web.services.impl;

import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.util.AbstractSelectModel;

import java.util.LinkedList;
import java.util.List;


public class StringSelectModel extends AbstractSelectModel {
    protected List<String> keys;
    protected String prefix;
    protected Messages messages;

    public StringSelectModel(List<String> keys, String prefix, Messages messages) {
        this.keys = keys;
        this.messages = messages;
        this.prefix = prefix;
    }

    @Override
    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    @Override
    public List<OptionModel> getOptions() {
        LinkedList<OptionModel> models = new LinkedList<OptionModel>();
        if (keys != null) {
            for (String option : keys) {
                models.add(new OptionModelImpl(messages.get(prefix + option), option));
            }
        }
        return models;
    }
}
