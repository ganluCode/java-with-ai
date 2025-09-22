package cn.geekslife.rpc.transport;

import cn.geekslife.rpc.common.URL;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

public class NettyClient implements Client {
    
    private final Bootstrap bootstrap;
    private final EventLoopGroup group;
    private volatile Channel channel;
    
    public NettyClient(URL url, ChannelHandler handler) {
        this.bootstrap = new Bootstrap();
        this.group = new NioEventLoopGroup();
        
        bootstrap.group(group)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline()
                        .addLast(new ObjectDecoder(ClassResolvers.cacheDisabled(null)))
                        .addLast(new ObjectEncoder())
                        .addLast(new NettyClientHandler(handler));
                }
            });
        
        // 连接服务器
        ChannelFuture future = bootstrap.connect(url.getHost(), url.getPort());
        this.channel = future.channel();
    }
    
    @Override
    public void send(Object message) {
        if (channel != null && channel.isActive()) {
            channel.writeAndFlush(message);
        }
    }
    
    @Override
    public void close() {
        if (channel != null) {
            channel.close();
        }
        if (group != null) {
            group.shutdownGracefully();
        }
    }
}