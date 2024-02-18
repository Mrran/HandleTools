package io.emqx.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;


public class MyMqttCollect {
    public static void main(String[] args) {

        startMockThread();

        String broker = "tcp://127.0.0.1:1883";
        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            client.setCallback(new SampleCallback());

            //System.out.println("Connecting to broker: " + broker);
            client.connect(connOpts);

            while (true){
                mockDataSend(client);
            }


//            client.disconnect();
//            client.close();
//            System.exit(0);
        } catch (MqttException me) {
            me.printStackTrace();
        }
    }

    private static void startMockThread() {
        new Thread(() -> {
            while (true){
                Scanner scanner = new Scanner(System.in);//2.构造一个“标准输入流”System.in关联的Scanner对象。
                System.out.println("请输入：");
                String s = scanner.nextLine();//3.读取输入
                try {
                    mockData = Integer.parseInt(s);
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 1: 模拟 uu违停
     * 2、模拟 交通管制
     */
    public static int mockData;


    private static void mockDataSend(MqttClient client) {
        BufferedReader br = null;
        InputStreamReader in = null;

        try {
            String line;
            int count = 0;

            while (true) {
                if (br == null) {
                    in = new InputStreamReader(Files.newInputStream(Paths.get("D:\\work\\doc\\business\\SmartCar\\log\\yuanshi_no_speed.txt")), StandardCharsets.UTF_8);
                    br = new BufferedReader(in);
                }

                if (mockData == 1) {
                    Thread.sleep(1000);
                } else {
                    line = br.readLine();

                    if (line == null) {
                        // 重新开始从文件头读取
                        br.close();
                        in.close();
                        br = null;
                        in = null;
                        count = 0;
                        continue;
                    }

                    count++;

                    if (!line.contains("{\"HVMSG\"")) {
                        continue;
                    }
                    if (count < 480) {
                        continue;
                    }

                    if (!line.isEmpty()) {
                        MqttMessage message = new MqttMessage(line.getBytes());
                        message.setQos(0);
                        client.publish("/obu/RunningData", message);
                    }

                    Thread.sleep(100);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (br != null) {
                    br.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
