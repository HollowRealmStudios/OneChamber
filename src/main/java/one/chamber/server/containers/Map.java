package one.chamber.server.containers;

import lombok.Getter;
import lombok.Synchronized;
import lombok.val;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.UUID;

public class Map {

    @Getter
    private final int mapWidth;
    @Getter
    private final int mapHeight;
    private final LinkedList<Bullet> bullets = new LinkedList<>();
    private final LinkedList<Player> players = new LinkedList<>();
    private final LinkedList<Shape> obstacles = new LinkedList<>();

    public Map(int mapWidth, int mapHeight) {
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    @Synchronized
    public void addPlayer(Player player) {
        players.add(player);
    }

    @Synchronized
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    @Synchronized
    public void addObstacle(Shape obstacle) {
        obstacles.add(obstacle);
    }

    @Synchronized
    public LinkedList<Bullet> getBullets() {
        return bullets;
    }

    @Synchronized
    public LinkedList<Player> getPlayers() {
        return players;
    }

    @Synchronized
    public LinkedList<Shape> getObstacles() {
        return obstacles;
    }

    public void tickBullets() {
        bullets.removeIf(bullet -> {
            bullet.setFlyDuration(bullet.getFlyDuration() + 1);
            if (bullet.getFlyDuration() >= Bullet.MAX_FLY_DURATION) {
                bullet.getHitPlayers()
                        .stream()
                        .map(this::getPlayerByUUID)
                        .forEach(Player::kill);
                return true;
            }
            bullet.setCurrentPosition(new Point2D.Float(
                    ((float) bullet.getCurrentPosition().getX()) + bullet.getXPitch(),
                    ((float) bullet.getCurrentPosition().getY()) + bullet.getYPitch())
            );
            val potentiallyHitPlayer = players.stream().filter(player -> player.getHitbox().contains(bullet.getCurrentPosition())).findAny();
            potentiallyHitPlayer.ifPresent(player -> bullet.getHitPlayers().add(player.getUuid()));
            return obstacles.stream().noneMatch(obstacle -> obstacle.contains(bullet.getCurrentPosition()));
        });
    }

    private Player getPlayerByUUID(UUID uuid) {
        return players.stream().filter(player -> player.getUuid() == uuid).findAny().orElseThrow();
    }
}
