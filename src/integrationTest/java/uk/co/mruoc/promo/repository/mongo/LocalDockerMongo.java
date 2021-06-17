package uk.co.mruoc.promo.repository.mongo;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import lombok.extern.slf4j.Slf4j;
import org.bson.codecs.configuration.CodecRegistry;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.output.OutputFrame;

import java.time.Duration;
import java.util.concurrent.Callable;

import static org.awaitility.Awaitility.await;
import static org.testcontainers.utility.MountableFile.forHostPath;

@Slf4j
public class LocalDockerMongo extends GenericContainer<LocalDockerMongo> {

    private static final String USERNAME = "promo-user";
    private static final String PASSWORD = "welcome01";
    private static final String DATABASE = "promo-local";
    private static final int PORT = 27017;

    public LocalDockerMongo() {
        super("mongo");
        withEnv("MONGO_INITDB_ROOT_USERNAME", USERNAME);
        withEnv("MONGO_INITDB_ROOT_PASSWORD", PASSWORD);
        withEnv("MONGO_INITDB_DATABASE", DATABASE);
        withCopyFileToContainer(forHostPath("mongo/mongo-init.js"), "/docker-entrypoint-initdb.d/mongo-init.js");
        withExposedPorts(PORT);
        withLogConsumer(this::logInfo);
    }

    public MongoDatabase getDatabase(CodecRegistry codecRegistry) {
        log.info("waiting for mongo to start...");
        waitForStartupToComplete();
        log.info("mongo startup complete");
        var settings = MongoClientSettings.builder()
                .applyConnectionString(getConnectionString())
                .codecRegistry(codecRegistry)
                .build();
        var client = MongoClients.create(settings);
        return client.getDatabase(DATABASE);
    }

    private void waitForStartupToComplete() {
        await().atMost(Duration.ofSeconds(150))
                .pollDelay(Duration.ofSeconds(120))
                .pollInterval(Duration.ofMillis(500))
                .until(containerIsRunning());
    }

    private ConnectionString getConnectionString() {
        String ip = getContainerIpAddress();
        int port = getMappedPort(PORT);
        return new ConnectionString(String.format("mongodb://%s:%s@%s:%d/%s", USERNAME, PASSWORD, ip, port, DATABASE));
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
