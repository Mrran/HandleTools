package io.emqx.mqtt.bean;

import java.util.Objects;

class PosOffset {
    private OffsetLL offsetLL;
    private OffsetV offsetV;

    public OffsetLL getOffsetLL() {
        return offsetLL;
    }

    public void setOffsetLL(OffsetLL offsetLL) {
        this.offsetLL = offsetLL;
    }

    public OffsetV getOffsetV() {
        return offsetV;
    }

    public void setOffsetV(OffsetV offsetV) {
        this.offsetV = offsetV;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PosOffset)) {
            return false;
        }
        PosOffset posOffset = (PosOffset) o;
        return Objects.equals(getOffsetLL(), posOffset.getOffsetLL()) && Objects.equals(getOffsetV(), posOffset.getOffsetV());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getOffsetLL(), getOffsetV());
    }
}