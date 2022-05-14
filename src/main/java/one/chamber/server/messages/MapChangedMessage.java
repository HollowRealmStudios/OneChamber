package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;
import one.chamber.server.containers.Map;

@Getter
@AllArgsConstructor
public class MapChangedMessage extends Message {

	private final Map map;

}
