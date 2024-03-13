package io.emqx.mqtt;

import java.sql.*;

public class SQLiteExample {
    public static void main(String[] args) {
        // SQLite数据库连接字符串，确保指向正确的数据库文件
        String dbURL = "jdbc:sqlite:D:\\work\\doc\\business\\rtty\\V2X-MapMark-master\\V2X-MapMark-master\\MapMark\\MapCollect.db";
        String[] inputArray = {"191-192", "192-193", "193-194"};

        try (Connection conn = DriverManager.getConnection(dbURL)) {
            if (conn != null) {

                // 第二个数据库的文件路径，需要附加到主数据库
                String secondDbPath = "D:\\work\\doc\\business\\rtty\\V2X-MapMark-master\\V2X-MapMark-master\\MapMark\\MapCollect_new_collect.db";

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
                    updateLandPoint(conn, start, end);
                    updateRoadTurnRelation(conn, start, end);
                    System.out.println("================================================================");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
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
            System.out.println("updateRoadNodeRelation Affected rows: " + affectedRows);
        }
    }

    private static void updateLandPoint(Connection conn, String start, String end) throws SQLException {
        String sql = "INSERT INTO LandPoint (Lng, Lat, Height, CreateTime, RoadID, OrderIndex, LandID) " +
                "SELECT lp.Lng, lp.Lat, lp.Height, lp.CreateTime, lp.RoadID, lp.OrderIndex, lp.LandID " +
                "FROM db2.LandPoint lp " +
                "JOIN db2.RoadNodeRelation rnr ON lp.RoadID = rnr.RoadID AND lp.LandID = rnr.LandID " +
                "JOIN db2.NodePoint NP1 ON rnr.TopNodeID = NP1.ID " +
                "JOIN db2.NodePoint NP2 ON rnr.NodeID = NP2.ID " +
                "WHERE NP1.OrderIndex = ? AND NP2.OrderIndex = ? " +
                "AND NOT EXISTS ( " +
                "    SELECT 1 " +
                "    FROM LandPoint lp1 " +
                "    JOIN RoadNodeRelation rnr1 ON lp1.RoadID = rnr1.RoadID AND lp1.LandID = rnr1.LandID " +
                "    JOIN NodePoint NP1_1 ON rnr1.TopNodeID = NP1_1.ID " +
                "    JOIN NodePoint NP2_1 ON rnr1.NodeID = NP2_1.ID " +
                "    WHERE NP1_1.OrderIndex = ? AND NP2_1.OrderIndex = ? " +
                "    AND lp1.ID = lp.ID " +
                ");";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, start);
            pstmt.setString(2, end);
            pstmt.setString(3, start);
            pstmt.setString(4, end);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("updateLandPoint Affected rows: " + affectedRows);
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
                ");";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, end); // 注意这里的顺序与前面方法中的SQL条件顺序一致
            pstmt.setString(2, start);
            int affectedRows = pstmt.executeUpdate();
            System.out.println("updateRoadTurnRelation Affected rows: " + affectedRows);
        }
    }


}
