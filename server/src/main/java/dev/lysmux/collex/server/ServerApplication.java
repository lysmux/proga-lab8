package dev.lysmux.collex.server;

import dev.lysmux.collex.server.command.registry.CommandRegistry;
import dev.lysmux.collex.server.network.TCPServer;

public class ServerApplication {
    private final TCPServer server;

    public ServerApplication(CommandRegistry commandRegistry, ServerConfig config) {
        Router router = new Router(commandRegistry, config);
        this.server = new TCPServer(config.getPort(), router::handleRequest);
    }

    public void run() {
        new Thread(server).start();
    }

    public void stop() {
        server.stop();
    }
}
