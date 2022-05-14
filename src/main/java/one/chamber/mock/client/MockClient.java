package one.chamber.mock.client;

import lombok.extern.java.Log;
import lombok.val;
import one.chamber.server.io.ByteTranslator;
import one.chamber.server.util.ParameterizedRunnable;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.LinkedList;

@Log
public class MockClient extends WebSocketClient {

	private final LinkedList<ParameterizedRunnable<Object>> listeners = new LinkedList<>();

	public MockClient(URI serverUri) {
		super(serverUri);
	}

	@Override
	public void onOpen(ServerHandshake handshakedata) {
		// TODO document why this method is empty
	}

	@Override
	public void onMessage(String message) {
		log.warning(String.format("Unexpected plaintext message %s", message));
	}

	@Override
	public void onMessage(ByteBuffer message) {
		val clazz = ByteTranslator.getObjectClass(message.array());
		log.info(String.format("Event of type %s was received", clazz.getCanonicalName()));
		listeners.forEach(listener -> listener.run(ByteTranslator.toObject(message.array(), Object.class)));
	}

	public void addListener(ParameterizedRunnable<Object> listener) {
		listeners.add(listener);
	}

	@Override
	public void onClose(int code, String reason, boolean remote) {
		log.severe(String.format("Connection to host was closed unexpectetly. Exit code: %d, Remote close: %b, Reason %s", code, remote, reason));
	}

	@Override
	public void onError(Exception ex) {
		log.severe(String.format("Error occurred: %s", ex.getMessage()));
	}
}
