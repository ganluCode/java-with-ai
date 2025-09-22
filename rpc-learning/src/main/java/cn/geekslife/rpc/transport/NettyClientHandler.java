package cn.geekslife.rpc.transport;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {
    
    private final io.netty.channel.ChannelHandler handler;
    
    public NettyClientHandler(io.netty.channel.ChannelHandler handler) {
        this.handler = handler;
    }
    
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        // 处理接收到的消息
        // 这里应该调用实际的响应处理器
        handler.getClass().getMethod("handle", Object.class).invoke(handler, msg);
    }
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}