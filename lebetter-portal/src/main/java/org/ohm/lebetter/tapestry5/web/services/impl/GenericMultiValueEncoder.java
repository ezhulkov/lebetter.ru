package org.ohm.lebetter.tapestry5.web.services.impl;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 22.06.2009
 * Time: 14:03:11
 * To change this template use File | Settings | File Templates.
 */

import org.apache.commons.beanutils.BeanUtils;
import org.ohm.lebetter.tapestry5.web.services.MultiValueEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GenericMultiValueEncoder<T> implements MultiValueEncoder<T> {

    private List<T> list;
    private List<T> listL;

    private String labelField;

    public GenericMultiValueEncoder(List<T> list, List<T> listL, String labelField) {
        this.list = list;
        this.listL = listL;
        this.labelField = labelField;
    }

    public List<String> toClient(T obj) {
        try {
            return Arrays.asList(BeanUtils.getArrayProperty(obj, labelField));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<T> toValue(String[] strings) {

        if (strings == null) {
            return new ArrayList<T>();
        }

        try {
            List<String> test = Arrays.asList(strings);
            List<T> valueList = new ArrayList<T>();
            for (int i = 0; i < listL.size(); i++) {
                T obj = listL.get(i);
                if (test.contains(BeanUtils.getProperty(obj, labelField))) {
                    valueList.add(list.get(i));
                }
            }
            return valueList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}