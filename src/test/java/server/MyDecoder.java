package server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @version 1.0
 * @Author:qiu
 * @Description 自定义解码器
 * @Date 17:00 2023/2/28
 **/
public class MyDecoder extends ByteToMessageDecoder {


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        if (byteBuf.readableBytes() > 4){
            list.add(byteBuf.readInt());
        }


    }
}
