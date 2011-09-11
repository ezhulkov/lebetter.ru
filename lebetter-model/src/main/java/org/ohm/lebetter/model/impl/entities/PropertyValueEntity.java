package org.ohm.lebetter.model.impl.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.AccessType;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.RelatedToAbstractObjectAware;
import org.ohm.lebetter.model.embedded.RelatedToAbstractObject;
import org.room13.mallcore.model.CreatorAware;
import org.room13.mallcore.model.OwnerAware;
import org.room13.mallcore.model.impl.BaseOwnerAwareEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
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
@Table(name = "app_property_value")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@AccessType("field")
public class PropertyValueEntity
        extends BaseOwnerAwareEntity
        implements OwnerAware, CreatorAware, RelatedToAbstractObjectAware {

    private static final long serialVersionUID = 2717136138544315411L;

    @Embedded
    private RelatedToAbstractObject relation = new RelatedToAbstractObject();

    @Column
    private String name;

    @Column
    private String additionalDictInfo;

    @Column
    private String additionalDictInfo2;

    @ManyToOne(fetch = FetchType.LAZY)
    private PropertyEntity property;

    @OneToMany(mappedBy = "propertyValue", fetch = FetchType.LAZY, cascade = CascadeType.REFRESH)
    private List<TagToValueEntity> tags = new ArrayList<TagToValueEntity>();

    public PropertyValueEntity() {
        setEntityCode("PropertyValue");
    }

    public RelatedToAbstractObject getRelation() {
        return relation;
    }

    public void setRelation(RelatedToAbstractObject relation) {
        this.relation = relation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAdditionalDictInfo() {
        return additionalDictInfo;
    }

    public void setAdditionalDictInfo(String additionalDictInfo) {
        this.additionalDictInfo = additionalDictInfo;
    }

    public String getAdditionalDictInfo2() {
        return additionalDictInfo2;
    }

    public void setAdditionalDictInfo2(String additionalDictInfo2) {
        this.additionalDictInfo2 = additionalDictInfo2;
    }

    public PropertyEntity getProperty() {
        return property;
    }

    public void setProperty(PropertyEntity property) {
        this.property = property;
    }

    public List<TagToValueEntity> getTags() {
        return tags;
    }

    public void setTags(List<TagToValueEntity> tags) {
        this.tags = tags;
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", this.getId())
                .append("name", this.getName());
        return sb.toString();
    }

}