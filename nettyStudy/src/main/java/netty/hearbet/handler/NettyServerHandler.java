package netty.hearbet.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

public class NettyServerHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            switch (idleStateEvent.state()) {
                case READER_IDLE:
                    System.out.println("这是一个读事件");
                    break;
                case WRITER_IDLE:
                    System.out.println("这是一个写事件");
                    break;
                case ALL_IDLE:
                    System.out.println("这是一个读写事件");
                    break;
            }
        }
    }
}
