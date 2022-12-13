package com.mapofzones.zoneparametercrawler.common.properties.multitheads;

public class Mutex {

    private boolean isAvailable = true;

    public boolean isAvailable() {
        return this.isAvailable;
    }

    public void block() {
        this.isAvailable = false;
    }

    public void release() {
        this.isAvailable = true;
    }
}
