package one.chamber.server.containers;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.io.Serializable;

public class Player implements Serializable {

	@Getter
	private final transient WebSocket socket;
	@Setter
	@Getter
	private String name;
	@Getter
	@Setter
	private Point2D.Float startPosition;
	@Getter
	@Setter
	private Point2D.Float currentPosition;
	@Getter
	@Setter
	private int score = 0;
	@Getter
	@Setter
	private boolean hasBullet = true;
	@Getter
	private boolean isAlive = true;
	@Getter
	@Setter
	private Ellipse2D.Float hitbox;
	@Getter
	@Setter
	private Player killer;

	public Player(WebSocket socket) {
		this.socket = socket;
	}

	public void kill() {
		this.isAlive = false;
	}
}
