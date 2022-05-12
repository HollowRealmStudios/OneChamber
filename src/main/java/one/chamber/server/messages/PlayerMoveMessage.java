package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.geom.Point2D;

@AllArgsConstructor
@Getter
public class PlayerMoveMessage implements Message {

    private Point2D.Float newPosition;

}
