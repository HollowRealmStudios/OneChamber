package one.chamber.server.io;

import lombok.val;
import one.chamber.server.containers.Bullet;
import one.chamber.server.containers.Map;
import one.chamber.server.containers.Player;
import one.chamber.server.messages.PlayerMoveMessage;
import one.chamber.server.messages.PlayerShootMessage;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.util.LinkedList;

public class GameServer extends WebSocketServer {

    private final LinkedList<Player> players = new LinkedList<>();
    private final Map map;

    public GameServer(int port, Map map) {
        super(new InetSocketAddress(port));
        this.map = map;
    }

    @Override
    public void onOpen(WebSocket webSocket, ClientHandshake clientHandshake) {
        players.add(new Player(webSocket));
    }

    @Override
    public void onClose(WebSocket webSocket, int i, String s, boolean b) {
        players.removeIf(player -> player.getSocket().getLocalSocketAddress().getHostString().equals(webSocket.getLocalSocketAddress().getHostString()));
    }

    @Override
    public void onMessage(WebSocket webSocket, String s) {
        System.out.println(s);
    }

    @Override
    public void onMessage(WebSocket conn, ByteBuffer message) {
        val clazz = ByteTranslator.getObjectClass(message.array());
        if (clazz.equals(PlayerMoveMessage.class)) {
            val playerMoveMessage = ByteTranslator.toObject(message.array(), PlayerMoveMessage.class);
            getPlayerBySocket(conn).setCurrentPosition(playerMoveMessage.getNewPosition());
        } else if (clazz.equals(PlayerShootMessage.class)) {
            val playerShootMessage = ByteTranslator.toObject(message.array(), PlayerShootMessage.class);
            map.addBullet(new Bullet(playerShootMessage.getStartingPoint(), playerShootMessage.getXPitch(), playerShootMessage.getYPitch()));
        }
    }

    @Override
    public void onError(WebSocket webSocket, Exception e) {
        System.err.println("Error in Websocket " + webSocket.toString());
        System.err.println(e.getMessage());
    }

    @Override
    public void onStart() {
        System.out.println("Starting websocket");
    }

    private Player getPlayerBySocket(WebSocket socket) {
        return players
                .stream().filter(player -> player.getSocket().getLocalSocketAddress().getHostString().equals(socket.getLocalSocketAddress().getHostString()))
                .findAny()
                .orElseThrow();
    }
}
