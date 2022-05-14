package one.chamber.mock.client;

import lombok.val;
import one.chamber.server.containers.Map;
import one.chamber.server.io.ByteTranslator;
import one.chamber.server.messages.MapChangedMessage;
import one.chamber.server.messages.PlayerJoinMessage;
import one.chamber.server.messages.PlayerMoveMessage;
import one.chamber.server.messages.PlayerShootMessage;
import org.apache.commons.lang3.RandomStringUtils;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;

public class MockUI extends JFrame {

	//Probably shouldn't be here...
	private static final int MOVE_SPEED = 5;
	private final MockClient client;
	//Probably shouldn't be here...
	private String localPlayerName;
	private @Nullable Map currentMap;

	public MockUI(MockClient client) {
		this.client = client;
		setSize(800, 600);
		setUndecorated(true);
		setLocationRelativeTo(null);
		setVisible(true);
		addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (currentMap == null) return;
				val localPlayer = Map.findPlayerByName(currentMap, localPlayerName);
				switch (e.getKeyCode()) {
					case KeyEvent.VK_W -> client.send(ByteTranslator.toByteArray(new PlayerMoveMessage(new Point2D.Float((float) localPlayer.getCurrentPosition().getX(), (float) (localPlayer.getCurrentPosition().getY() - MOVE_SPEED)))));
					case KeyEvent.VK_S -> client.send(ByteTranslator.toByteArray(new PlayerMoveMessage(new Point2D.Float((float) localPlayer.getCurrentPosition().getX(), (float) (localPlayer.getCurrentPosition().getY() + MOVE_SPEED)))));
					case KeyEvent.VK_A -> client.send(ByteTranslator.toByteArray(new PlayerMoveMessage(new Point2D.Float((float) localPlayer.getCurrentPosition().getX() - MOVE_SPEED, (float) (localPlayer.getCurrentPosition().getY())))));
					case KeyEvent.VK_D -> client.send(ByteTranslator.toByteArray(new PlayerMoveMessage(new Point2D.Float((float) localPlayer.getCurrentPosition().getX() + MOVE_SPEED, (float) (localPlayer.getCurrentPosition().getY())))));
				}
			}
		});
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (currentMap == null) return;
				val mouseVec = new Vector2f(e.getX(), getY());
				val centerVec = new Vector2f(getWidth(), getHeight());
				val normalizedMouseVec = mouseVec.sub(centerVec).normalize();
				client.send(ByteTranslator.toByteArray(new PlayerShootMessage(Map.findPlayerByName(currentMap, localPlayerName).getCurrentPosition(), normalizedMouseVec.x, normalizedMouseVec.y)));
			}
		});
		client.addListener((message) -> {
			if (message instanceof MapChangedMessage mapChangedMessage) {
				currentMap = mapChangedMessage.getMap();
				repaint();
			} else if (message instanceof PlayerJoinMessage playerJoinMessage) {
				//TODO check if the joined player is the local player -> no message
				JOptionPane.showMessageDialog(null, String.format("Player %s joined", playerJoinMessage.getName()));
			}
		});
		localPlayerName = JOptionPane.showInputDialog(null, "Username?", RandomStringUtils.randomAlphanumeric(8));
		client.send(ByteTranslator.toByteArray(new PlayerJoinMessage(localPlayerName)));
	}

	@Override
	public void paint(Graphics g) {
		val g2d = ((Graphics2D) g);
		g2d.clearRect(0, 0, getWidth(), getHeight());
		if (currentMap == null) return;
		g2d.setColor(Color.GREEN.darker().darker().darker());
		g2d.fillRect(0, 0, getWidth(), getHeight());
		val localPlayer = Map.findPlayerByName(currentMap, localPlayerName);
		g2d.translate(-localPlayer.getCurrentPosition().getX(), -localPlayer.getCurrentPosition().getY());
		g2d.setColor(Color.RED);
		currentMap.getObstacles().forEach(g2d::fill);
		g2d.setColor(Color.BLUE);
		currentMap.getPlayers().forEach(player -> g2d.fill(player.getHitbox()));
		g2d.setColor(Color.DARK_GRAY.darker().darker());
		currentMap.getBullets().forEach(bullet -> g2d.fillOval((int) (bullet.getCurrentPosition().getX() - 2), (int) (bullet.getCurrentPosition().getY() - 2), 4, 4));
	}
}
