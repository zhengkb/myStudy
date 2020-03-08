package netty.hearbet;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

public class NettyHearbet {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();

        try{
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new ChannelInitializer<ServerSocketChannel>() {
                        @Override
                        protected void initChannel(ServerSocketChannel serverSocketChannel) throws Exception {
                            ChannelPipeline pipeline = serverSocketChannel.pipeline();
                            /**
                             * IdleStateHandler:Netty提供的处理空闲状态的处理器
                             * ReaderIdleTime:读空闲时间，就会发送心跳检测包，检测是否还是连接的状态
                             * WriterIdleTime:写空闲时间
                             * AllIdleTime:读写空闲时间
                             * 当IdleStateEvent触发之后就会传给管道的下一个handler去进行处理（通过会调触发下一个handler的userEventTrigger方法）
                             * */
                            pipeline.addLast(new IdleStateHandler(3,5,7, TimeUnit.SECONDS))
                                    .addLast(null);
                        }
                    });
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        }finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
