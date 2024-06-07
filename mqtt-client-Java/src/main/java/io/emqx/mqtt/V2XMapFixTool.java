package io.emqx.mqtt;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class V2XMapFixTool {
    public static void main(String[] args) {
        // SQLite数据库连接字符串，确保指向正确的数据库文件
        String dbURL = "jdbc:sqlite:D:\\work\\doc\\business\\rtty\\V2X-MapMark-master\\V2X-MapMark-master\\MapMark\\MapCollect.db";

        String filePath = "D:\\work\\doc\\business\\rtty\\V2X-MapMark-master\\new_lane_import_fix.txt";
        List<String> inputList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                inputList.add(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 转换List为Array
        String[] inputArray = inputList.toArray(new String[0]);

        try (Connection conn = DriverManager.getConnection(dbURL)) {
            if (conn != null) {

                // 第二个数据库的文件路径，需要附加到主数据库
                String secondDbPath = "D:\\work\\doc\\business\\rtty\\V2X-MapMark-master\\杨修正后的地图\\20240524\\MapCollect_new2(12).db";

                     Statement stmt = conn.createStatement();

                    // 执行ATTACH DATABASE命令附加第二个数据库
                    String sqlAttach = "ATTACH DATABASE '" + secondDbPath.replace("\\", "\\\\") + "' AS db2";
                    stmt.execute(sqlAttach);

                    // 从这一点开始，你可以执行跨数据库的查询，例如访问db2中的表
                    // 示例：SELECT * FROM db2.TableName;

                    System.out.println("Database attached successfully.");

                for (String item : inputArray) {
                    // 解析数组中的项
                    String[] parts = item.split("-");
                    String start = parts[0];
                    String end = parts[1];

                    /*updateRoadNodeRelation(conn, start, end);
                    Thread.sleep(10);
                    updateLandPoint(conn, start, end);
                    Thread.sleep(10);
                    updateRoadTurnRelation(conn, start, end);
                    Thread.sleep(10);*/
                    System.out.println("================================================================");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }






}
