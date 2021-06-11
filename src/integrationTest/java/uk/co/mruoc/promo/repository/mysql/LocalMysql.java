package uk.co.mruoc.promo.repository.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;

public class LocalMysql {

    public DataSource getDataSource() {
        var config = new HikariConfig();
        config.setJdbcUrl("jdbc:mysql://localhost:3306/promo?useSSL=false");
        config.setUsername("root");
        config.setPassword("welcome");
        config.setMaximumPoolSize(100);
        return new HikariDataSource(config);
    }

}
