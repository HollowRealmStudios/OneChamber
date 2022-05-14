package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class PlayerJoinMessage extends Message implements Serializable {

    private final String name;

}
