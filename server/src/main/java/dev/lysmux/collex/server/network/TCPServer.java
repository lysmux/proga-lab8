package dev.lysmux.collex.server.network;

import dev.lysmux.collex.dto.request.Request;
import dev.lysmux.collex.dto.response.Response;
import org.apache.commons.lang3.SerializationException;
import org.apache.commons.lang3.SerializationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;


public class TCPServer implements Runnable {
    private static final Logger log = LoggerFactory.getLogger(TCPServer.class);

    private final AtomicBoolean isRunning = new AtomicBoolean(false);
    private final int listenPort;

    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    private final Function<Request, Response> requestHandler;


    public TCPServer(int listenPort, Function<Request, Response> requestHandler) {
        this.listenPort = listenPort;
        this.requestHandler = requestHandler;
    }

    @Override
    public void run() {
        isRunning.set(true);

        try {
            init();

            log.atInfo()
                    .setMessage("Starting TCP Server")
                    .addKeyValue("port", listenPort)
                    .log();
        } catch (IOException e) {
            log.atError()
                    .setMessage("Failed to start TCP Server")
                    .setCause(e)
                    .log();
        }

        try {
            while (isRunning.get()) processSelector();
        } catch (IOException e) {
            log.atError()
                    .setMessage("Error in server main loop")
                    .setCause(e)
                    .log();
        } finally {
            cleanup();
        }
    }

    private void init() throws IOException {
        selector = Selector.open();

        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(listenPort));
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void cleanup() {
        try {
            if (selector != null) selector.close();
            if (serverSocketChannel != null) serverSocketChannel.close();
        } catch (IOException e) {
            log.atWarn()
                    .setMessage("Failed to cleanup resources")
                    .setCause(e)
                    .log();
        }
        log.info("Server stopped");
    }

    private void processSelector() throws IOException {
        selector.select();
        Iterator<SelectionKey> iter = selector.selectedKeys().iterator();
        while (iter.hasNext()) {
            SelectionKey key = iter.next();
            iter.remove();

            if (key.isValid()) processSelectorKey(key);
        }
    }

    private void processSelectorKey(SelectionKey key) {
        try {
            if (key.isAcceptable()) acceptClient();
            else if (key.isReadable()) handleRead(key);
        } catch (IOException e) {
            disconnectClient((SocketChannel) key.channel());
        }
    }

    private void acceptClient() throws IOException {
        SocketChannel client = serverSocketChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);

        log.atInfo()
                .setMessage("Client connected")
                .addKeyValue("client", client)
                .log();
    }

    private void handleRead(SelectionKey key) throws IOException {
        SocketChannel client = (SocketChannel) key.channel();
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        client.read(sizeBuffer);
        sizeBuffer.flip();

        if (sizeBuffer.remaining() < 4) {
            log.atWarn()
                    .setMessage("Incorrect data received")
                    .addKeyValue("client", client)
                    .log();
            disconnectClient(client);
            return;
        }

        ByteBuffer dataBuffer = ByteBuffer.allocate(sizeBuffer.getInt());
        while (dataBuffer.hasRemaining()) {
            client.read(dataBuffer);
        }
        dataBuffer.flip();

        log.atInfo()
                .setMessage("New request from client")
                .addKeyValue("client", client)
                .addKeyValue("size", dataBuffer.capacity())
                .log();

        Request request;
        try {
            request = SerializationUtils.deserialize(dataBuffer.array());
        } catch (SerializationException e) {
            log.atInfo()
                    .setMessage("Failed to deserialize request")
                    .addKeyValue("client", client)
                    .log();
            disconnectClient(client);
            return;
        }

        Response<?> response = requestHandler.apply(request);
        try {
            sendResponse(client, SerializationUtils.serialize(response));
        } catch (SerializationException e) {
            log.atInfo()
                    .setMessage("Failed to serialize response")
                    .addKeyValue("client", client)
                    .log();
        }
        disconnectClient(client);
    }

    private void sendResponse(SocketChannel client, byte[] response) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(4 + response.length);
        buffer.putInt(response.length);
        buffer.put(response);
        buffer.flip();

        while (buffer.hasRemaining()) {
            client.write(buffer);
        }

        log.atInfo()
                .setMessage("Data sent to client")
                .addKeyValue("client", client)
                .addKeyValue("size", buffer.capacity())
                .log();
    }

    private void disconnectClient(SocketChannel client) {
        try {
            client.close();
            log.atInfo()
                    .setMessage("Client disconnected")
                    .addKeyValue("client", client)
                    .log();
        } catch (IOException e) {
            log.atInfo()
                    .setMessage("Error in disconnect client")
                    .addKeyValue("client", client)
                    .setCause(e)
                    .log();

        }
    }

    public void stop() {
        isRunning.set(false);
        if (selector != null) {
            selector.wakeup();
        }
    }
}
