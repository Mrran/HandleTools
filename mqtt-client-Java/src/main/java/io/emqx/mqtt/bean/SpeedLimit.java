package io.emqx.mqtt.bean;

import java.util.Objects;

class SpeedLimit {
    private String type;
    private int speed;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SpeedLimit)) {
            return false;
        }
        SpeedLimit that = (SpeedLimit) o;
        return getSpeed() == that.getSpeed() && Objects.equals(getType(), that.getType());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getType(), getSpeed());
    }
}