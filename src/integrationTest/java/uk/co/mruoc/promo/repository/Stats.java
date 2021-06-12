package uk.co.mruoc.promo.repository;

import com.google.common.util.concurrent.AtomicDouble;
import lombok.ToString;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@ToString
public class Stats {

    private final AtomicLong count = new AtomicLong();
    private final AtomicDouble total = new AtomicDouble();
    private final AtomicLong max = new AtomicLong();
    private final List<Long> values = Collections.synchronizedList(new ArrayList<>());

    public String getMessage() {
        return String.format("\n\ttotal\t %f\n" +
                        "\taverage\t %fms\n" +
                        "\t99th\t %dms\n" +
                        "\t95th\t %dms\n" +
                        "\tmax\t\t %dms",
                total.get(),
                getAverage(),
                calculatePercentile(0.99),
                calculatePercentile(0.95),
                getMax());
    }

    public void update(Duration duration) {
        update(duration.toMillis());
    }

    public void update(long value) {
        count.incrementAndGet();
        total.addAndGet(value);
        values.add(value);
        if (value > max.get()) {
            max.set(value);
        }
    }

    public long calculatePercentile(double value) {
        Collections.sort(values);
        int index = (int)(count.get() * value);
        return values.get(index);
    }

    public double getAverage() {
        return total.get() / count.get();
    }

    public long getMax() {
        return max.get();
    }

}
