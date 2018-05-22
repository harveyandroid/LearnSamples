package com.avatarcn.transfer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.Date;

/**
 * Created by hanhui on 2018/4/8 0008 11:52
 */

public class UDPServiceActivity extends AppCompatActivity {
    TextView tvReceive;
    DatagramChannel datagramChannel = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_udp_service);
        tvReceive = (TextView) findViewById(R.id.tv_receive);
        try {
            datagramChannel = DatagramChannel.open();
            datagramChannel.socket().bind(new InetSocketAddress(2222));
            new ReadThread().start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void sendReply(SocketAddress socketAddress, String msg) throws IOException {
        String message = "I has receive your message:" + msg;
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(message.getBytes("UTF-8"));
        buffer.flip();
        datagramChannel.send(buffer, socketAddress);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class ReadThread extends Thread {
        final ByteBuffer buffer = ByteBuffer.allocate(1024);

        @Override
        public void run() {
            while (true) {
                try {
                    buffer.clear();
                    final SocketAddress socketAddress = datagramChannel.receive(buffer);
                    if (socketAddress != null) {
                        int position = buffer.position();
                        byte[] b = new byte[position];
                        buffer.flip();
                        for (int i = 0; i < position; ++i) {
                            b[i] = buffer.get();
                        }
                        final String msg = new String(b, "UTF-8");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvReceive.append("\n");
                                tvReceive.append("时间：" + new Date() + "\n");
                                tvReceive.append("客户端地址：" + socketAddress.toString() + "\n");
                                tvReceive.append("发送的消息内容:" + msg + "\n");
                            }
                        });
                        System.out.println("receive remote " + socketAddress.toString() + ":" + msg);
                        sendReply(socketAddress, msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}