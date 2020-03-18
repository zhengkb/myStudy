package netty.inorouthandler.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * decode 会根据接收的数据，被调用多次，直到确定没有新的元素被添加到list
     * 或者是bytebuff没有更多的可读字节为止
     * 如果list out不为空，就会将list的内容传递给下一个channelinboundhandler处理，改处理器方法也会被调用多次
     * <p>
     * ctx 上下文对象
     * in 入站的ByteBuf
     * out List集合，将解码后的数据传给下一个handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyByteToLongDecoder 被调用");
        //因为long'8个字节
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
