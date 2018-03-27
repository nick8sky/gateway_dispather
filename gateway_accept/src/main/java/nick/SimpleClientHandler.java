package nick;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.ExecutionException;

/**
 * create by sunkx on 2018/3/27
 */
public class SimpleClientHandler  extends ChannelHandlerAdapter {
    private final ByteBuf msg;
    private  HttpServletResponse response;

    public SimpleClientHandler(String info,HttpServletResponse response){
        this.response = response ;
        msg = Unpooled.buffer();
        msg.writeBytes(info.getBytes());
    }
    /**
     * 客户端和服务端连接成功后，会调用channelActive
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx){
        System.out.println("connect succeed !");
        ctx.writeAndFlush(msg);//发送消息
    }
    /**
     * 服务器应答
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws IOException {
        ByteBuf bf= (ByteBuf) msg;
        byte[] req = new byte[bf.readableBytes()];
        bf.readBytes(req);
        String str = new String(req,"utf-8");
        ChannelFuture future = ctx.channel().close();
        /*ChannelFuture future = ctx.channel().close();
        future.addListener(new ChannelFutureListener() {
            public void operationComplete(ChannelFuture future) {
                try {
                    System.out.println(future.get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        });*/

        response.getWriter().write(str);
        response.getWriter().flush();
        System.out.println("client read from server :"+str);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx,Throwable ex){
        ctx.close();
    }
}
