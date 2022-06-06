package org.example;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.output.Slf4jLogConsumer;

import java.sql.*;

public class AppTest {
    private static Logger LOG = LoggerFactory.getLogger(AppTest.class);
    public static final String DATABASE_NAME = "test-db";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "sa";
    public static GenericContainer postgreSQLContainer = new PostgreSQLContainer("postgres:14.3")
            .withInitScript("init.sql")
            .withDatabaseName(DATABASE_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD)
            .withLogConsumer(new Slf4jLogConsumer(LOG));

    @Test
    public void test() {
        postgreSQLContainer.start();
        doTest();
        postgreSQLContainer.stop();
    }

    private void doTest() {
        String url = "jdbc:tc:postgresql:14.3:///" + DATABASE_NAME;
        try (Connection con = DriverManager.getConnection(url, USERNAME, PASSWORD);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM accounts")) {
            if (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException ex) {
            LOG.error("Error", ex);
        }
    }

}
