package io.emqx.mqtt;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

//客户端
public class TcpClientTest {
    public static void main(String[] args) {
        Socket socket = null;
        OutputStream out = null;
        try {
            //1.创建Socket对象，指明服务器IP和端口号
            socket = new Socket("192.168.137.236", 9999);
            //2.获取一个输出流用于输出数据

            out = socket.getOutputStream();
            //3.写出数据的操作(这里也可以用转换流OutputStreamWriter)
            out.write((byte) 0x01);

            File pic = new File("C:\\Users\\ranfeng\\Desktop\\temp\\capture.jpg");
            FileOutputStream os = new FileOutputStream(pic);

            InputStream is = socket.getInputStream();

            //Thread.sleep(100);

            while (true){
                byte[] type = new byte[1];
                int typeCount = is.read(type);
                //System.out.println("count = " + typeCount);

                byte[] lenght = new byte[4];
                int lenCount = is.read(lenght);

                int anInt = toInt(lenght, 0);
                System.out.println("=======================================> type = " + type[0] + ", len = " + anInt);


                byte[] buff = new byte[10240];
                int len, total = 0;
                while ((len = is.read(buff)) != -1) {
                    total += len;
                    System.out.println("anInt = " + total + ", total = " + total);
                    os.write(buff, 0, len);
                    if(total >= anInt){
                        break;
                    }
                }
                os.flush();
            }

//            if(total >= anInt){

            //            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            //4.资源的关闭
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static int toInt(byte[] bytes, int offset) {
        int ret = 0;
        for (int i=0; i<4 && i+offset<bytes.length; i++) {
            ret <<= 8;
            ret |= (int)bytes[i] & 0xFF;
        }
        return ret;
    }
}