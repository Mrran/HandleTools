package io.emqx.mqtt.bean;

import java.util.Objects;

class Id {
    private int id;
    private int region;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRegion() {
        return region;
    }

    public void setRegion(int region) {
        this.region = region;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Id)) {
            return false;
        }
        Id id1 = (Id) o;
        return getId() == id1.getId() && getRegion() == id1.getRegion();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getRegion());
    }
}