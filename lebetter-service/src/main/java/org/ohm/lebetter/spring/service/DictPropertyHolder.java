package org.ohm.lebetter.spring.service;

import org.ohm.lebetter.model.impl.entities.PropertyEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: eugene
 * Date: 10.08.2009
 * Time: 16:16:12
 * To change this template use File | Settings | File Templates.
 */
public interface DictPropertyHolder {

    public void removeDictCategory(PropertyEntity property);

    public List<PropertyEntity> getDictProperties(String category);

    public Map<String, List<PropertyEntity>> getDictCategoriesMap();

    public void addNewDictCategory(PropertyEntity property);

    public void clear();
}