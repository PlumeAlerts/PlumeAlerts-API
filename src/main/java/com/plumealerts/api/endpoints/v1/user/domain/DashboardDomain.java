package com.plumealerts.api.endpoints.v1.user.domain;

public class DashboardDomain {
    private String type;
    private Short x;
    private Short y;
    private Short width;
    private Short height;
    private boolean show;

    public DashboardDomain() {
    }

    public DashboardDomain(String type, Short x, Short y, Short width, Short height, boolean show) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.show = show;
    }

    public String getType() {
        return type;
    }

    public Short getX() {
        return x;
    }

    public Short getY() {
        return y;
    }

    public Short getWidth() {
        return width;
    }

    public Short getHeight() {
        return height;
    }

    public boolean isShow() {
        return show;
    }
}
