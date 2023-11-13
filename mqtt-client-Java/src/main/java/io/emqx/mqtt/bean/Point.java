package io.emqx.mqtt.bean;

import java.util.Objects;

class Point {
    private PosOffset posOffset;

    public PosOffset getPosOffset() {
        return posOffset;
    }

    public void setPosOffset(PosOffset posOffset) {
        this.posOffset = posOffset;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Point)) {
            return false;
        }
        Point point = (Point) o;
        return Objects.equals(getPosOffset(), point.getPosOffset());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPosOffset());
    }
}