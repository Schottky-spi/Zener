package com.github.schottky.zener.util.version;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public class ReleaseCandidate implements Version {

    private final int releaseCandidateNumber;
    public int releaseCandidateNumber() { return releaseCandidateNumber; }

    private final Release release;

    public ReleaseCandidate(Release release, int releaseCandidateNumber) {
        this.releaseCandidateNumber = releaseCandidateNumber;
        this.release = release;
    }

    @Override
    public String toString() {
        return release + "-rc" + releaseCandidateNumber;
    }

    @Override
    public int compareToVersion(@NotNull Version other) {
        if (other.getClass() == Release.class) {
            return asRelease().compareToVersion(other);
        } else if (other.getClass() == this.getClass()) {
            ReleaseCandidate releaseCandidate = (ReleaseCandidate) other;
            return Comparator.comparing(ReleaseCandidate::asRelease)
                    .thenComparingInt(ReleaseCandidate::releaseCandidateNumber)
                    .compare(this, releaseCandidate);
        } else {
            return 0;
        }
    }

    @Override
    public Release asRelease() {
        return release;
    }
}
