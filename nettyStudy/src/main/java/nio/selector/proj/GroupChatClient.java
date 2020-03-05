package nio.selector.proj;


import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class GroupChatClient {

    public static void main(String[] args) throws IOException {
        GroupChatClient groupChatClient = new GroupChatClient();
        new Thread() {
            public void run() {
                while (true) {
                    groupChatClient.readMessage();
                    try {
                        Thread.currentThread().sleep(3000);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            groupChatClient.sendMessage(line);
        }
    }

    private Selector selector;

    private SocketChannel socketChannel;

    private static final String HOST = "127.0.0.1";

    private static final int PORT = 8899;

    private String userName;

    public GroupChatClient() throws IOException {
        selector = Selector.open();
        socketChannel = socketChannel.open(new InetSocketAddress(HOST, PORT));
        socketChannel.configureBlocking(false);
        socketChannel.register(selector, SelectionKey.OP_READ);
        userName = socketChannel.getRemoteAddress().toString().substring(1);
        System.out.println(userName + "准备完毕");
    }

    public void sendMessage(String msg) {
        msg = userName + "说：" + msg;
        try {
            socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void readMessage() {
        try {
            int readChannel = selector.select(2000);
            if (readChannel < 0) {
                System.out.println("等待消息返回");
            }
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();
                if (selectionKey.isReadable()) {
                    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                    ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                    int read = socketChannel.read(byteBuffer);
                    if (read > 0) {
                        System.out.println(socketChannel.getRemoteAddress() + "说：" + new String(byteBuffer.array()));
                    } else {
                        continue;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("服务器更新维护中。。。。");
        }
    }
}
