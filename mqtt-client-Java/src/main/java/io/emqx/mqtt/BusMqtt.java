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


public class BusMqtt {
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

    public static int mockData = 1;


    private static void mockDataSend(MqttClient client) {

        String line = "{\"gpio\":" + mockData + "}";

        MqttMessage message = new MqttMessage(line.getBytes());
        message.setQos(0);
        try {
            client.publish("/weiping/gpio", message);
            Thread.sleep(2000);
        } catch (MqttException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

}
