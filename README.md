# Spring Boot Multitenancy

_For a more structured example, check my other repo: https://github.com/ThomasVitale/spring-boot-multitenancy._

## Stack

* Java 21
* Spring Boot 3.2

## Usage

The Instrument Service application can be run as follows to rely on Testcontainers to spin up a PostgreSQL database:

```bash
./gradlew bootTestRun
```

You can directly call the Instrument Service on behalf of a tenant (`dukes` or `beans`), for example, using httpie.

```bash
# Get instruments
http :8181/instruments X-TenantId:dukes
# Add instrument
http :8181/instruments X-TenantId:dukes name="Pearl" type="drums"
```

The Edge Service application can be run as follows:

```bash
./gradlew bootRun
```

Two tenants are configured: `dukes` and `beans`. Ensure you add the following configuration to your `hosts` file to resolve tenants from DNS names.

```bash
127.0.0.1       dukes.rock
127.0.0.1       beans.rock
```

Now open the browser window and navigate to `http://dukes.rock/instruments/`. The result will be the list of instruments from the Dukes rock band.

Now open another browser window and navigate to `http://beans.rock/instruments/`. The result will be the list of instruments from the Beans rock band.
