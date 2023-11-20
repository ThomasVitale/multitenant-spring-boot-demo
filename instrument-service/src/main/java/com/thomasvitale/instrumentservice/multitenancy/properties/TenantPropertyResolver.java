package com.thomasvitale.instrumentservice.multitenancy.properties;

import com.thomasvitale.instrumentservice.multitenancy.context.TenantContext;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class TenantPropertyResolver<T> {

    private final Supplier<Map<String,T>> tenantPropertiesExtractor;
    private final Supplier<T> defaultPropertiesExtractor;

    public TenantPropertyResolver(Supplier<Map<String, T>> tenantPropertiesExtractor, Supplier<T> defaultPropertiesExtractor) {
        this.tenantPropertiesExtractor = tenantPropertiesExtractor;
        this.defaultPropertiesExtractor = defaultPropertiesExtractor;
    }

    public <C> C resolve(Function<T,C> propertyValueExtractor) {
        var tenant = TenantContext.getTenantId();
        var tenantConfig = Objects.requireNonNullElse(tenantPropertiesExtractor.get().get(tenant), defaultPropertiesExtractor.get());
        return Stream.ofNullable(tenantConfig)
                .map(propertyValueExtractor)
                .filter(Objects::nonNull)
                .findFirst()
                .orElseGet(() -> propertyValueExtractor.apply(defaultPropertiesExtractor.get()));
    }
}
