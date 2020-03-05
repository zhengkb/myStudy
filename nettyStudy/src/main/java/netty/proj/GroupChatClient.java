package netty.proj;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import netty.proj.handler.GroupChatClientHandler;

import java.util.Scanner;

public class GroupChatClient {
    public static void main(String[] args) throws InterruptedException {
        GroupChatClient groupChatClient = new GroupChatClient("localhost", 8848);
        groupChatClient.run();
    }

    private final String host;
    private final int port;
    private NioEventLoopGroup eventLoopGroup;

    public GroupChatClient(String host, int port) {
        this.host = host;
        this.port = port;
        eventLoopGroup = new NioEventLoopGroup();
    }

    public void run() throws InterruptedException {
        Bootstrap bootstrap = new Bootstrap();
        try {
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast("decoder", new StringDecoder())
                                    .addLast("encoder", new StringEncoder())
                                    .addLast(new GroupChatClientHandler());
                        }
                    });
            ChannelFuture channelFuture = bootstrap.connect(host, port).sync();
            Scanner scanner = new Scanner(System.in);
            while (scanner.hasNext()) {
                String nextLine = scanner.nextLine();
                channelFuture.channel().writeAndFlush(nextLine);
            }
            channelFuture.channel().closeFuture().sync();
        } finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
