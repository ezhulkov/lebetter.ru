package org.ohm.lebetter.model.impl.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.room13.mallcore.model.CreatorAware;
import org.room13.mallcore.model.OwnerAware;
import org.room13.mallcore.model.impl.BaseOwnerAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 21.04.2009
 * Time: 13:50:11
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "app_property")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PropertyEntity
        extends BaseOwnerAwareEntity
        implements CreatorAware, OwnerAware {

    private static final long serialVersionUID = 1417626163573315411L;

    public enum Type {
        LIST, VALUE, DICTIONARY, GROUP
    }

    @Column
    private String name;

    @Column
    @Enumerated(EnumType.STRING)
    private Type type = Type.LIST;

    @Column
    private boolean multiple;

    @Column
    private String dictionary;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    private PropertyEntity parent = null;

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<PropertyValueEntity> values = new ArrayList<PropertyValueEntity>();

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<PropertyToCategoryEntity> categories = new ArrayList<PropertyToCategoryEntity>();

    public PropertyEntity() {
        setEntityCode("Property");
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public PropertyEntity getParent() {
        return parent;
    }

    public void setParent(PropertyEntity parent) {
        this.parent = parent;
    }

    public List<PropertyValueEntity> getValues() {
        return values;
    }

    public void setValues(List<PropertyValueEntity> values) {
        this.values = values;
    }

    public List<PropertyToCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<PropertyToCategoryEntity> categories) {
        this.categories = categories;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", this.getId())
                .append("name", this.getName());
        return sb.toString();
    }


}