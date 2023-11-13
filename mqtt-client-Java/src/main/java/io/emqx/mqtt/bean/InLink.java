package io.emqx.mqtt.bean;

import java.util.List;
import java.util.Objects;

class InLink {
    private String name;
    private Id upstreamNodeId;
    private List<Movement> movements;
    private List<SpeedLimit> speedLimits;
    private int linkWidth;
    private List<Point> points;
    private List<Lane> lanes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Id getUpstreamNodeId() {
        return upstreamNodeId;
    }

    public void setUpstreamNodeId(Id upstreamNodeId) {
        this.upstreamNodeId = upstreamNodeId;
    }

    public List<Movement> getMovements() {
        return movements;
    }

    public void setMovements(List<Movement> movements) {
        this.movements = movements;
    }

    public List<SpeedLimit> getSpeedLimits() {
        return speedLimits;
    }

    public void setSpeedLimits(List<SpeedLimit> speedLimits) {
        this.speedLimits = speedLimits;
    }

    public int getLinkWidth() {
        return linkWidth;
    }

    public void setLinkWidth(int linkWidth) {
        this.linkWidth = linkWidth;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Lane> getLanes() {
        return lanes;
    }

    public void setLanes(List<Lane> lanes) {
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
        return getLinkWidth() == inLink.getLinkWidth() && Objects.equals(getName(), inLink.getName()) && Objects.equals(getUpstreamNodeId(), inLink.getUpstreamNodeId()) && Objects.equals(getMovements(), inLink.getMovements()) && Objects.equals(getSpeedLimits(), inLink.getSpeedLimits()) && Objects.equals(getPoints(), inLink.getPoints()) && Objects.equals(getLanes(), inLink.getLanes());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getName(), getUpstreamNodeId(), getMovements(), getSpeedLimits(), getLinkWidth(), getPoints(), getLanes());
    }
}
