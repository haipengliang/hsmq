package com.hsms.mqserver.reactor;

import com.hsmq.data.Message;
import com.hsmq.decode.LengthObjectDecode;
import com.hsmq.decode.ObjectDecode;
import com.hsmq.encode.LengthObjectEncode;
import com.hsmq.encode.ObjectEncode;
import com.hsms.mqserver.handle.ServerInHandel;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author ：河神
 * @date ：Created in 2021/6/4 3:53 下午
 */
public class ObjectReactor {

    private int port;
    public ObjectReactor(int port ) {
        this.port = port;
    }

    public void start() throws InterruptedException {

        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        serverBootstrap.group(boss,work);

        serverBootstrap.channel(NioServerSocketChannel.class);

        serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast(new LengthObjectDecode());
                pipeline.addLast(new LengthObjectEncode());
                pipeline.addLast(new ServerInHandel());
            }
        });

        serverBootstrap.option(ChannelOption.SO_BACKLOG,128);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE,true);

        ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
        channelFuture.channel().closeFuture().sync();
    }
}
