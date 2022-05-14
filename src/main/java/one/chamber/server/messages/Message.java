package one.chamber.server.messages;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public abstract class Message implements Serializable {

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
