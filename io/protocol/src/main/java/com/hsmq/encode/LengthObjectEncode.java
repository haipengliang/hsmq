package com.hsmq.encode;

import com.hsmq.data.Message;
import com.hsmq.protocol.HsEecodeData;
import com.hsmq.protocol.HsMessage;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.ByteBuffer;

/**
 * @author ：河神
 * @date ：Created in 2021/6/6 6:38 下午
 */
public class LengthObjectEncode extends MessageToByteEncoder<HsEecodeData> {


    @Override
    protected void encode(ChannelHandlerContext ctx, HsEecodeData encodeData, ByteBuf out) throws Exception {
        out.writeInt(encodeData.getLength());
        out.writeInt(encodeData.getHeadLength());
        out.writeBytes(encodeData.getHead());
        out.writeInt(encodeData.getDataLength());
        out.writeBytes(encodeData.getData());
    }
}
