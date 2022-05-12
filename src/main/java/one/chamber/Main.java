package one.chamber;

import lombok.SneakyThrows;
import lombok.val;
import one.chamber.server.io.GameServer;
import one.chamber.server.util.RandomMapGenerator;

public class Main {
    @SneakyThrows
    public static void main(String[] args) {
        val server = new GameServer(6969, RandomMapGenerator.generateRandomMap(10, 100));
        server.start();
    }
}
