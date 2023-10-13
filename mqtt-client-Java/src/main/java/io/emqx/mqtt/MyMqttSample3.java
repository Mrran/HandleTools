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


public class MyMqttSample3 {

    public static void main(String[] args) {
        String broker = "tcp://127.0.0.1:8080";
        String clientId = MqttClient.generateClientId();
        MemoryPersistence persistence = new MemoryPersistence();
        MqttConnectOptions connOpts = new MqttConnectOptions();
        try {
            MqttClient client = new MqttClient(broker, clientId, persistence);
            client.setCallback(new SampleCallback());

            System.out.println("Connecting to broker: " + broker);
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

    private static void mockDataSend(MqttClient client) {

        try {
            //获取assests文件夹下的内容
            InputStreamReader in = new InputStreamReader(Files.newInputStream(Paths.get("D:\\work\\doc\\business\\SmartCar\\log\\yuanshi.txt")), StandardCharsets.UTF_8);
//            InputStreamReader in = new InputStreamReader(Files.newInputStream(Paths.get("D:\\work\\doc\\business\\SmartCar\\log\\yuanshi_no_speed.txt")), StandardCharsets.UTF_8);
           // InputStreamReader in = new InputStreamReader(Files.newInputStream(Paths.get("D:\\work\\doc\\business\\SmartCar\\log\\mqtt830.log")), StandardCharsets.UTF_8);
//            InputStreamReader in = new InputStreamReader(Files.newInputStream(Paths.get("D:\\work\\doc\\business\\SmartCar\\log\\yuanshi2.txt")), StandardCharsets.UTF_8);
            //读取文件的信息
            BufferedReader br = new BufferedReader(in);
            String line = "";
            int count = 0;

            while ((line = br.readLine()) != null) {
                count++;

                if (!line.contains("{\"HVMSG\"")) {
                    continue;
                }
                //line = "{\"msgType\":\"OBU\",\"data\":" + line + "}";

                /**
                 * 3750 国博
                 * 400 alertType=37
                 * 3780 alertType=46
                 * 4630 alertType=s78
                 * 4300 alertType=306
                 * 5350 下个
                 */

                if (count < 0) {
                    continue;
                }

                MqttMessage message = new MqttMessage(line.getBytes());
                message.setQos(0);
                client.publish("/obu/RunningData", message);

                System.out.println("count = " + count);

                Thread.sleep(100);

            }
            System.out.println("loop finished!!!");
            // mockDataSend(client);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
