package com.zzang.chongdae.global.config;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class TestClock extends Clock {

    private Instant instant;
    private final ZoneId zone;

    public TestClock(Instant instant, ZoneId zone) {
        this.instant = instant;
        this.zone = zone;
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zone) {
        return new TestClock(this.instant, zone);
    }

    @Override
    public Instant instant() {
        return this.instant;
    }

    public void plusDays(long days) {
        this.instant = this.instant.plus(days, ChronoUnit.DAYS);
    }
}
