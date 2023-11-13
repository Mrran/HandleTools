package io.emqx.mqtt.bean;

import java.util.Objects;

class PositionLatLon {
    private long lon;
    private long lat;

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
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
        return getLon() == that.getLon() && getLat() == that.getLat();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getLon(), getLat());
    }
}
