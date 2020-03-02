package nio.buffer.selector.proj;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;

public class GroupChatServer {

    public static void main(String[] args) {
        GroupChatServer groupChatServer = new GroupChatServer();
        groupChatServer.listen();
    }

    private Selector selector;
    private ServerSocketChannel listenerChannel;
    private static final int port = 8899;

    //初始化
    public GroupChatServer() {
        try {
            selector = Selector.open();
            listenerChannel = ServerSocketChannel.open();
            listenerChannel.socket().bind(new InetSocketAddress(port));
            listenerChannel.configureBlocking(false);
            listenerChannel.register(selector, SelectionKey.OP_ACCEPT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void listen() {
        try {
            while (true) {
                int select = selector.select(1000);
                if (select < 0) {
                    System.out.println("无连接事件");
                }

                Set<SelectionKey> selectionKeys = selector.selectedKeys();
                Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
                while (keyIterator.hasNext()) {
                    SelectionKey selectionKey = keyIterator.next();

                    if (selectionKey.isAcceptable()) {
                        SocketChannel socketChannel = listenerChannel.accept();
                        socketChannel.configureBlocking(false);
                        System.out.println(socketChannel.getRemoteAddress() + "上线了");
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    }

                    if (selectionKey.isValid() && selectionKey.isReadable()) {
                        chat(selectionKey);
                    }

                    keyIterator.remove();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void chat(SelectionKey selectionKey) {
        SocketChannel socketChannel = null;
        try {
            socketChannel = (SocketChannel) selectionKey.channel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
            int count = socketChannel.read(byteBuffer);
            if (count > 0) {
                String string = new String(byteBuffer.array());
                System.out.println(socketChannel.getRemoteAddress() + "说：" + string);
                broadCast(string, socketChannel);
            }
        } catch (Exception e) {
            try {
                System.out.println(socketChannel.getRemoteAddress() + "离线了");
                selectionKey.cancel();
                socketChannel.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private void broadCast(String msg, SocketChannel socketChannel) throws IOException {
        System.out.println("服务器消息转发中。。。。。");
        Set<SelectionKey> selectionKeys = selector.keys();
        for (SelectionKey selectionKey : selectionKeys) {
            SelectableChannel channel = selectionKey.channel();
            if (channel instanceof SocketChannel && socketChannel != channel) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(msg.getBytes());
                ((SocketChannel) channel).write(byteBuffer);
            }
        }
    }

}
