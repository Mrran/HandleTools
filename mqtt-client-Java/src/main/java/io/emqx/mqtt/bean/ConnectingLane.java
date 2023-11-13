package io.emqx.mqtt.bean;

import java.util.Objects;

class ConnectingLane {
    private int lane;
    private String maneuver;

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
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ConnectingLane that = (ConnectingLane) o;
        return lane == that.lane && Objects.equals(maneuver, that.maneuver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lane, maneuver);
    }
}