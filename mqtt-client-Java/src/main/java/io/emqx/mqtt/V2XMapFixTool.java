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

                    updateRoadNodeRelation(conn, start, end);
                    Thread.sleep(10);
                    updateLandPoint(conn, start, end);
                    Thread.sleep(10);
                    updateRoadTurnRelation(conn, start, end);
                    Thread.sleep(10);
                    System.out.println("================================================================");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void updateRoadNodeRelation(Connection conn, String start, String end) throws SQLException {
        String sql = "INSERT INTO RoadNodeRelation (RoadID, TopNodeID, NodeID, RoadWidth, RoadLimitSpeed, LandID) " +
                "SELECT d2.RoadID, d2.TopNodeID, d2.NodeID, d2.RoadWidth, d2.RoadLimitSpeed, d2.LandID " +
                "FROM db2.RoadNodeRelation d2 " +
                "WHERE NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM RoadNodeRelation d1 " +
                "    WHERE d1.RoadID = d2.RoadID AND d1.LandID = d2.LandID " +
                ") " +
                "AND d2.TopNodeID = ( " +
                "    SELECT NP.ID " +
                "    FROM db2.NodePoint NP " +
                "    WHERE NP.OrderIndex = ? " +
                ") " +
                "AND d2.NodeID = ( " +
                "    SELECT NP.ID " +
                "    FROM db2.NodePoint NP " +
                "    WHERE NP.OrderIndex = ? " +
                ");";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, start);
            pstmt.setString(2, end);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("updateRoadNodeRelation (" + start + "-" + end + ") Affected rows: " + affectedRows);
        }
    }

    private static void updateLandPoint(Connection conn, String start, String end) throws SQLException {
        String sql = "INSERT INTO LandPoint (Lng, Lat, Height, CreateTime, RoadID, OrderIndex, LandID) " +
                "SELECT d2.Lng, d2.Lat, d2.Height, d2.CreateTime, d2.RoadID, d2.OrderIndex, d2.LandID " +
                "FROM db2.LandPoint AS d2 " +
                "JOIN db2.RoadNodeRelation AS rnr2 ON d2.RoadID = rnr2.RoadID AND d2.LandID = rnr2.LandID " +
                "JOIN db2.NodePoint AS NP1_2 ON rnr2.TopNodeID = NP1_2.ID " +
                "JOIN db2.NodePoint AS NP2_2 ON rnr2.NodeID = NP2_2.ID " +
                "WHERE NP1_2.OrderIndex = ? AND NP2_2.OrderIndex = ? " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM LandPoint AS d1 " +
                "    JOIN RoadNodeRelation AS rnr1 ON d1.RoadID = rnr1.RoadID AND d1.LandID = rnr1.LandID " +
                "    JOIN NodePoint AS NP1_1 ON rnr1.TopNodeID = NP1_1.ID " +
                "    JOIN NodePoint AS NP2_1 ON rnr1.NodeID = NP2_1.ID " +
                "    WHERE NP1_1.OrderIndex = ? AND NP2_1.OrderIndex = ? " +
                "    AND d1.RoadID = d2.RoadID AND d1.LandID = d2.LandID" +
                ") " +
                "ORDER BY d2.ID ASC;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, start);
            pstmt.setString(2, end);
            pstmt.setString(3, start);
            pstmt.setString(4, end);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("updateLandPoint (" + start + "-" + end + ") Affected rows: " + affectedRows);
        }
    }

    private static void updateRoadTurnRelation(Connection conn, String start, String end) throws SQLException {
        String sql = "INSERT INTO RoadTurnRelation (RoadID, NextNodeID, LandID, Turn, TurnDirection, OrderNum, TargetLandID) " +
                "SELECT rtr.RoadID, rtr.NextNodeID, rtr.LandID, rtr.Turn, rtr.TurnDirection, rtr.OrderNum, rtr.TargetLandID " +
                "FROM db2.RoadTurnRelation rtr " +
                "WHERE rtr.RoadID IN ( " +
                "    SELECT rnr.RoadID " +
                "    FROM db2.RoadNodeRelation rnr " +
                "    JOIN db2.NodePoint NP ON rnr.NodeID = NP.ID " +
                "    WHERE NP.OrderIndex = ? " +
                "    AND rnr.TopNodeID IN ( " +
                "        SELECT NP.ID " +
                "        FROM db2.NodePoint NP " +
                "        WHERE NP.OrderIndex = ? " +
                "    )" +
                ") " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM RoadTurnRelation rtr1 " +
                "    WHERE rtr1.RoadID = rtr.RoadID AND rtr1.LandID = rtr.LandID " +
                ")" +
                "ORDER BY rtr.ID ASC;";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, end); // 注意这里的顺序与前面方法中的SQL条件顺序一致
            pstmt.setString(2, start);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("updateRoadTurnRelation (" + start + "-" + end + ") Affected rows: " + affectedRows);
        }
    }


}
