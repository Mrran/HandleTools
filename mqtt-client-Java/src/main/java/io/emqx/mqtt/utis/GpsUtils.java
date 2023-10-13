package io.emqx.mqtt.utis;

import java.math.BigDecimal;

public class GpsUtils {

    private static final double EARTH_RADIUS = 6371000.0; // 地球半径（米）

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // 将经纬度从度转换为弧度
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        // 计算经纬度差值
        double dlon = lon2 - lon1;
        double dlat = lat2 - lat1;

        // 使用Haversine公式计算距离
        double a = Math.pow(Math.sin(dlat / 2), 2) + Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(dlon / 2), 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = EARTH_RADIUS * c; // 单位：米

        return distance;
    }

    public static void main(String[] args) {
        // 示例坐标（纬度，经度）

        // 240m
        double lat1 = 44.8276142;
        double lon1 = 89.2713464;
        double lat2 = 44.8276142;

        double lon2 = 89.2728690;

        double distance = calculateDistance(lat1, lon1, lat2, lon2);

        System.out.println("距离：" + distance + " 米");
    }

    public static void main2(String[] args) {
        // 示例坐标（纬度，经度）

        // 160m
        double lat1 = 44.8276142;
        double lon1 = 89.2713464;
        double lat2 = 44.8276142;

        double lon2 = 89.2733753;

        double distance = calculateDistance(lat1, lon1, lat2, lon2);

        System.out.println("距离：" + distance + " 米");
    }
}