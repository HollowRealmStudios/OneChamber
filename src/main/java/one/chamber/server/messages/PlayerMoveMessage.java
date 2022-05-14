package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.geom.Point2D;
import java.io.Serializable;

@AllArgsConstructor
@Getter
public class PlayerMoveMessage extends Message implements Serializable {

    private final Point2D.Float newPosition;

}
