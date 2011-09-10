package org.ohm.lebetter.model.impl.entities;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.ohm.lebetter.model.DictSyncAware;
import org.room13.mallcore.model.impl.BaseCreatorRepAwareEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 24.03.2009
 * Time: 14:38:46
 * To change this template use File | Settings | File Templates.
 */
@Entity
@Table(name = "app_color")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ColorEntity
        extends BaseCreatorRepAwareEntity
        implements DictSyncAware {

    private static final long serialVersionUID = 1712626151376359411L;

    private String name = "";
    private String code = "";
    private String colorCode = "";
    private boolean uiColor = false;

    public ColorEntity() {
        setEntityCode("Color");
    }

    @Column(length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Column(length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this, ToStringStyle.DEFAULT_STYLE)
                .append("id", this.getId())
                .append("code", this.getCode())
                .append("name", this.getName());
        return sb.toString();
    }

    @Column(length = 7)
    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }

    @Column(columnDefinition = "boolean default false")
    public boolean isUiColor() {
        return uiColor;
    }

    public void setUiColor(boolean uiColor) {
        this.uiColor = uiColor;
    }
}