package netty.inorouthandler.coder;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ReplayingDecoder;

import java.util.List;

public class MyBtyeToLongDecoder2 extends ReplayingDecoder<Void> {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        System.out.println("MyBtyeToLongDecoder2 被调用");
        //在replayingdecoder不需要判断数据是否足够读取，内部会进行处理判断
        out.add(in.readLong());
    }
}
