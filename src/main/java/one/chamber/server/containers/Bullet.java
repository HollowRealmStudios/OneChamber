package one.chamber.server.containers;

import lombok.Data;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.UUID;

@Data
public class Bullet implements Serializable {

    public static final int MAX_FLY_DURATION = 32;

    private final Point2D.Float startPosition;
    private final float xPitch;
    private final float yPitch;
    private Point2D.Float currentPosition;
    private UUID shooter;
    private LinkedList<UUID> hitPlayers = new LinkedList<>();
    private int flyDuration = 0;

}
