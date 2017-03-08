package com.txw.netty;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.net.URL;

/**
 * Created by tangxiewen on 2017/3/8.
 */
public class HttpHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

    protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
        URL url=HttpHandler.class.getProtectionDomain().getCodeSource().getLocation();
        System.out.println(url);
        String uri = msg.getUri();
        System.out.println(uri);

    }
}
