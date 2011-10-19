package org.ohm.lebetter.tapestry5.web.components.base;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.InjectComponent;
import org.apache.tapestry5.annotations.Parameter;
import org.ohm.lebetter.tapestry5.web.components.mallcomponents.control.EditObject;
import org.room13.mallcore.model.ObjectBaseEntity;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 21.04.2009
 * Time: 17:01:24
 * To change this template use File | Settings | File Templates.
 */
@Import(
        stylesheet = {"proxy:/redactor/css/redactor.css"},
        library = {"proxy:/redactor/redactor.js"}
)
public abstract class AbstractEditComponent
        extends AbstractBaseComponent {

    @Parameter(name = "object", required = false, allowNull = false)
    private ObjectBaseEntity selectedObject;

    @InjectComponent
    private EditObject editObject;

    protected final ObjectBaseEntity getSelectedObjectInternal() {
        return selectedObject;
    }

    protected void setSelectedObjectInternal(final ObjectBaseEntity selectedObject) {
        this.selectedObject = selectedObject;
    }

    protected final ObjectBaseEntity getSelectedRootObjectInternal() {
        if (editObject.isNewObject() ||
            getSelectedObjectInternal().isRootObject()) {
            return getSelectedObjectInternal();
        } else {
            return editObject.getObjectManager().getRootDuplicate(getSelectedObjectInternal().getRootId());
        }
    }

    public EditObject getEditObject() {
        return editObject;
    }
}