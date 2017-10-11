/*
 * Copyright 2012 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package net.gaddie;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;

/**
 * Handler implementation for the echo client.  It initiates the ping-pong
 * traffic between the echo client and server by sending the first message to
 * the server.
 */
public class EchoClientHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println("channel active");
        Thread.currentThread().getId();
        ByteBuffer byteBuffer = ByteBuffer.allocate(10);
        byteBuffer.put("hello\n".getBytes());
        byteBuffer.flip();
        ByteBuf msg = Unpooled.wrappedBuffer(byteBuffer);
        System.out.println(msg.readableBytes());
        ctx.writeAndFlush(msg);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(msg);
    }

    public static String bufferToString(final ByteBuffer byteBuffer) {
        final int position = byteBuffer.position();
        final byte[] tmp = new byte[byteBuffer.remaining()];
        byteBuffer.get(tmp);
        byteBuffer.position(position);
        try {
            return new String(tmp, "US-ASCII");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        byteBuffer.position(3);
        System.out.println(byteBuffer);
        ByteBuf buf = Unpooled.buffer();
        buf.writeBytes("Hello how are you!".getBytes());
        System.out.println(buf.readableBytes());

        byteBuffer.limit(byteBuffer.position() + buf.readableBytes());
        System.out.println("state before read");
        System.out.println(byteBuffer);

        buf.readBytes(byteBuffer);
        System.out.println("state after read");
        System.out.println(byteBuffer);

        flipAndReadBuffer(byteBuffer);
    }

    private static void flipAndReadBuffer(ByteBuffer byteBuffer) {
        byteBuffer.flip();
        System.out.println("--------------");
        System.out.println(byteBuffer);
        byte[] dst = new byte[100];
        int remaining = byteBuffer.remaining();
        byteBuffer.get(dst, 0, remaining);
        System.out.println(new String(dst, 0, remaining));
        System.out.println("--------------");
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
       ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
