package org.example.configuration;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.PooledDataSourceFactory;
import javax.validation.Valid;

public class GeoLocationConfiguration extends Configuration {
    private @Valid PooledDataSourceFactory dataSourceFactory = new DataSourceFactory();

    public GeoLocationConfiguration() {
    }

    @JsonProperty("database")
    public PooledDataSourceFactory getDataSourceFactory() {
        return this.dataSourceFactory;
    }

    @JsonProperty("database")
    public void setDataSourceFactory(DataSourceFactory dataSourceFactory) {
        this.dataSourceFactory = dataSourceFactory;
    }

}
