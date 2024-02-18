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


public class MyMqttMap {
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

            mockDataSend(client);


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
        try {
            // 一次性读取整个文件到一个字符串
            // String content = new String(Files.readAllBytes(Paths.get("D:\\work\\doc\\business\\rtty\\sqlite\\xingyun_map\\0651原196---改.json")), StandardCharsets.UTF_8);

            // String content = new String(Files.readAllBytes(Paths.get("D:\\work\\doc\\business\\rtty\\sqlite\\huali_map\\map-3400-416.json")), StandardCharsets.UTF_8);

            // String content = new String(Files.readAllBytes(Paths.get("D:\\work\\doc\\business\\rtty\\sqlite\\xingyun_map\\星云20240103\\3301原160.json")), StandardCharsets.UTF_8);

            //String content = new String(Files.readAllBytes(Paths.get("D:\\work\\doc\\business\\rtty\\sqlite\\xingyun_map\\地图-2024-1-22\\800原188.json")), StandardCharsets.UTF_8);

            String content = new String(Files.readAllBytes(Paths.get("D:\\work\\doc\\business\\rtty\\sqlite\\xingyun_map\\星云20240103\\2806原170.json")), StandardCharsets.UTF_8);

            // 这里是处理文件内容的代码，比如发送 MQTT 消息
            // 例如，发送整个文件内容作为一个消息
            while (true){
                if (!content.isEmpty()) {
                    MqttMessage message = new MqttMessage(content.getBytes());
                    message.setQos(0);
                    client.publish("caeri/v1/v2x/MAP", message);
                }
                Thread.sleep(1000);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // 这里不再需要关闭 BufferedReader 或 InputStreamReader
        }
    }

}
