package io.emqx.mqtt;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;

//客户端
public class TcpClientTest {

    private final static int READ_COUNT = 1024;

    private static int seq = 0;

    private final static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss_SSS");


    public static void main(String[] args) {
        Socket socket = null;
        OutputStream out = null;
        try {
            //1.创建Socket对象，指明服务器IP和端口号
            //socket = new Socket("192.168.137.151", 9999);
            socket = new Socket("192.168.28.147", 9999);
            //2.获取一个输出流用于输出数据

            out = socket.getOutputStream();
            //3.写出数据的操作(这里也可以用转换流OutputStreamWriter)
            out.write((byte) 0x01);



            InputStream is = socket.getInputStream();

            //Thread.sleep(100);

            while (true){
                String format = formatter.format(System.currentTimeMillis());
                File pic = new File("C:\\Users\\ranfeng\\Desktop\\temp\\pic5\\capture_" + format +".jpg");
                FileOutputStream os = new FileOutputStream(pic);

                byte[] type = new byte[1];
                int typeCount = is.read(type);
                //System.out.println("count = " + typeCount);

                byte[] lenght = new byte[4];
                int lenCount = is.read(lenght);
                printLen(lenght);

                int allCount = toInt(lenght, 0);
                System.out.println(" type = " + type[0] + ", AllCount = " + allCount);


                byte[] buff = new byte[READ_COUNT];
                int len, total = 0;
                while ((len = is.read(buff)) != -1) {
                    total += len;
                    //System.out.println("allCount = " + allCount + ", total = " + total + ", len = " + len);
                    os.write(buff, 0, len);

                    if(allCount - total < READ_COUNT){
                        while ((len = is.read(buff, 0, allCount - total)) != -1) {
                            total += len;
                            System.out.println("allCount2222 = " + allCount + ", total = " + total + ", len = " + len);
                            os.write(buff, 0, len);

                            if(total >= allCount){
                                break;
                            }

                        }
                        break;
                    }
                }
                os.flush();
                seq++;

                if(seq >= 500){
                    is.close();
                    break;
                }
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

    private static void printLen(byte[] lenght) {
        StringBuilder sb = new StringBuilder();
        for (byte b : lenght) {
            sb.append(b).append("-");
        }
        System.out.println("=======================================>" + sb);
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