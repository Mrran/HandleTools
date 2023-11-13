package io.emqx.mqtt.bean;

import io.emqx.mqtt.inter.ICompare;

import java.util.List;

public class Node implements ICompare {

    private String name;
    private Id id;
    private RefPos refPos;
    private List<InLink> inLinks;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public RefPos getRefPos() {
        return refPos;
    }

    public void setRefPos(RefPos refPos) {
        this.refPos = refPos;
    }

    public List<InLink> getInLinks() {
        return inLinks;
    }

    public void setInLinks(List<InLink> inLinks) {
        this.inLinks = inLinks;
    }

    @Override
    public String compareAllFile() {




        return null;
    }
}
