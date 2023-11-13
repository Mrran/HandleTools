package io.emqx.mqtt;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpBroadcast {
    public static void main(String[] args) {
        // UDP广播的端口号
        int port = 49527;
        // 要发送的数据
        String message = "Hello, UDP Broadcast!";
        // 创建数据包
        byte[] buffer = message.getBytes();

        try {
            // 创建DatagramSocket实例
            DatagramSocket socket = new DatagramSocket();
            // 设置广播
            socket.setBroadcast(true);

            // 创建发送的数据包，指定消息内容、长度、地址和端口
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("192.168.1.255"), port);

            // 发送数据包
            while (true){
                socket.send(packet);
                System.out.println("Broadcast message sent to 192.168.1.255:" + port);
                Thread.sleep(2000);
            }


            // 关闭socket
            //socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
