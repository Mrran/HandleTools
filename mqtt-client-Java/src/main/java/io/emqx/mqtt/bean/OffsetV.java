package io.emqx.mqtt.bean;

import java.util.Objects;

class OffsetV {
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
        return getElevation() == offsetV.getElevation();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getElevation());
    }
}