package io.emqx.mqtt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
//import io.emqx.mqtt.bean.Lane;
//import io.emqx.mqtt.bean.SpeedLimit;

import java.io.*;
import java.util.*;

public class PlatFormJsonComparator {

    private static ObjectMapper mapper = new ObjectMapper();

    private static List<NodeBean> nodeList1 = new ArrayList<>();
    private static List<NodeBean> nodeList2 = new ArrayList<>();

    public static void main(String[] args) {
        redirectOutputToFile("diff.log");
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            // 读取两个 JSON 文件
            JsonNode ja1 = mapper.readTree(new File("D:\\work\\doc\\business\\rtty\\sqlite\\server\\map.json"));
            JsonNode ja2 = mapper.readTree(new File("D:\\work\\doc\\business\\rtty\\sqlite\\server\\lc_all_20240313.json"));

            jsonToList1(ja1);
            jsonToList2(ja2);

            // 对最外层数组中的对象进行比较
            compareJsonArrays();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static void redirectOutputToFile(String filename) {
        try {
            PrintStream out = new PrintStream(new FileOutputStream(filename));
            System.setOut(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private static void jsonToList1(JsonNode ja) {
        for (JsonNode obj : ja) {
            NodeBean node = convertJsonToBean(obj, NodeBean.class);

            NodeBean findNode = null;
            for(int i=0; i<nodeList1.size(); i++){
                if(nodeList1.get(i).id.id == node.id.id){
                    findNode = nodeList1.get(i);

                    int totalLength = findNode.inLinks.length + node.inLinks.length;
                    InLink[] result = new InLink[totalLength];
                    System.arraycopy(findNode.inLinks, 0, result, 0, findNode.inLinks.length);
                    System.arraycopy(node.inLinks, 0, result, findNode.inLinks.length, node.inLinks.length);

                    // 对result数组进行排序
                    InLink[] sortedResult = Arrays.stream(result)
                            .sorted((r1, r2) -> {
                                int firstIndex1 = Integer.parseInt(r1.name.split("-")[0]);
                                int firstIndex2 = Integer.parseInt(r2.name.split("-")[0]);
                                return Integer.compare(firstIndex1, firstIndex2); // 注意此处的比较逻辑，可能需要根据实际需求调整
                            })
                            .toArray(InLink[]::new); // 将排序后的流转换回数组

                    findNode.inLinks = sortedResult;
                }
            }

            // 未找到
            if(findNode == null){
                nodeList1.add(node);
            }
        }
    }

    private static void jsonToList2(JsonNode ja) {
        for (JsonNode obj : ja) {
            NodeBean node = convertJsonToBean(obj, NodeBean.class);

            // 对result数组进行排序
            InLink[] sortedResult = Arrays.stream(node.inLinks)
                    .sorted((r1, r2) -> {
                        int firstIndex1 = Integer.parseInt(r1.name.split("-")[0]);
                        int firstIndex2 = Integer.parseInt(r2.name.split("-")[0]);
                        return Integer.compare(firstIndex1, firstIndex2); // 注意此处的比较逻辑，可能需要根据实际需求调整
                    })
                    .toArray(InLink[]::new); // 将排序后的流转换回数组

            node.inLinks = sortedResult;


            NodeBean findNode = null;
            for(int i=0; i<nodeList2.size(); i++){
                if(nodeList2.get(i).id.id == node.id.id){
                    findNode = nodeList2.get(i);

                    int totalLength = findNode.inLinks.length + node.inLinks.length;
                    InLink[] result = new InLink[totalLength];
                    System.arraycopy(findNode.inLinks, 0, result, 0, findNode.inLinks.length);
                    System.arraycopy(node.inLinks, 0, result, findNode.inLinks.length, node.inLinks.length);

                    findNode.inLinks = result;
                }
            }

            // 未找到
            if(findNode == null){
                nodeList2.add(node);
            }
        }
    }

    private static void compareJsonArrays() {
        // 循环遍历数组中的对象
        for (NodeBean obj1 : nodeList1) {
            String name1 = obj1.getName();
            // 在第二个数组中找到相同 name 属性值的对象
            for (NodeBean obj2 : nodeList2) {
                String name2 = obj2.getName();
                if (name1.equals(name2)) {
                    System.out.println("============================================> Comparing objects with name: " + name1);
                    compareJsonObject(obj1, obj2);
                    break; // 找到匹配的对象后，跳出内层循环
                }
            }
        }
    }

    private static void compareJsonObject(NodeBean bean1, NodeBean bean2) {
        // 比较两个 Java Bean 对象
        if (!bean1.equals(bean2)) {
            System.out.println("Objects are not equal:");
            System.out.println(convertBeanToJson(bean1));
            System.out.println(convertBeanToJson(bean2));
        }
    }


    private static String convertBeanToJson(Object bean) {
        try {
            return mapper.writeValueAsString(bean);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
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

            boolean equal = name.equals(nodeBean.name) &&
                    id.equals(nodeBean.id) &&
                    refPos.equals(nodeBean.refPos) &&
                    Arrays.equals(inLinks, nodeBean.inLinks);
            if(!equal){
           //     System.out.println("NodeBean not equal, this = " + this + ", that = " + obj);
            }

            return equal;
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
        public String toString() {
            return "Id{" +
                    "id=" + id +
                    ", region=" + region +
                    '}';
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

            boolean equal = id == id1.id && region == id1.region;
            if(!equal){
                System.out.println("Id not equal, this = ");
            }

            return equal;
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

        @JsonProperty("long")
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
        public String toString() {
            return "RefPos{" +
                    "elevation=" + elevation +
                    ", lat=" + lat +
                    ", lon=" + lon +
                    '}';
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

            boolean equal = elevation == refPos.elevation && lat == refPos.lat && lon == refPos.lon;
            if(!equal){
                System.out.println("RefPos not equal, this = ");
            }

            return equal;
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
        public String toString() {
            return "InLink{" +
                    "name='" + name + '\'' +
                    ", upstreamNodeId=" + upstreamNodeId +
                    ", movements=" + Arrays.toString(movements) +
                    ", speedLimits=" + Arrays.toString(speedLimits) +
                    ", linkWidth=" + linkWidth +
                    ", points=" + Arrays.toString(points) +
                    ", lanes=" + Arrays.toString(lanes) +
                    '}';
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
            boolean equal = linkWidth == inLink.linkWidth && Objects.equals(name, inLink.name) && Objects.equals(upstreamNodeId, inLink.upstreamNodeId) /*&& Arrays.equals(movements, inLink.movements) /* &&  Arrays.equals(speedLimits, inLink.speedLimits) */ /*&& Arrays.equals(points, inLink.points)*/ && Arrays.equals(lanes, inLink.lanes);

            if(!equal){
                System.out.println("InLink not equal, this = ");
            }

            return equal;
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(name, upstreamNodeId, linkWidth);
            result = 31 * result + Arrays.hashCode(movements);
            //result = 31 * result + Arrays.hashCode(speedLimits);
            //result = 31 * result + Arrays.hashCode(points);
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
        public String toString() {
            return "UpstreamNodeId{" +
                    "id=" + id +
                    ", region=" + region +
                    '}';
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

            boolean equal = id == that.id && region == that.region;
            if(!equal){
                System.out.println("Movement not equal, this = ");
            }

            return equal;

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
        public String toString() {
            return "Movement{" +
                    "phaseId=" + phaseId +
                    ", remoteIntersection=" + remoteIntersection +
                    '}';
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

            boolean equal = phaseId == movement.phaseId && Objects.equals(remoteIntersection, movement.remoteIntersection);
            if(!equal){
                System.out.println("Movement not equal, this = ");
            }

            return equal;

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
        public String toString() {
            return "RemoteIntersection{" +
                    "id=" + id +
                    ", region=" + region +
                    '}';
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

            boolean equal = id == that.id && region == that.region;
            if(!equal){
                System.out.println("RemoteIntersection not equal, this = ");
            }

            return equal;
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
        public String toString() {
            return "SpeedLimit{" +
                    "type='" + type + '\'' +
                    ", speed=" + speed +
                    '}';
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

            boolean equal = speed == that.speed && Objects.equals(type, that.type);
            if(!equal){
                System.out.println("SpeedLimit not equal, this = ");
            }

            return equal;
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
        public String toString() {
            return "Point{" +
                    "posOffset=" + posOffset +
                    '}';
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

            boolean equal = Objects.equals(posOffset, point.posOffset);
            if(!equal){
                System.out.println("Point not equal, this = ");
            }

            return equal;
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
        public String toString() {
            return "PosOffset{" +
                    "offsetLL=" + offsetLL +
                    ", offsetV=" + offsetV +
                    '}';
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

            boolean equal = Objects.equals(offsetLL, posOffset.offsetLL) && Objects.equals(offsetV, posOffset.offsetV);
            if(!equal){
                System.out.println("PosOffset not equal, this = ");
            }

            return equal;
        }

        @Override
        public int hashCode() {
            return Objects.hash(offsetLL, offsetV);
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class OffsetLL {

        @JsonProperty("position-LatLon")
        private PositionLatLon positionLatLon;

        public PositionLatLon getPositionLatLon() {
            return positionLatLon;
        }

        public void setPositionLatLon(PositionLatLon positionLatLon) {
            this.positionLatLon = positionLatLon;
        }

        @Override
        public String toString() {
            return "OffsetLL{" +
                    "positionLatLon=" + positionLatLon +
                    '}';
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

            boolean equal = Objects.equals(positionLatLon, offsetLL.positionLatLon);
            if(!equal){
                System.out.println("OffsetLL not equal, this = ");
            }

            return equal;

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
        public String toString() {
            return "PositionLatLon{" +
                    "lon=" + lon +
                    ", lat=" + lat +
                    '}';
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

            boolean equal = lon == that.lon && lat == that.lat;
            if(!equal){
                System.out.println("PositionLatLon not equal, this = ");
            }

            return equal;

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
        public String toString() {
            return "OffsetV{" +
                    "elevation=" + elevation +
                    '}';
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

            boolean equal = elevation == offsetV.elevation;
            if(!equal){
                System.out.println("OffsetV not equal, this = ");
            }

            return equal;

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
        public String toString() {
            return "Lane{" +
                    "connectsTo=" + Arrays.toString(connectsTo) +
                    ", speedLimits=" + Arrays.toString(speedLimits) +
                    ", laneID=" + laneID +
                    ", laneWidth=" + laneWidth +
                    ", maneuvers='" + maneuvers + '\'' +
                    ", points=" + Arrays.toString(points) +
                    '}';
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

            boolean equal = laneID == lane.laneID /*&& laneWidth == lane.laneWidth*/ && Arrays.equals(connectsTo, lane.connectsTo) /*&& Arrays.equals(speedLimits, lane.speedLimits)*/
                    && (Objects.equals(maneuvers, lane.maneuvers) || (maneuvers.equals("") && lane.maneuvers.equals("0000")) || (maneuvers.equals("0000") && lane.maneuvers.equals("")))
                    /* && Arrays.equals(points, lane.points) */;
            if(!equal){
                System.out.println("Lane not equal, this = ");
            }

            return equal;
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(laneID);
            result = 31 * result + Arrays.hashCode(connectsTo);
            //result = 31 * result + Arrays.hashCode(speedLimits);
            //result = 31 * result + Arrays.hashCode(points);
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
        public String toString() {
            return "ConnectsTo{" +
                    "phaseId=" + phaseId +
                    ", remoteIntersection=" + remoteIntersection +
                    ", connectingLane=" + connectingLane +
                    '}';
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

            boolean equal = phaseId == that.phaseId && Objects.equals(remoteIntersection, that.remoteIntersection) && Objects.equals(connectingLane, that.connectingLane);
            if(!equal){
                System.out.println("ConnectsTo not equal, this = ");
            }

            return equal;
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
        public String toString() {
            return "ConnectingLane{" +
                    "lane=" + lane +
                    ", maneuver='" + maneuver + '\'' +
                    '}';
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof ConnectingLane)) {
                return false;
            }
            ConnectingLane that = (ConnectingLane) o;
            boolean equal = lane == that.lane
                    && (Objects.equals(maneuver, that.maneuver) || (maneuver.equals("") && that.maneuver.equals("0000")) || (maneuver.equals("0000") && that.maneuver.equals("")));

            if(!equal){
                System.out.println("ConnectingLane not equal, this = ");
            }
            return equal;
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
