package nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NioServer {
    public static void main(String[] args) throws IOException {
        //注册selector
        Selector selector = Selector.open();
        //获取连接通道
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

        //绑定端口
        serverSocketChannel.socket().bind(new InetSocketAddress(8888));
        //设置为非阻塞
        serverSocketChannel.configureBlocking(false);

        //将通道注册到selector中
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

        while (true) {
            //阻塞一秒查看是否有新的连接
            if (selector.select(1000) == 0) {
                continue;
            }

            //有新的事件：区分事件和连接
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
            while (keyIterator.hasNext()) {
                SelectionKey selectionKey = keyIterator.next();

                //查看当前事件是否是连接事件
                if (selectionKey.isAcceptable()) {
                    //接取连接事件
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    System.out.println("有新的连接" + socketChannel.hashCode());
                    //设置为非阻塞
                    socketChannel.configureBlocking(false);
                    //注册事件，设置buffer
                    socketChannel.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(512));
                }

                //判断是否是读取事件
                if (selectionKey.isReadable()) {
                    try {
                        SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                        ByteBuffer byteBuffer = (ByteBuffer) selectionKey.attachment();
                        int read = socketChannel.read(byteBuffer);
                        if (read > 0) {
                            System.out.println("输出：" + new String(byteBuffer.array()));
                        }
                    } catch (Exception e) {
                        System.out.println("连接中断");
                    }
                }

                //迭代器移除事件避免重复执行
                keyIterator.remove();
            }

        }
    }
}



















