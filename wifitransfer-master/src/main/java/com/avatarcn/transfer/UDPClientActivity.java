package com.avatarcn.transfer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
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

public class UDPClientActivity extends AppCompatActivity {
    TextView tvReceive;
    EditText etSend;
    DatagramChannel channel;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_udp_client);
        tvReceive = (TextView) findViewById(R.id.tv_receive);
        etSend = (EditText) findViewById(R.id.et_sent_content);
        try {
            channel = DatagramChannel.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                ByteBuffer buffer = ByteBuffer.allocate(1024);
                byte b[];
                while (true) {
                    try {
                        buffer.clear();
                        final SocketAddress socketAddress = channel.receive(buffer);
                        if (socketAddress != null) {
                            int position = buffer.position();
                            b = new byte[position];
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
                                    tvReceive.append("服务器地址：" + socketAddress.toString() + "\n");
                                    tvReceive.append("回复的消息内容:" + msg + "\n");
                                }
                            });

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    public void sendMsg(View v) {
        String msg = etSend.getText().toString();
        if (TextUtils.isEmpty(msg)) return;
        final ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.clear();
        buffer.put(msg.getBytes());
        buffer.flip();
        System.out.println("send msg:" + msg);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    channel.send(buffer, new InetSocketAddress("192.168.3.102", 2222));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}