package com.github.schottky.zener.util.version;

public class PreRelease {

    private final int preReleaseNumber;
    private final Release release;

    public PreRelease(Release release, int preReleaseNumber) {
        this.preReleaseNumber = preReleaseNumber;
        this.release = release;
    }

    @Override
    public String toString() {
        return release + "-pre" + preReleaseNumber;
    }
}
