package uk.co.mruoc.promo.repository.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;

import javax.sql.DataSource;
import java.time.Duration;
import java.util.concurrent.Callable;

import static org.awaitility.Awaitility.await;

@Slf4j
public class LocalDockerMysql extends GenericContainer<LocalDockerMysql> {

    private static final String USERNAME = "promo-user";
    private static final String PASSWORD = "welcome01";
    private static final String DATABASE = "promo-local";
    private static final int PORT = 3306;

    public LocalDockerMysql() {
        super("mysql");
        withEnv("MYSQL_USER", USERNAME);
        withEnv("MYSQL_PASSWORD", PASSWORD);
        withEnv("MYSQL_ROOT_PASSWORD", PASSWORD);
        withEnv("MYSQL_DATABASE", DATABASE);
        withExposedPorts(PORT);
        withLogConsumer(this::logInfo);
    }

    public DataSource getDataSource() {
        log.info("waiting for mysql to start...");
        waitForStartupToComplete();
        log.info("mysql startup complete");

        String url = getUrl();

        Flyway.configure()
                .locations("db/migrations")
                .dataSource(url, "root", PASSWORD)
                .load()
                .migrate();

        var config = new HikariConfig();
        config.setJdbcUrl(getUrl());
        config.setUsername(USERNAME);
        config.setPassword(PASSWORD);
        config.setMaximumPoolSize(100);
        return new HikariDataSource(config);
    }

    private void waitForStartupToComplete() {
        await().atMost(Duration.ofSeconds(60))
                .pollDelay(Duration.ofSeconds(20))
                .pollInterval(Duration.ofMillis(500))
                .until(containerIsRunning());
    }

    private String getUrl() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(PORT);
        return String.format("jdbc:mysql://%s:%d/%s?useSSL=false&allowPublicKeyRetrieval=true", ip, port, DATABASE);
    }

    private void logInfo(OutputFrame frame) {
        log.info(frame.getUtf8String());
    }

    private Callable<Boolean> containerIsRunning() {
        return () -> {
            boolean running = this.isRunning();
            log.info("checking mongo container is running {}", running);
            return running;
        };
    }

}
