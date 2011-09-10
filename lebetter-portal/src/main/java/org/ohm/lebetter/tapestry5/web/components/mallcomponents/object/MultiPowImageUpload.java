package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.BindingConstants;
import org.apache.tapestry5.annotations.Import;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.annotations.Property;
import org.springframework.context.i18n.LocaleContextHolder;

@Import(library = {"proxy:/scripts/swfobject.js"})
public class MultiPowImageUpload {

    @Parameter(required = true, allowNull = false,
            defaultPrefix = BindingConstants.LITERAL)
    @Property(write = false)
    private String id;

    @Parameter(required = true, allowNull = false)
    @Property(write = false)
    private String ticket;

    @Parameter(required = false, allowNull = false, value = "600")
    @Property(write = false)
    private int width;

    @Parameter(required = false, allowNull = false, value = "250")
    @Property(write = false)
    private int height;

    @Parameter(required = false, allowNull = false, value = "false")
    private boolean multipleFiles;


    @Parameter(required = false, allowNull = false,
            value = "Images(JPEG, GIF, PNG)|*.jpg:*.jpeg:*.gif:*.png",
            defaultPrefix = BindingConstants.LITERAL)
    @Property(write = false)
    private String fileFilters;

    @Parameter(required = false, allowNull = false, value = "false")
    @Property(write = false)
    private boolean allowCrop;

    @Parameter(required = false, allowNull = false, value = "1048576")
    @Property(write = false)
    private int maxFileSize;

    @Parameter(required = false, allowNull = false, value = "false")
    @Property(write = false)
    private boolean allowRotate;

    @Parameter(required = false, allowNull = false,
            value = "message:upload.img.btn", defaultPrefix = BindingConstants.LITERAL)
    @Property(write = false)
    private String title;

    public int getMaxFileCount() {
        return multipleFiles ? -1 : 1;
    }

    public String getLang() {
        return LocaleContextHolder.getLocale().getLanguage();
    }

    public boolean isNeedsLocalization() {
        return !"en".equalsIgnoreCase(getLang());
    }
}