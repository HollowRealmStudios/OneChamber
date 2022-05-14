package one.chamber.server.io;

import lombok.extern.java.Log;
import lombok.val;
import one.chamber.server.containers.Bullet;
import one.chamber.server.containers.Map;
import one.chamber.server.containers.Player;
import one.chamber.server.messages.*;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

@Log
public class GameServer extends WebSocketServer {

	private final Map map;

	public GameServer(int port, Map map) {
		super(new InetSocketAddress(port));
		this.map = map;
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			public void run() {
				map.tickBullets();
				map.getPlayers()
						.stream()
						.filter(player -> !player.isAlive())
						.forEach(player -> broadcastPlayerKilled(player.getKiller(), player));
			}
		}, 0L, 8L);
	}

	@Override
	public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
		log.info(String.format("Player with IP %s connected to socket", webSocket.getLocalSocketAddress().getHostString()));
		map.addPlayer((new Player(webSocket)));
	}

	@Override
	public void onClose(WebSocket webSocket, int i, String s, boolean b) {
		log.info(String.format("Player with IP %s disconnected from socket", webSocket.getLocalSocketAddress().getHostString()));
		map.getPlayers().removeIf(player -> player.getSocket().getLocalSocketAddress().getHostString().equals(webSocket.getLocalSocketAddress().getHostString()));
	}

	@Override
	public void onMessage(WebSocket webSocket, String s) {
		log.warning(String.format("Player with IP %s sent unexpected string %s", webSocket.getLocalSocketAddress().getHostString(), s));
	}

	@Override
	public void onMessage(WebSocket webSocket, ByteBuffer message) {
		val clazz = ByteTranslator.getObjectClass(message.array());
		log.info(String.format("Event of type %s was received from %s", clazz.getCanonicalName(), webSocket.getLocalSocketAddress().getHostString()));
		if (clazz.equals(PlayerMoveMessage.class)) {
			val playerMoveMessage = ByteTranslator.toObject(message.array(), PlayerMoveMessage.class);
			log.info(playerMoveMessage.toString());
			getPlayerBySocket(webSocket).setCurrentPosition(playerMoveMessage.getNewPosition());
		} else if (clazz.equals(PlayerShootMessage.class)) {
			val playerShootMessage = ByteTranslator.toObject(message.array(), PlayerShootMessage.class);
			log.info(playerShootMessage.toString());
			val bullet = new Bullet(playerShootMessage.getStartingPoint(), playerShootMessage.getXPitch(), playerShootMessage.getYPitch(), getPlayerBySocket(webSocket));
			bullet.setCurrentPosition(bullet.getStartPosition());
			map.addBullet(bullet);
		} else if (clazz.equals(PlayerJoinMessage.class)) {
			val playerJoinMessage = ByteTranslator.toObject(message.array(), PlayerJoinMessage.class);
			log.info(playerJoinMessage.toString());
			val player = getPlayerBySocket(webSocket);
			player.setName(playerJoinMessage.getName());
			val startingPosition = new Point2D.Float(ThreadLocalRandom.current().nextInt(0, map.getMapWidth()), ThreadLocalRandom.current().nextInt(0, map.getMapHeight()));
			player.setStartPosition(startingPosition);
			player.setCurrentPosition(startingPosition);
			player.setHitbox(new Ellipse2D.Float((float) (player.getCurrentPosition().getX() - 10), (float) (player.getCurrentPosition().getY() - 10), 20, 20));
		}
		broadcast(message);
		broadcast(ByteTranslator.toByteArray(new MapChangedMessage(map)));
	}

	public void broadcastPlayerKilled(Player killer, Player killed) {
		broadcast(ByteTranslator.toByteArray(new PlayerKilledMessage(killer.getName(), killed.getName())));
	}

	@Override
	public void onError(WebSocket webSocket, Exception e) {
		log.severe(String.format("Exception in socket %s: %s", webSocket.toString(), e.getMessage()));
	}

	@Override
	public void onStart() {
		log.info("Starting websocket");
	}

	private Player getPlayerBySocket(WebSocket socket) {
		return map.getPlayers()
				.stream()
				.filter(player -> player.getSocket().getLocalSocketAddress().getHostString().equals(socket.getLocalSocketAddress().getHostString()))
				.findAny()
				.orElseThrow();
	}
}
