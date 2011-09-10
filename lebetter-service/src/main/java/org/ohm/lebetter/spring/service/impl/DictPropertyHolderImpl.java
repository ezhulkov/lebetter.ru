package org.ohm.lebetter.spring.service.impl;

import org.apache.commons.lang.StringUtils;
import org.ohm.lebetter.model.impl.entities.PropertyEntity;
import org.ohm.lebetter.spring.service.DictPropertyHolder;
import org.room13.mallcore.log.RMLogger;
import org.room13.mallcore.spring.service.GenericManager;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class DictPropertyHolderImpl implements DictPropertyHolder, ApplicationContextAware {

    private final RMLogger log = new RMLogger(this.getClass());

    private final Map<String, List<PropertyEntity>> dictToPropertyMap =
            Collections.synchronizedMap(new HashMap<String, List<PropertyEntity>>());

    private ApplicationContext context;

    public void clear(){
        dictToPropertyMap.clear();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }

    private RMLogger getLogger() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            log.setRequest(request);
        }
        return log;
    }

    @Override
    public void removeDictCategory(PropertyEntity property) {

        if (property != null && property.getType().equals(PropertyEntity.Type.DICTIONARY)) {

            String dictionary = property.getDictionary().toLowerCase();
            if (getLogger().isTraceEnabled()) {
                getLogger().trace("Removing dict category " + property + ". Dictionary " + dictionary);
            }
            synchronized (dictToPropertyMap) {
                List<PropertyEntity> cats = dictToPropertyMap.get(dictionary);
                if (cats != null) {
                    for (PropertyEntity cat : cats) {
                        if (cat.getId().equals(property.getId())) {
                            cats.remove(cat);
                            break;
                        }
                    }
                }
                if (dictToPropertyMap.get(dictionary) != null &&
                        dictToPropertyMap.get(dictionary).size() == 0) {
                    dictToPropertyMap.remove(dictionary);
                }
            }


        }
    }

    @Override
    public List<PropertyEntity> getDictProperties(String category) {
        return dictToPropertyMap.get(category);
    }

    @Override
    public final Map<String, List<PropertyEntity>> getDictCategoriesMap() {
        return dictToPropertyMap;
    }

    @Override
    public void addNewDictCategory(PropertyEntity property) {

        if (property != null && property.getType().equals(PropertyEntity.Type.DICTIONARY)) {
            String dictionary = property.getDictionary();
            if (!StringUtils.isBlank(dictionary)) {
                dictionary = dictionary.toLowerCase();
                if (getLogger().isTraceEnabled()) {
                    getLogger().trace("Adding dict category " + property + ". Dictionary " + dictionary);
                }
                Object dictManager = context.getBean(dictionary + "Manager");
                if (getLogger().isTraceEnabled()) {
                    getLogger().trace("Dict manager: " + dictManager);
                }
                if (dictManager != null && dictManager instanceof GenericManager) {
                    //Put dict manager to the map for further access
                    synchronized (dictToPropertyMap) {
                        List<PropertyEntity> list = dictToPropertyMap.get(dictionary);
                        if (list == null) {
                            list = new Vector<PropertyEntity>();
                        }
                        list.add(property);
                        dictToPropertyMap.put(dictionary.toLowerCase(), list);
                    }

                } else {
                    getLogger().error("Dict manager is null or not a GenericManager instance.");
                }
            } else {
                getLogger().error("Dictionary field is empty for category " + property);
            }
        }

    }

}
