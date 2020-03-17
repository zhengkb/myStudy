package netty.inorouthandler.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MyByteToLongDecoder extends ByteToMessageDecoder {
    /**
     * ctx 上下文对象
     * in 入站的ByteBuf
     * out List集合，将解码后的数据传给下一个handler
     */
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //因为long'8个字节
        if (in.readableBytes() >= 8) {
            out.add(in.readLong());
        }
    }
}
