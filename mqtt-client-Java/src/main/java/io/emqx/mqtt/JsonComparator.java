package io.emqx.mqtt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class JsonComparator {

    private static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 读取两个 JSON 文件
            JsonNode json1 = mapper.readTree(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\response.json"));
            JsonNode json2 = mapper.readTree(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\lianchuang_20240218_0920.json"));

            // 对最外层数组中的对象进行比较
            compareJsonArrays(json1, json2);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareJsonArrays(JsonNode array1, JsonNode array2) {
        // 循环遍历数组中的对象
        for (JsonNode obj1 : array1) {
            String name1 = obj1.get("name").asText();
            // 在第二个数组中找到相同 name 属性值的对象
            for (JsonNode obj2 : array2) {
                String name2 = obj2.get("name").asText();
                if (name1.equals(name2)) {
                    System.out.println("Comparing objects with name: " + name1);
                    compareJsonObject(obj1, obj2);
                    break; // 找到匹配的对象后，跳出内层循环
                }
            }
        }
    }

    private static void compareJsonObject(JsonNode obj1, JsonNode obj2) {
        // 将 JSON 对象转换为对应的 Java Bean 对象
        NodeBean bean1 = convertJsonToBean(obj1, NodeBean.class);
        NodeBean bean2 = convertJsonToBean(obj2, NodeBean.class);

        // 比较两个 Java Bean 对象
        if (!bean1.equals(bean2)) {
            System.out.println("Objects are not equal:");
            System.out.println("JSON 1: " + bean1);
            System.out.println("JSON 2: " + bean2);
        }
    }

    private static <T> T convertJsonToBean(JsonNode node, Class<T> clazz) {

        try {
            return mapper.treeToValue(node, clazz);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class NodeBean {
        private String name;
        private Id id;
        private RefPos refPos;
        private InLink[] inLinks;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public RefPos getRefPos() {
            return refPos;
        }

        public void setRefPos(RefPos refPos) {
            this.refPos = refPos;
        }

        public InLink[] getInLinks() {
            return inLinks;
        }

        public void setInLinks(InLink[] inLinks) {
            this.inLinks = inLinks;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (obj == null || getClass() != obj.getClass()) {
                return false;
            }
            NodeBean nodeBean = (NodeBean) obj;
            return name.equals(nodeBean.name) &&
                    id.equals(nodeBean.id) &&
                    refPos.equals(nodeBean.refPos) &&
                    Arrays.equals(inLinks, nodeBean.inLinks);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name, id, refPos);
            result = 31 * result + Arrays.hashCode(inLinks);
            return result;
        }

        @Override
        public String toString() {
            return "MyBean{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    ", refPos=" + refPos +
                    ", inLinks=" + Arrays.toString(inLinks) +
                    '}';
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Id {
        private int id;
        private int region;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Id)) {
                return false;
            }
            Id id1 = (Id) o;
            return id == id1.id && region == id1.region;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, region);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RefPos {
        private int elevation;
        private int lat;
        private int lon;

        public int getElevation() {
            return elevation;
        }

        public void setElevation(int elevation) {
            this.elevation = elevation;
        }

        public int getLat() {
            return lat;
        }

        public void setLat(int lat) {
            this.lat = lat;
        }

        public int getLon() {
            return lon;
        }

        public void setLon(int lon) {
            this.lon = lon;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RefPos)) {
                return false;
            }
            RefPos refPos = (RefPos) o;
            return elevation == refPos.elevation && lat == refPos.lat && lon == refPos.lon;
        }

        @Override
        public int hashCode() {
            return Objects.hash(elevation, lat, lon);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class InLink {
        private String name;
        private UpstreamNodeId upstreamNodeId;
        private Movement[] movements;
        private SpeedLimit[] speedLimits;
        private int linkWidth;
        private Point[] points;
        private Lane[] lanes;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public UpstreamNodeId getUpstreamNodeId() {
            return upstreamNodeId;
        }

        public void setUpstreamNodeId(UpstreamNodeId upstreamNodeId) {
            this.upstreamNodeId = upstreamNodeId;
        }

        public Movement[] getMovements() {
            return movements;
        }

        public void setMovements(Movement[] movements) {
            this.movements = movements;
        }

        public SpeedLimit[] getSpeedLimits() {
            return speedLimits;
        }

        public void setSpeedLimits(SpeedLimit[] speedLimits) {
            this.speedLimits = speedLimits;
        }

        public int getLinkWidth() {
            return linkWidth;
        }

        public void setLinkWidth(int linkWidth) {
            this.linkWidth = linkWidth;
        }

        public Point[] getPoints() {
            return points;
        }

        public void setPoints(Point[] points) {
            this.points = points;
        }

        public Lane[] getLanes() {
            return lanes;
        }

        public void setLanes(Lane[] lanes) {
            this.lanes = lanes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof InLink)) {
                return false;
            }
            InLink inLink = (InLink) o;
            return linkWidth == inLink.linkWidth && Objects.equals(name, inLink.name) && Objects.equals(upstreamNodeId, inLink.upstreamNodeId) && Arrays.equals(movements, inLink.movements) && Arrays.equals(speedLimits, inLink.speedLimits) && Arrays.equals(points, inLink.points) && Arrays.equals(lanes, inLink.lanes);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name, upstreamNodeId, linkWidth);
            result = 31 * result + Arrays.hashCode(movements);
            result = 31 * result + Arrays.hashCode(speedLimits);
            result = 31 * result + Arrays.hashCode(points);
            result = 31 * result + Arrays.hashCode(lanes);
            return result;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UpstreamNodeId {
        private int id;
        private int region;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof UpstreamNodeId)) {
                return false;
            }
            UpstreamNodeId that = (UpstreamNodeId) o;
            return id == that.id && region == that.region;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, region);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Movement {
        private int phaseId;
        private RemoteIntersection remoteIntersection;

        public int getPhaseId() {
            return phaseId;
        }

        public void setPhaseId(int phaseId) {
            this.phaseId = phaseId;
        }

        public RemoteIntersection getRemoteIntersection() {
            return remoteIntersection;
        }

        public void setRemoteIntersection(RemoteIntersection remoteIntersection) {
            this.remoteIntersection = remoteIntersection;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Movement)) {
                return false;
            }
            Movement movement = (Movement) o;
            return phaseId == movement.phaseId && Objects.equals(remoteIntersection, movement.remoteIntersection);
        }

        @Override
        public int hashCode() {
            return Objects.hash(phaseId, remoteIntersection);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RemoteIntersection {
        private int id;
        private int region;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getRegion() {
            return region;
        }

        public void setRegion(int region) {
            this.region = region;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof RemoteIntersection)) {
                return false;
            }
            RemoteIntersection that = (RemoteIntersection) o;
            return id == that.id && region == that.region;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, region);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SpeedLimit {
        private String type;
        private int speed;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public int getSpeed() {
            return speed;
        }

        public void setSpeed(int speed) {
            this.speed = speed;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof SpeedLimit)) {
                return false;
            }
            SpeedLimit that = (SpeedLimit) o;
            return speed == that.speed && Objects.equals(type, that.type);
        }

        @Override
        public int hashCode() {
            return Objects.hash(type, speed);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Point {
        private PosOffset posOffset;

        public PosOffset getPosOffset() {
            return posOffset;
        }

        public void setPosOffset(PosOffset posOffset) {
            this.posOffset = posOffset;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Point)) {
                return false;
            }
            Point point = (Point) o;
            return Objects.equals(posOffset, point.posOffset);
        }

        @Override
        public int hashCode() {
            return Objects.hash(posOffset);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PosOffset {
        private OffsetLL offsetLL;
        private OffsetV offsetV;

        public OffsetLL getOffsetLL() {
            return offsetLL;
        }

        public void setOffsetLL(OffsetLL offsetLL) {
            this.offsetLL = offsetLL;
        }

        public OffsetV getOffsetV() {
            return offsetV;
        }

        public void setOffsetV(OffsetV offsetV) {
            this.offsetV = offsetV;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PosOffset)) {
                return false;
            }
            PosOffset posOffset = (PosOffset) o;
            return Objects.equals(offsetLL, posOffset.offsetLL) && Objects.equals(offsetV, posOffset.offsetV);
        }

        @Override
        public int hashCode() {
            return Objects.hash(offsetLL, offsetV);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OffsetLL {
        private PositionLatLon positionLatLon;

        public PositionLatLon getPositionLatLon() {
            return positionLatLon;
        }

        public void setPositionLatLon(PositionLatLon positionLatLon) {
            this.positionLatLon = positionLatLon;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof OffsetLL)) {
                return false;
            }
            OffsetLL offsetLL = (OffsetLL) o;
            return Objects.equals(positionLatLon, offsetLL.positionLatLon);
        }

        @Override
        public int hashCode() {
            return Objects.hash(positionLatLon);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class PositionLatLon {
        private int lon;
        private int lat;


        public int getLon() {
            return lon;
        }

        public void setLon(int lon) {
            this.lon = lon;
        }

        public int getLat() {
            return lat;
        }

        public void setLat(int lat) {
            this.lat = lat;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof PositionLatLon)) {
                return false;
            }
            PositionLatLon that = (PositionLatLon) o;
            return lon == that.lon && lat == that.lat;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lon, lat);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OffsetV {
        private int elevation;

        public int getElevation() {
            return elevation;
        }

        public void setElevation(int elevation) {
            this.elevation = elevation;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof OffsetV)) {
                return false;
            }
            OffsetV offsetV = (OffsetV) o;
            return elevation == offsetV.elevation;
        }

        @Override
        public int hashCode() {
            return Objects.hash(elevation);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Lane {
        private ConnectsTo[] connectsTo;
        private SpeedLimit[] speedLimits;
        private int laneID;
        private int laneWidth;
        private String maneuvers;
        private Point[] points;

        public ConnectsTo[] getConnectsTo() {
            return connectsTo;
        }

        public void setConnectsTo(ConnectsTo[] connectsTo) {
            this.connectsTo = connectsTo;
        }

        public SpeedLimit[] getSpeedLimits() {
            return speedLimits;
        }

        public void setSpeedLimits(SpeedLimit[] speedLimits) {
            this.speedLimits = speedLimits;
        }

        public int getLaneID() {
            return laneID;
        }

        public void setLaneID(int laneID) {
            this.laneID = laneID;
        }

        public int getLaneWidth() {
            return laneWidth;
        }

        public void setLaneWidth(int laneWidth) {
            this.laneWidth = laneWidth;
        }

        public String getManeuvers() {
            return maneuvers;
        }

        public void setManeuvers(String maneuvers) {
            this.maneuvers = maneuvers;
        }

        public Point[] getPoints() {
            return points;
        }

        public void setPoints(Point[] points) {
            this.points = points;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Lane)) {
                return false;
            }
            Lane lane = (Lane) o;
            return laneID == lane.laneID && laneWidth == lane.laneWidth && Arrays.equals(connectsTo, lane.connectsTo) && Arrays.equals(speedLimits, lane.speedLimits) && Objects.equals(maneuvers, lane.maneuvers) && Arrays.equals(points, lane.points);
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(laneID, laneWidth, maneuvers);
            result = 31 * result + Arrays.hashCode(connectsTo);
            result = 31 * result + Arrays.hashCode(speedLimits);
            result = 31 * result + Arrays.hashCode(points);
            return result;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConnectsTo {
        private int phaseId;
        private RemoteIntersection remoteIntersection;
        private ConnectingLane connectingLane;

        public int getPhaseId() {
            return phaseId;
        }

        public void setPhaseId(int phaseId) {
            this.phaseId = phaseId;
        }

        public RemoteIntersection getRemoteIntersection() {
            return remoteIntersection;
        }

        public void setRemoteIntersection(RemoteIntersection remoteIntersection) {
            this.remoteIntersection = remoteIntersection;
        }

        public ConnectingLane getConnectingLane() {
            return connectingLane;
        }

        public void setConnectingLane(ConnectingLane connectingLane) {
            this.connectingLane = connectingLane;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ConnectsTo)) {
                return false;
            }
            ConnectsTo that = (ConnectsTo) o;
            return phaseId == that.phaseId && Objects.equals(remoteIntersection, that.remoteIntersection) && Objects.equals(connectingLane, that.connectingLane);
        }

        @Override
        public int hashCode() {
            return Objects.hash(phaseId, remoteIntersection, connectingLane);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ConnectingLane {
        private int lane;
        private String maneuver;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ConnectingLane)) {
                return false;
            }
            ConnectingLane that = (ConnectingLane) o;
            return lane == that.lane && Objects.equals(maneuver, that.maneuver);
        }

        public int getLane() {
            return lane;
        }

        public void setLane(int lane) {
            this.lane = lane;
        }

        public String getManeuver() {
            return maneuver;
        }

        public void setManeuver(String maneuver) {
            this.maneuver = maneuver;
        }

        @Override
        public int hashCode() {
            return Objects.hash(lane, maneuver);
        }
    }
}
