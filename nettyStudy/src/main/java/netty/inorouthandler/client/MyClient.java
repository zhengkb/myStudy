package netty.inorouthandler.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.inorouthandler.coder.MyLongToByteEncoder;
import netty.inorouthandler.handler.MyClientHandler;

public class MyClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(nioEventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            pipeline.addLast(new MyLongToByteEncoder())//对数据进行编码
                                    .addLast(new MyClientHandler())//加入自定义业务逻辑
                            ;
                        }
                    });

            ChannelFuture channelFuture = bootstrap.connect("localhost", 7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            nioEventLoopGroup.shutdownGracefully();
        }
    }
}
