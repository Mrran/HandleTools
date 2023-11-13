package io.emqx.mqtt.bean;

import java.util.Objects;

class ConnectsTo {
    private int phaseId;
    private Id remoteIntersection;
    private ConnectingLane connectingLane;

    public int getPhaseId() {
        return phaseId;
    }

    public void setPhaseId(int phaseId) {
        this.phaseId = phaseId;
    }

    public Id getRemoteIntersection() {
        return remoteIntersection;
    }

    public void setRemoteIntersection(Id remoteIntersection) {
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
        return getPhaseId() == that.getPhaseId() && Objects.equals(getRemoteIntersection(), that.getRemoteIntersection()) && Objects.equals(getConnectingLane(), that.getConnectingLane());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhaseId(), getRemoteIntersection(), getConnectingLane());
    }
}
