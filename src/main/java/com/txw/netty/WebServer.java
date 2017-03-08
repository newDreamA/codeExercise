package com.txw.netty;

import com.txw.nio.TimeServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.ServerSocketChannel;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * Created by tangxiewen on 2017/3/8.
 */
public class WebServer {

    private void  bind(int port)throws Exception {
        //设置主从工作线程
        EventLoopGroup boss = new NioEventLoopGroup();
        EventLoopGroup work = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(boss, work).channel(ServerSocketChannel.class)
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        protected void initChannel(SocketChannel ch) throws Exception {
                            ChannelPipeline pipeline = ch.pipeline();
                            //请求解码
                            pipeline.addLast(new HttpRequestDecoder());
                            pipeline.addLast(new HttpObjectAggregator(64 * 1024));
                            //响应编码
                            pipeline.addLast(new HttpResponseEncoder());
                            pipeline.addLast(new ChunkedWriteHandler());
                            pipeline.addLast(new HttpHandler());
                        }
                    });
            ChannelFuture future = b.bind(port).sync();
            future.channel().closeFuture().sync();
        }finally {
            boss.shutdownGracefully();
            work.shutdownGracefully();
        }
    }
    public static void main(String[] args)throws Exception{
        int port = 8000;
        new TimeServer().bind(port);
    }
}
