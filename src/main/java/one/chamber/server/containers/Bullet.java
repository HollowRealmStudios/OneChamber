package one.chamber.server.containers;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.LinkedList;

@Getter
@RequiredArgsConstructor
public class Bullet implements Serializable {

	public static final int MAX_FLY_DURATION = 32;

	private final Point2D.Float startPosition;
	private final float xPitch;
	private final float yPitch;
	private final Player shooter;
	private final LinkedList<Player> hitPlayers = new LinkedList<>();
	@Setter
	private Point2D.Float currentPosition;
	@Setter
	private int flyDuration = 0;

}
