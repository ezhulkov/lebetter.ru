package org.ohm.lebetter.tapestry5.web.data;

/**
 * Created by IntelliJ IDEA.
 * User: ezhulkov
 * Date: 16.03.2009
 * Time: 17:16:04
 * To change this template use File | Settings | File Templates.
 */
public class FlashMessage {

    public enum Type {
        SUCCESS, FAILURE;
    }

    private String message;
    private Type type;

    public FlashMessage(String message, Type type) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

}