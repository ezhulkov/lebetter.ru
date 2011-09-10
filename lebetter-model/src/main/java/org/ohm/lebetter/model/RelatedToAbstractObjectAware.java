package org.ohm.lebetter.model;

import org.ohm.lebetter.model.embedded.RelatedToAbstractObject;
import org.room13.mallcore.model.ObjectBaseEntity;
/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.04.2009
 * Time: 9:56:32
 * To change this template use File | Settings | File Templates.
 * <p/>
 * Interface for objects that could be rated and viewed to get its popularity
 */
public interface RelatedToAbstractObjectAware extends ObjectBaseEntity {

    public RelatedToAbstractObject getRelation();

    public void setRelation(RelatedToAbstractObject relation);

}