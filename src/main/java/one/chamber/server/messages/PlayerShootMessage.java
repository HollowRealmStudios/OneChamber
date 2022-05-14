package one.chamber.server.messages;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.awt.geom.Point2D;
import java.io.Serializable;

@Getter
@AllArgsConstructor
public class PlayerShootMessage extends Message implements Serializable {

	private final Point2D.Float startingPoint;
	private final float xPitch;
	private final float yPitch;

}
