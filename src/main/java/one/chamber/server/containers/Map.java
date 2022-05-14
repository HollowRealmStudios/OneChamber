package one.chamber.server.containers;

import lombok.Getter;
import lombok.Synchronized;
import lombok.val;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class Map implements Serializable {

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

	public static Player findPlayerByName(@NotNull Map map, String name) {
		return map.getPlayers().stream().filter(player -> player.getName().equals(name)).findFirst().orElseThrow();
	}

	public static List<Player> findPlayersInRange(@NotNull Map map, Rectangle2D searchBox) {
		return map.getPlayers().stream().filter(player -> searchBox.contains(player.getCurrentPosition())).toList();
	}

	public static List<Shape> findObstaclesInRange(@NotNull Map map, Rectangle2D searchBox) {
		return map.getObstacles().stream().filter(obstacle -> searchBox.contains(searchBox) || searchBox.intersects(searchBox)).toList();
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
	public List<Bullet> getBullets() {
		return bullets;
	}

	@Synchronized
	public List<Player> getPlayers() {
		return players;
	}

	@Synchronized
	public List<Shape> getObstacles() {
		return obstacles;
	}

	public void tickBullets() {
		bullets.removeIf(bullet -> {
			bullet.setFlyDuration(bullet.getFlyDuration() + 1);
			if (bullet.getFlyDuration() >= Bullet.MAX_FLY_DURATION) {
				bullet.getHitPlayers().forEach(Player::kill);
				return true;
			}
			bullet.setCurrentPosition(new Point2D.Float(
					((float) bullet.getCurrentPosition().getX()) + bullet.getXPitch(),
					((float) bullet.getCurrentPosition().getY()) + bullet.getYPitch())
			);
			val potentiallyHitPlayer = players.stream().filter(player -> player.getHitbox().contains(bullet.getCurrentPosition())).findAny();
			potentiallyHitPlayer.ifPresent(player -> bullet.getHitPlayers().add(player));
			return obstacles.stream().noneMatch(obstacle -> obstacle.contains(bullet.getCurrentPosition()));
		});
	}
}
