package nick;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * create by sunkx on 2018/3/27
 */
public class SimpleNettyServer {
    private int port;
    public SimpleNettyServer(int port) {
        this.port = port;
    }
    public void run() throws Exception {
        //NioEventLoopGroup是个Reactor线程组，专门处理网络事件，一个用于接收连接，一个用于进行socketChannel读写，
        EventLoopGroup bossGroup = new NioEventLoopGroup(); // (1)
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //ServerBootstrap是nettyNIO服务的启动类
            ServerBootstrap b = new ServerBootstrap(); // (2)
            b.group(bossGroup, workerGroup) //group方法将两个线程组放入到ServerBootstrap中
                    .channel(NioServerSocketChannel.class) // (3)
                    .childHandler(new ChannelInitializer<SocketChannel>() { // (4) //绑定handler处理类
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new SimpleNettyHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 1024)// (5) //队列大小，默认为100
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6)
            ChannelFuture f = b.bind(port).sync(); // (7)类似于Future，用于异步操作的通知回调。
            f.channel().closeFuture().sync();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
    public static void main(String[] args) throws Exception {
        new SimpleNettyServer(8001).run();
    }

}
