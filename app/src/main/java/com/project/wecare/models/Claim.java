package com.project.wecare.models;

public class Claim {
    private String name;
    private Integer src;

    public Claim(String name, Integer src) {
        this.name = name;
        this.src = src;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSrc() {
        return src;
    }

    public void setSrc(Integer src) {
        this.src = src;
    }
}
