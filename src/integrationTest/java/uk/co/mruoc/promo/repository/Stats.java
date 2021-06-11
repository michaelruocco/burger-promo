package uk.co.mruoc.promo.repository;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.ToString;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicLong;

@ToString
public class Stats {

    private final AtomicLong count = new AtomicLong();
    private final AtomicDouble total = new AtomicDouble();
    private final AtomicLong max = new AtomicLong();

    public void update(Duration duration) {
        update(duration.toMillis());
    }

    public void update(long value) {
        count.incrementAndGet();
        total.addAndGet(value);
        if (value > max.get()) {
            max.set(value);
        }
    }

    public double getAverage() {
        return total.get() / count.get();
    }

    public long getMax() {
        return max.get();
    }

}
