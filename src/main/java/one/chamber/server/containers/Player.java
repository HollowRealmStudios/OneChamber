package one.chamber.server.containers;

import lombok.Getter;
import lombok.Setter;
import org.java_websocket.WebSocket;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.net.Socket;
import java.util.UUID;

public class Player {

    @Getter
    private final WebSocket socket;
    @Getter
    private final UUID uuid = UUID.randomUUID();
    @Getter
    private String name;
    @Getter
    private Point2D startPosition;
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

    public Player(WebSocket socket) {
        this.socket = socket;
    }

    public void kill() {
        this.isAlive = false;
    }
}
