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

    private String name = "";
    private String code = "";
    private String alias = "";
    private String description = "";
    private Type type = Type.LIST;
    private boolean multiple = false;
    private boolean mandatory = false;
    private boolean specialFilter = false;
    private PropertyEntity parent = null;
    private List<PropertyValueEntity> values = new ArrayList<PropertyValueEntity>();
    private List<PropertyToCategoryEntity> categories =
            new ArrayList<PropertyToCategoryEntity>();
    private String dictionary = "";

    public PropertyEntity() {
        setEntityCode("Property");
    }

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Column(columnDefinition = "boolean default false")
    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public List<PropertyValueEntity> getValues() {
        return values;
    }

    public void setValues(List<PropertyValueEntity> values) {
        this.values = values;
    }

    @Column(length = 255)
    public String getDictionary() {
        return dictionary;
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    @Column(length = 255)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column
    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    public PropertyEntity getParent() {
        return parent;
    }

    public void setParent(PropertyEntity parent) {
        this.parent = parent;
    }

    @OneToMany(mappedBy = "property", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    public List<PropertyToCategoryEntity> getCategories() {
        return categories;
    }

    public void setCategories(List<PropertyToCategoryEntity> productTypesByProperties) {
        this.categories = productTypesByProperties;
    }

    @Column
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(name = "special_filter")
    public boolean isSpecialFilter() {
        return specialFilter;
    }

    public void setSpecialFilter(boolean specialFilter) {
        this.specialFilter = specialFilter;
    }

    @Column
    public boolean isMandatory() {
        return mandatory;
    }

    public void setMandatory(boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", this.getId())
                .append("name", this.getName());
        return sb.toString();
    }


}