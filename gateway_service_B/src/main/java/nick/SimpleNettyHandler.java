package nick;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.UnsupportedEncodingException;

/**
 * create by sunkx on 2018/3/27
 */
public class SimpleNettyHandler  extends ChannelHandlerAdapter { // (1)
    //将msg转换为ByteBuffer对象，ByteBuf
    public void channelRead(ChannelHandlerContext ctx,Object msg) throws UnsupportedEncodingException, InterruptedException { // (2)
        ByteBuf bf = (ByteBuf) msg;//ByteBuf是netty中的byteBuffer
        byte[] request = new byte[bf.readableBytes()];
        bf.readBytes(request);
        String info = new String(request,"utf-8");
        System.out.println("received from client :"+info);
        String repMsg = "received ! \n";
        Thread.sleep(2000l);
        bf = Unpooled.copiedBuffer(repMsg.getBytes());
        ctx.write(bf);
        // ctx.flush(); 调用channelReadComplete，这里不flush
    }
    //发生异常时，关闭ctx资源。
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        cause.printStackTrace();
        ctx.close();
    }
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception{
        ctx.flush();
    }
}
