package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class PlayerKilledMessage extends Message implements Serializable {

    private final String killerName;
    private final String killedName;

}
