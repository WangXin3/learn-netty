package com.wxx.netty.c2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import lombok.extern.slf4j.Slf4j;

/**
 * @ChannelHandler.Sharable 标志这个ChannelHandler可以被多个Channel安全地共享
 * @author wangxin
 * @date 2021/5/24
 */
@Slf4j
@ChannelHandler.Sharable
public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 处理所有接收到的数据
     * @param ctx /
     * @param msg 数据
     * @throws Exception /
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf in = (ByteBuf) msg;
        log.info("Server received: {}", in.toString(CharsetUtil.UTF_8));
        // 将收到的消息原封不动的返回给发送者，但不刷出
        ctx.write(in);
    }

    /**
     * 处理完成
     * @param ctx /
     * @throws Exception /
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        // 将缓存在缓冲区的数据刷出，并关闭改Channel

        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                .addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 异常处理
     * @param ctx /
     * @param cause /
     * @throws Exception /
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
