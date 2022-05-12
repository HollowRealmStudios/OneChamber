package one.chamber.server;

import lombok.SneakyThrows;
import lombok.Synchronized;
import lombok.val;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

public class SocketManager extends Thread {

    private final ServerSocket serverSocket;
    private final LinkedList<Socket> connectedSockets = new LinkedList<>();
    private boolean running = true;

    @SneakyThrows
    public SocketManager(int port) {
        serverSocket = new ServerSocket(port);
    }

    @SneakyThrows
    @Override
    public void run() {
        while (running) {
            //Split this, so only serverSockets.accept() blocks
            // and the lock is released from the synchronized getConnectedSocket() method
            val socket = serverSocket.accept();
            getConnectedSockets().add(socket);
        }
    }

    public void stopManager() {
        running = false;
    }

    @Synchronized
    public LinkedList<Socket> getConnectedSockets() {
        return connectedSockets;
    }
}
