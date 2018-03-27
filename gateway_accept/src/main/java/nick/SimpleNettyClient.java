package nick;

import com.alibaba.fastjson.JSON;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import javax.servlet.http.HttpServletResponse;

/**
 * create by sunkx on 2018/3/27
 */

public class SimpleNettyClient {
    public void connect(String host, int port, final String msg, final HttpServletResponse response)throws Exception{
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group); // (2)
            b.channel(NioSocketChannel.class);// (3)
            b.option(ChannelOption.TCP_NODELAY, true); // (4)

            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleClientHandler(msg,response));
                }
            });

            ChannelFuture f = b.connect(host,port).await(); //异步连接

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully();
        }
    }



}
