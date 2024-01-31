package com.thomasvitale.instrumentservice.multitenancy.data.flyway;

import com.thomasvitale.instrumentservice.MyProperties;
import com.thomasvitale.instrumentservice.Tenant;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TenantFlywayMigrationInitializer implements InitializingBean, Ordered {

    private static final String TENANT_MIGRATION_LOCATION = "db/migration/tenant";

    private final DataSource dataSource;
    private final Flyway defaultFlyway;
    private final MyProperties myProperties;
    private final JdbcClient jdbcClient;

    public TenantFlywayMigrationInitializer(DataSource dataSource, Flyway defaultFlyway, MyProperties myProperties, JdbcClient jdbcClient) {
        this.dataSource = dataSource;
        this.defaultFlyway = defaultFlyway;
        this.myProperties = myProperties;
        this.jdbcClient = jdbcClient;
    }

    @Override
    public void afterPropertiesSet() {
        System.out.println("---");
        System.out.println(myProperties.getTenants().stream().map(Tenant::getIdentifier).collect(Collectors.joining(",")));
        System.out.println("---");

        myProperties.getTenants().forEach(tenant -> {
            Flyway tenantFlyway = tenantFlyway(tenant.getSchema());
            tenantFlyway.repair();
            tenantFlyway.migrate();
        });

        TenantProvider.tenantList().forEach(tenant -> {
            Flyway tenantFlyway = tenantFlyway(tenant);
            tenantFlyway.repair();
            tenantFlyway.migrate();
        });

        System.out.println("--- SCHEMAS");
        jdbcClient.sql("select * from information_schema.schemata")
                .query()
                .listOfRows()
                .forEach(row -> System.out.println(row.entrySet()));
        System.out.println("---");
    }

    @Override
    public int getOrder() {
        // Executed after the default schema initialization in FlywayMigrationInitializer.
        return 1;
    }

    private Flyway tenantFlyway(String tenant) {
        return Flyway.configure()
                .configuration(defaultFlyway.getConfiguration())
                .locations(TENANT_MIGRATION_LOCATION)
                .dataSource(dataSource)
                .schemas(tenant)
                .load();
    }
}

class TenantProvider {
    static List<String> tenantList() {
        return List.of("beans", "dukes");
    }
}
