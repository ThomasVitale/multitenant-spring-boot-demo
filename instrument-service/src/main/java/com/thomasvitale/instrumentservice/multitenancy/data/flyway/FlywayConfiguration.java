package com.thomasvitale.instrumentservice.multitenancy.data.flyway;

import com.thomasvitale.instrumentservice.multitenancy.config.TenantDataProperties;
import org.springframework.boot.autoconfigure.flyway.FlywayConfigurationCustomizer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {

    private static final String DEFAULT_MIGRATION_LOCATION = "db/migration/default";
    private final TenantDataProperties tenantDataProperties;

    public FlywayConfiguration(TenantDataProperties tenantDataProperties) {
        this.tenantDataProperties = tenantDataProperties;
    }

    @Bean
    FlywayConfigurationCustomizer flywayConfigurationCustomizer() {
        return configuration -> {
            configuration
                    .locations(DEFAULT_MIGRATION_LOCATION)
                    .schemas(tenantDataProperties.defaultSchema());
        };
    }

    @Bean
    FlywayMigrationStrategy flywayMigrationStrategy() {
        return flyway -> {
            flyway.repair(); // example of customization
            flyway.migrate();
        };
    }

}
