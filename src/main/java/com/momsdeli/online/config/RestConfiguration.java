package com.momsdeli.online.config;

import com.momsdeli.online.model.Product;
import com.momsdeli.online.model.ProductCategory;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class RestConfiguration implements RepositoryRestConfigurer {

    // should give the id #s to ProductCategory and Product databases
    // from Postgres serial IDs
    @Override
    public void configureRepositoryRestConfiguration(
            RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(ProductCategory.class, Product.class);
    }
}