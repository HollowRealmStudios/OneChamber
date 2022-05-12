package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.geom.Point2D;

@Getter
@AllArgsConstructor
public class PlayerShootMessage implements Message {

    Point2D.Float startingPoint;
    float xPitch;
    float yPitch;

}
