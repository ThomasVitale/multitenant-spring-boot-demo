package com.thomasvitale.instrumentservice.instrument.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@ConfigurationProperties(prefix = "instrument")
public record InstrumentTenantProperties (
        InstrumentTenantConfigData defaults,
        Map<String, InstrumentTenantConfigData> tenants
){}
