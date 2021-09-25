package com.unknown.xg42.utils;

public final class Timer {
    private long time = -1L;
    private long current = System.currentTimeMillis();

    public boolean passed(long ms) {
        return ((System.currentTimeMillis() - this.time) >= (ms));
    }

    public Timer reset() {
        this.time = System.currentTimeMillis();
        return null;
    }

    public boolean hasReached(long var1) {
        return System.currentTimeMillis() - this.current >= (var1);
    }

    public boolean hasReached(long var1, boolean var3) {
        if (var3) {
            this.reset();
        }
        return System.currentTimeMillis() - this.current >= (var1);
    }

    public boolean passedS(double s) {
        return passedMs((long)s * 1000L);
    }

    public boolean passedDms(double dms) {
        return passedMs((long)dms * 10L);
    }

    public boolean passedDs(double ds) {
        return passedMs((long)ds * 100L);
    }

    public boolean passedMs(long ms) {
        return System.currentTimeMillis() - this.time >= ms;
    }

    public long timePassed(final long n) {
        return System.currentTimeMillis() - n;
    }

    public long getPassedTimeMs() {
        return System.currentTimeMillis() - (this.time);
    }
}

