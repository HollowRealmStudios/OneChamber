package one.chamber;

import lombok.SneakyThrows;
import lombok.val;
import one.chamber.mock.client.MockClient;
import one.chamber.mock.client.MockUI;
import one.chamber.server.io.GameServer;
import one.chamber.server.util.RandomMapGenerator;

import java.net.URI;

public class Main {

	public static GameServer server;
	public static MockClient client;

	@SneakyThrows
	public static void main(String[] args) {
        /*if (args.length == 0) throw new IllegalArgumentException("Required 1 or 2 argument");
        switch (args[0].toLowerCase()) {
            case "--server" -> {
                if(args.length != 1) throw new IllegalArgumentException("Required only 1 argument");
                server = new GameServer(6969, RandomMapGenerator.generateRandomMap(10, 100));
                server.start();
            }
            case "--client" -> {
                if (args.length != 2) throw new IllegalArgumentException("Required server URI");
                client = new MockClient(URI.create(args[1]));
                client.connect();
            }
            default -> throw new IllegalArgumentException("Argument has to either be --server or --client");
        }*/
		server = new GameServer(6969, RandomMapGenerator.generateRandomMap(10, 100));
		server.start();
		client = new MockClient(URI.create("ws://127.0.0.1:6969"));
		client.connectBlocking();
		new MockUI(client);
	}
}
