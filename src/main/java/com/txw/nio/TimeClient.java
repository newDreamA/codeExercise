package com.txw.nio;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

/**
 * Created by tangxiewen on 2017/3/3.
 */
public class TimeClient {
    public void connect(int port,String host)throws Exception{
        //配置客户端NIO线程组
        EventLoopGroup group = new NioEventLoopGroup();
        try{
            Bootstrap b = new Bootstrap();
            b.group(group)
           .channel(NioSocketChannel.class)
            .option(ChannelOption.TCP_NODELAY, true)
           .handler(new ChannelInitializer<SocketChannel>() {

                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
                    ch.pipeline().addLast(new StringDecoder());
                   ch.pipeline().addLast(new TimeClientHandler());
                }
            });
            //发起异步连接操作
            ChannelFuture f = b.connect(host,port).sync();
            //等待客户端链路关闭
            f.channel().closeFuture().sync();
        }finally {
            group.shutdownGracefully();
        }
    }

    public static void main(String[] args)throws Exception{
        int port = 8000;
        new TimeClient().connect(port,"127.0.0.1");
    }
}
