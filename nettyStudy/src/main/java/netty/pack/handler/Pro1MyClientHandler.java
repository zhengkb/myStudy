package netty.pack.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class Pro1MyClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
    private int count;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg) throws Exception {
        byte[] buffer = new byte[msg.readableBytes()];
        msg.readBytes(buffer);
        String message = new String(buffer, CharsetUtil.UTF_8);
        System.out.println("客户端接收数据:" + message);
        System.out.println("接收消息次数:" + (++count));
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //使用客户端发送十条数据
        for (int i = 0; i < 10; i++) {
            ByteBuf byteBuf = Unpooled.copiedBuffer("hello world" + i, CharsetUtil.UTF_8);
            ctx.writeAndFlush(byteBuf);
        }
    }
}
