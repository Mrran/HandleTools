package io.emqx.mqtt.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

class OffsetLL {
    @JsonProperty("position-LatLon")
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
        return Objects.equals(getPositionLatLon(), offsetLL.getPositionLatLon());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPositionLatLon());
    }
}
