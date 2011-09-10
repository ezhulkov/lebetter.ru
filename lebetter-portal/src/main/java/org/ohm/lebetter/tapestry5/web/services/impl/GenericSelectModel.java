package org.ohm.lebetter.tapestry5.web.services.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.tapestry5.OptionGroupModel;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.internal.OptionGroupModelImpl;
import org.apache.tapestry5.internal.OptionModelImpl;
import org.apache.tapestry5.ioc.Messages;
import org.apache.tapestry5.ioc.services.PropertyAccess;
import org.apache.tapestry5.ioc.services.PropertyAdapter;
import org.apache.tapestry5.util.AbstractSelectModel;
import org.room13.mallcore.model.impl.entities.RoleEntity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class GenericSelectModel<T> extends AbstractSelectModel implements ValueEncoder<T> {

    protected Messages messages;
    protected PropertyAdapter labelFieldAdapter;
    protected PropertyAdapter idFieldAdapter;
    protected Collection<T> list;
    protected Collection<T> listL;
    protected List<OptionGroupModel> optionGroups;
    protected Class<T> persistentClass;

    public GenericSelectModel(final Class<T> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public GenericSelectModel() {
    }

    public GenericSelectModel(Collection<T> list, Collection<T> listL,
                              Class<T> clazz, String labelField,
                              String idField, PropertyAccess access) {
        this.list = list;
        this.listL = listL;
        if (idField != null) {
            this.idFieldAdapter = access.getAdapter(clazz).getPropertyAdapter(idField);
        }
        if (labelField != null) {
            this.labelFieldAdapter = access.getAdapter(clazz).getPropertyAdapter(labelField);
        }
    }

    public void addOptionGroup(String label, boolean disabled, List<T> options) {
        List<OptionModel> optionModels = new ArrayList<OptionModel>();
        if (labelFieldAdapter == null) {
            for (T obj : options) {
                optionModels.add(new OptionModelImpl(nvl(obj), obj));
            }
        } else {
            for (T obj : options) {
                optionModels.add(new OptionModelImpl(nvl(labelFieldAdapter.get(obj)), obj));
            }
        }

        if (optionGroups == null) {
            optionGroups = new ArrayList<OptionGroupModel>();
        }

        optionGroups.add(new OptionGroupModelImpl(label, disabled, optionModels, new String[0]));
    }

    public List<OptionGroupModel> getOptionGroups() {
        return null;
    }

    public List<OptionModel> getOptions() {

        if (list == null) {
            return new LinkedList<OptionModel>();
        }

        List<OptionModel> optionModelList = new ArrayList<OptionModel>();
        if (labelFieldAdapter == null) {
            for (T obj : listL) {
                optionModelList.add(new OptionModelImpl(nvl(obj)));
            }
        } else {
            for (T obj : listL) {
                Object selectLabelEntry = labelFieldAdapter.get(obj);
                if (obj instanceof RoleEntity && selectLabelEntry instanceof String) {
                    selectLabelEntry = StringUtils.repeat("&nbsp;", ((RoleEntity) obj).getOffset() * 3) +
                                       selectLabelEntry;
                }
                optionModelList.add(new OptionModelImpl(nvl(selectLabelEntry), obj));
            }
        }
        return optionModelList;
    }

    // ValueEncoder functions
    public String toClient(T obj) {
        if (list == null) {
            return "";
        }
        if (idFieldAdapter == null) {
            return obj + "";
        } else {
            return idFieldAdapter.get(obj) + "";
        }
    }

    public T toValue(String string) {
        if (idFieldAdapter == null) {
            for (T obj : list) {
                if (nvl(obj).equals(string)) {
                    return obj;
                }
            }
        } else {
            for (T obj : list) {
                if (nvl(idFieldAdapter.get(obj)).equals(string)) {
                    return obj;
                }
            }
        }
        return null;
    }

    private String nvl(Object o) {
        if (o == null) {
            return "";
        } else {
            return o.toString();
        }
    }
}