package io.emqx.mqtt.bean;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

public class RefPos {
    private int elevation;
    private long lat;

    @JsonProperty("long")
    private long longitude;

    // Getters and setters
    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLongitude() {
        return longitude;
    }

    public void setLongitude(long longitude) {
        this.longitude = longitude;
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
        return getElevation() == refPos.getElevation() && getLat() == refPos.getLat() && getLongitude() == refPos.getLongitude();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getElevation(), getLat(), getLongitude());
    }
}
