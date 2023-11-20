package com.thomasvitale.instrumentservice.multitenancy.data.hibernate;

import com.thomasvitale.instrumentservice.multitenancy.config.TenantDataProperties;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

@Component
public class ConnectionProvider implements MultiTenantConnectionProvider, HibernatePropertiesCustomizer {

  	private final DataSource dataSource;
	private final TenantDataProperties tenantDataProperties;

	ConnectionProvider(DataSource dataSource, TenantDataProperties tenantDataProperties) {
		this.dataSource = dataSource;
		this.tenantDataProperties = tenantDataProperties;
	}

	@Override
	public Connection getAnyConnection() throws SQLException {
		return getConnection(tenantDataProperties.defaultSchema());
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String tenantIdentifier) throws SQLException {
		final Connection connection = dataSource.getConnection();
		connection.setSchema(tenantIdentifier);
		return connection;
	}

	@Override
	public void releaseConnection(String tenantIdentifier, Connection connection) throws SQLException {
    	connection.setSchema(tenantDataProperties.defaultSchema());
		connection.close();
  	}

	@Override
	public boolean supportsAggressiveRelease() {
		return true;
	}

	@Override
	public boolean isUnwrappableAs(Class<?> unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		throw new UnsupportedOperationException("Unimplemented method 'unwrap'.");
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
	}
  
}
