package org.ohm.lebetter.model.embedded;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 10.04.2009
 * Time: 16:05:21
 * To change this template use File | Settings | File Templates.
 */
@Embeddable
@MappedSuperclass
public class RelatedToAbstractObject implements Serializable {

    private static final long serialVersionUID = 1327251621297799411L;

    private Long objectId;
    private String objectType;

    @Column
    public Long getObjectId() {
        return objectId;
    }

    public void setObjectId(Long objectId) {
        this.objectId = objectId;
    }

    @Column
    public String getObjectType() {
        return objectType;
    }

    public void setObjectType(String objectType) {
        this.objectType = objectType;
    }

}
