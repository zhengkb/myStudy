package netty.proj2;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import netty.proj2.handler.MyTextWebsocketFrameHandler;

public class Proj2MyServer {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            pipeline.addLast(new HttpServerCodec())//因为基于http协议，所以使用http的编解码器
                                    .addLast(new ChunkedWriteHandler())//是以块方式写，添加ChunkedWriteHandler处理器
                                    /**
                                     * 说明
                                     * 1.http数据在传输过程中是分段的，HttpObjectAggregator，就是可以多段聚合
                                     * 2.这就是为什么当浏览器发送大量数据时会发出多穿http请求
                                     * */
                                    .addLast(new HttpObjectAggregator(8192))
                                    /**
                                     * 说明
                                     * 1.对于websocket，它的数据是以帧(frame)的形式传递的
                                     * 2.可以看到websocketframe下有六个子类
                                     * 3.浏览器发送请求时ws://localhost:7000/hello(表示请求的uri)
                                     * 4.WebSocketServerProtocolHandler核心功能是将http协议升级为ws协议（保持长连接）
                                     * */
                                    .addLast(new WebSocketServerProtocolHandler("/hello"))
                                    .addLast(new MyTextWebsocketFrameHandler())//自定义handler处理业务
                            ;
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(7000).sync();
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
