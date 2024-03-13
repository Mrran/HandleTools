package io.emqx.mqtt;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class JSONComparer {
    public static void main(String[] args) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            //List<JSONStructure> json1 = mapper.readValue(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\test.json"), new TypeReference<List<JSONStructure>>() {});
            //List<JSONStructure> json2 = mapper.readValue(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\test2.json"), new TypeReference<List<JSONStructure>>() {});

            List<Node> jsonList1 = mapper.readValue(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\upload\\lianchuang_20231226_1430.json"), new TypeReference<List<Node>>() {});
            List<Node> jsonList2 = mapper.readValue(new File("D:\\work\\doc\\business\\rtty\\sqlite\\export-json\\lianchuang_20240124_0926.json"), new TypeReference<List<Node>>() {});

            compareJSON(jsonList1, jsonList2);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void compareJSON(List<Node> jsonList1, List<Node> jsonList2) {
        for (Node item1 : jsonList1) {
            for (Node item2 : jsonList2) {
                if (item1.name.equals(item2.name)) {
                    compareInLinks(item1.inLinks, item2.inLinks);
                }
            }
        }
    }

    private static void compareInLinks(List<InLink> inLinks1, List<InLink> inLinks2) {
        for (InLink link1 : inLinks1) {
            for (InLink link2 : inLinks2) {
                if (link1.name.equals(link2.name) && !link1.movements.equals(link2.movements)) {
                    System.out.println("Movements mismatch in InLink: " + link1.name);
                }
            }
        }
    }

    static class Node {
        public String name;
        public List<InLink> inLinks;
    }

    static class InLink {
        public String name;
        public List<Movement> movements;
    }

    static class Movement {
        public int phaseId;
        public RemoteIntersection remoteIntersection;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
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

    static class RemoteIntersection {
        public int id;
        public int region;

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
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
}
