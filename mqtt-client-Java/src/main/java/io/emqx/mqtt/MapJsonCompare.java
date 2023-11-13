package io.emqx.mqtt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.emqx.mqtt.bean.Node;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Objects;

public class MapJsonCompare {


    public static void main(String[] args) {
        Node[] nodeArrayNew = readJsonFile("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\test0.json");
        Node[] nodeArrayOld0 = readJsonFile("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\test1.json");
        compareJson(nodeArrayNew, nodeArrayOld0);
    }

    public static Node[] readJsonFile(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(new File(filePath), Node[].class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static void compareJson(Node[] nodeArrayNew, Node[] nodeArrayOld) {
        for (Node nodeNew : nodeArrayNew) {
            for (Node nodeOld : nodeArrayOld) {
                if (nodeNew.getName().equals(nodeOld.getName())) {
//                    // 比较 node0 和 node1 的内容
//                    if (!nodeNew.equals(nodeOld)) {
//                        System.out.println("差异在节点: " + nodeNew.getName());
//                        // 这里可以进一步打印出差异的具体内容
//                    }
                    compareAndPrintDifferences(nodeNew, nodeOld);

                }
            }
        }
    }

    public static void compareAndPrintDifferences(Node o1, Node o2) {
        if (o1 == null || o2 == null) {
            System.out.println("One of the objects is null");
            return;
        }

        if (o1.getClass() != o2.getClass()) {
            System.out.println("Objects are of different classes.");
            return;
        }

        for (Field field : o1.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            try {
                Object value1 = field.get(o1);
                Object value2 = field.get(o2);

                if (field.getType().equals(List.class)) {
                    compareLists((List<?>) value1, (List<?>) value2, field.getName());
                } else {
                    if (!Objects.equals(value1, value2)) {
                        System.out.println("Difference found in field '" + field.getName() + "': " + value1 + " != " + value2);
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    private static void compareLists(List<?> list1, List<?> list2, String fieldName) {
        if (list1 == null || list2 == null) {
            System.out.println("Difference in field '" + fieldName + "': one of the lists is null");
            return;
        }

        if (list1.size() != list2.size()) {
            System.out.println("Difference in field '" + fieldName + "': lists have different sizes");
            return;
        }

        for (int i = 0; i < list1.size(); i++) {
            if (!Objects.equals(list1.get(i), list2.get(i))) {
                System.out.println("Difference found in list '" + fieldName + "' at index " + i + ": " + list1.get(i) + " != " + list2.get(i));
            }
        }
    }


}
