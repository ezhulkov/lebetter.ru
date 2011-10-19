package org.ohm.lebetter.tapestry5.web.components.base;

import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.room13.mallcore.model.ImageAware;

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
public abstract class AbstractPhotoComponent<T extends ImageAware>
        extends AbstractBaseComponent {

    @Parameter(name = "object", required = false, allowNull = false)
    private T selectedObject;

    public T getSelectedObject() {
        return selectedObject;
    }
}