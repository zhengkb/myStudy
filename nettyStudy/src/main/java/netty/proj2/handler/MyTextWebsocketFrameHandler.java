package netty.proj2.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

import java.time.LocalDate;

public class MyTextWebsocketFrameHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame) throws Exception {
        System.out.println("服务器端收到消息：" + textWebSocketFrame.text());

        channelHandlerContext.channel().writeAndFlush(new TextWebSocketFrame("服务器时间：" + LocalDate.now() + "，信息：" + textWebSocketFrame.text()));
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        //id表示一个唯一的值，存在两种形式：LongText(唯一),ShortText(可能重复)
        System.out.println("handlerAdded 被调用：" + ctx.channel().id().asLongText());
        System.out.println("handlerAdded 被调用：" + ctx.channel().id().asShortText());
        ctx.channel().writeAndFlush("11111");
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("handlerRemoved 被调用：" + ctx.channel().id().asLongText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常发生：" + cause.getMessage());
        ctx.close();
    }
}
