package com.thomasvitale.instrumentservice.instrument.config;

import com.thomasvitale.instrumentservice.multitenancy.properties.TenantPropertyResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    TenantPropertyResolver<InstrumentTenantConfigData> tenantPropertyResolver(InstrumentTenantProperties tenantProperties) {
        return new TenantPropertyResolver<>(tenantProperties::tenants, tenantProperties::defaults);
    }
}
