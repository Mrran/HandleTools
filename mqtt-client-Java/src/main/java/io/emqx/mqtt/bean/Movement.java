package io.emqx.mqtt.bean;

import java.util.Objects;

class Movement {
    private int phaseId;
    private Id remoteIntersection;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Movement)) {
            return false;
        }
        Movement movement = (Movement) o;
        return getPhaseId() == movement.getPhaseId() && Objects.equals(getRemoteIntersection(), movement.getRemoteIntersection());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPhaseId(), getRemoteIntersection());
    }
}