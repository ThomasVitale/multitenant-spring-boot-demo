package com.thomasvitale.instrumentservice.multitenancy.data.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.List;

@Component
public class TenantFlywayMigrationInitializer implements InitializingBean, Ordered {

    private static final String TENANT_MIGRATION_LOCATION = "db/migration/tenant";

    private final DataSource dataSource;
    private final Flyway defaultFlyway;

    public TenantFlywayMigrationInitializer(DataSource dataSource, Flyway defaultFlyway) {
        this.dataSource = dataSource;
        this.defaultFlyway = defaultFlyway;
    }

    @Override
    public void afterPropertiesSet() {
        TenantProvider.tenantList().forEach(tenant -> {
            Flyway tenantFlyway = tenantFlyway(tenant);
            tenantFlyway.repair();
            tenantFlyway.migrate();
        });
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
