package dev.lysmux.collex.client.network.connection;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class TCPConnection implements Connection {
    private final SocketChannel socket;

    public TCPConnection(String serverHost, int serverPort) throws IOException {
        socket = SocketChannel.open();
        socket.connect(new InetSocketAddress(serverHost, serverPort));
    }

    public byte[] read() throws IOException {
        ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
        socket.read(sizeBuffer);
        sizeBuffer.flip();

        ByteBuffer dataBuffer = ByteBuffer.allocate(sizeBuffer.getInt());
        while (dataBuffer.hasRemaining()) socket.read(dataBuffer);
        dataBuffer.flip();

        return dataBuffer.array();
    }


    public void send(byte[] data) throws IOException {
        ByteBuffer dataBuffer = ByteBuffer.allocate(4 + data.length);
        dataBuffer.putInt(data.length);
        dataBuffer.put(data);
        dataBuffer.flip();

        while (dataBuffer.hasRemaining()) socket.write(dataBuffer);
    }

    @Override
    public void close() throws Exception {
        socket.close();
    }
}
