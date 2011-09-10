package org.ohm.lebetter.tapestry5.web.components.mallcomponents.object;

import org.apache.tapestry5.Binding;
import org.apache.tapestry5.ComponentResources;
import org.apache.tapestry5.FieldValidator;
import org.apache.tapestry5.MarkupWriter;
import org.apache.tapestry5.OptionModel;
import org.apache.tapestry5.SelectModel;
import org.apache.tapestry5.SelectModelVisitor;
import org.apache.tapestry5.ValidationException;
import org.apache.tapestry5.ValidationTracker;
import org.apache.tapestry5.ValueEncoder;
import org.apache.tapestry5.annotations.BeforeRenderTemplate;
import org.apache.tapestry5.annotations.Environmental;
import org.apache.tapestry5.annotations.Parameter;
import org.apache.tapestry5.corelib.base.AbstractField;
import org.apache.tapestry5.ioc.annotations.Inject;
import org.apache.tapestry5.services.FieldValidatorDefaultSource;
import org.apache.tapestry5.services.Request;
import org.apache.tapestry5.services.ValueEncoderSource;
import org.apache.tapestry5.util.EnumSelectModel;
import org.ohm.lebetter.tapestry5.web.services.MultiValueEncoder;
import org.ohm.lebetter.tapestry5.web.services.impl.SelectMultipleModelRenderer;
import org.room13.mallcore.model.ObjectBaseEntity;

import java.util.List;
import java.util.Locale;

public final class SelectMultiple extends AbstractField {

    private class Renderer extends SelectMultipleModelRenderer {

        public Renderer(MarkupWriter writer) {
            super(writer, _encoder);
        }


        @Override
        @SuppressWarnings("unchecked")
        protected boolean isOptionSelected(OptionModel optionModel) {
            Object value = optionModel.getValue();
            if (_values == null) {
                return false;
            }
            for (Object o : _values) {
                if (o instanceof ObjectBaseEntity) {
                    ObjectBaseEntity obe = (ObjectBaseEntity) o;
                    ObjectBaseEntity valueObe = (ObjectBaseEntity) value;
                    if (valueObe.getRootId().equals(obe.getId())) {
                        return true;
                    }
                }
            }
            return false;
        }

    }

    @Parameter
    private MultiValueEncoder _encoder;

    @Inject
    private FieldValidatorDefaultSource _fieldValidatorDefaultSource;

    @Inject
    private Locale _locale;

    @Parameter(required = true)
    private SelectModel _model;

    @Inject
    private Request _request;

    @Inject
    private ComponentResources _resources;

    @Environmental
    private ValidationTracker _tracker;

    /**
     * Performs input validation on the value supplied by the user in the form submission.
     */
    @Parameter(defaultPrefix = "validate")
    @SuppressWarnings("unchecked")
    private FieldValidator<Object> _validate;

    /**
     * The list of value to read or update.
     */
    @Parameter(required = true, principal = true)
    private List<Object> _values;


    @Inject
    private ValueEncoderSource _valueEncoderSource;

    @Override
    @SuppressWarnings("unchecked")
    protected void processSubmission(String elementName) {
        String[] primaryKeys = _request.getParameters(elementName);
        List<Object> selectedValues = _encoder.toValue(primaryKeys);

        if (selectedValues == null) {
            return;
        }

        try {
            for (Object selectedValue : selectedValues) {
                if (_validate != null) {
                    _validate.validate(selectedValue);
                }
            }
            _values = selectedValues;
        } catch (ValidationException ex) {
            _tracker.recordError(this, ex.getMessage());
            return;
        }
    }

    void afterRender(MarkupWriter writer) {
        writer.end();
    }

    void beginRender(MarkupWriter writer) {
        writer.element("select", "name", getControlName(), "rootId", getClientId(), "multiple", "multiple");
    }

    @SuppressWarnings("unchecked")
    ValueEncoder defaultEncoder() {
        return _valueEncoderSource.getValueEncoder(String.class);
    }

    @SuppressWarnings("unchecked")
    SelectModel defaultModel() {
        Class valueType = _resources.getBoundType("value");

        if (valueType == null) {
            return null;
        }

        if (Enum.class.isAssignableFrom(valueType)) {
            return new EnumSelectModel(valueType, _resources.getContainerMessages());
        }

        return null;
    }

    FieldValidator defaultValidate() {
        Class type = _resources.getBoundType("value");

        if (type == null) {
            return null;
        }

        return _fieldValidatorDefaultSource.createDefaultValidator(
                this,
                _resources.getId(),
                _resources.getContainerMessages(),
                _locale,
                type,
                _resources.getAnnotationProvider("value"));
    }

    Binding defaultValue() {
        return createDefaultParameterBinding("value");
    }

    @BeforeRenderTemplate
    void options(MarkupWriter writer) {
        SelectModelVisitor renderer = new Renderer(writer);

        _model.visit(renderer);
    }

    // For testing.

    void setModel(SelectModel model) {
        _model = model;
    }

    void setValues(List<Object> values) {
        _values = values;
    }

    void setValueEncoder(MultiValueEncoder encoder) {
        _encoder = encoder;
    }
}
