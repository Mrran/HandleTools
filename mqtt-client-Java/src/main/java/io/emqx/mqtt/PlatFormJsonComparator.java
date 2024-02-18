package io.emqx.mqtt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;

public class PlatFormJsonComparator {

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();

            // 读取两个 JSON 文件
            JsonNode json1 = mapper.readTree(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\response.json"));
            JsonNode json2 = mapper.readTree(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\lianchuang_20240218_0920.json"));

            // 比较两个 JSON 对象
            compareJsonArrays(json1, json2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareJsonArrays(JsonNode array1, JsonNode array2) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("diff.txt"));

        int length1 = array1.size();
        int length2 = array2.size();
        int minLength = Math.min(length1, length2);

        for (int i = 0; i < minLength; i++) {
            JsonNode obj1 = array1.get(i);
            JsonNode obj2 = array2.get(i);

            compareJsonNode(obj1, obj2, "", writer);
        }

        // 处理数组长度不一致的情况
        if (length1 != length2) {
            writer.write("Arrays have different lengths: " + length1 + " and " + length2 + "\n");
        }

        writer.close();
    }

    private static void compareJsonNode(JsonNode node1, JsonNode node2, String currentPath, BufferedWriter writer) throws IOException {
        Iterator<String> fieldNames1 = node1.fieldNames();

        while (fieldNames1.hasNext()) {
            String fieldName = fieldNames1.next();
            JsonNode childNode1 = node1.get(fieldName);
            JsonNode childNode2 = node2.get(fieldName);

            if (childNode2 == null) {
                writer.write("In JSON 1, missing property: " + currentPath + fieldName + "\n");
            } else if (!childNode1.equals(childNode2)) {
                writer.write("Property '" + currentPath + fieldName + "' differs: \n");
                writer.write("  JSON 1: " + childNode1.toString() + "\n");
                writer.write("  JSON 2: " + childNode2.toString() + "\n");
            }

            // 递归处理对象属性
            if (childNode1.isObject() && childNode2.isObject()) {
                compareJsonNode(childNode1, childNode2, currentPath + fieldName + ".", writer);
            }
        }

        // 检查 JSON 2 中是否有 JSON 1 中没有的属性
        Iterator<String> fieldNames2 = node2.fieldNames();
        while (fieldNames2.hasNext()) {
            String fieldName = fieldNames2.next();
            JsonNode childNode2 = node2.get(fieldName);
            JsonNode childNode1 = node1.get(fieldName);

            if (childNode1 == null) {
                writer.write("In JSON 2, missing property: " + currentPath + fieldName + "\n");
            }
        }
    }
}
