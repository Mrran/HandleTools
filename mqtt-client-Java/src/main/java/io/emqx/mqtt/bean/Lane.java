package io.emqx.mqtt.bean;

import java.util.List;
import java.util.Objects;

class Lane {
    private List<ConnectsTo> connectsTo;
    private List<SpeedLimit> speedLimits;
    private int laneID;
    private int laneWidth;
    private String maneuvers;
    private List<Point> points;

    public List<ConnectsTo> getConnectsTo() {
        return connectsTo;
    }

    public void setConnectsTo(List<ConnectsTo> connectsTo) {
        this.connectsTo = connectsTo;
    }

    public List<SpeedLimit> getSpeedLimits() {
        return speedLimits;
    }

    public void setSpeedLimits(List<SpeedLimit> speedLimits) {
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

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
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
        return getLaneID() == lane.getLaneID() && getLaneWidth() == lane.getLaneWidth() && Objects.equals(getConnectsTo(), lane.getConnectsTo()) && Objects.equals(getSpeedLimits(), lane.getSpeedLimits()) && Objects.equals(getManeuvers(), lane.getManeuvers()) && Objects.equals(getPoints(), lane.getPoints());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getConnectsTo(), getSpeedLimits(), getLaneID(), getLaneWidth(), getManeuvers(), getPoints());
    }
}