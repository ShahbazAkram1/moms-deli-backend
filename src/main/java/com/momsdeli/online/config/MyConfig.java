//package com.momsdeli.online.config;
//import lombok.Data;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//import org.springframework.beans.factory.annotation.Value;
//
//@Component
//@Data
//public class MyConfig {
//    private final DatabaseConfig databaseConfig;
//    @Autowired
//    public MyConfig(DatabaseConfig databaseConfig) {
//        this.databaseConfig = databaseConfig;
//    }
//
//    @Value("${DATABASE_URL}")
//    private String databaseUrl;
//
//    @Value("${DATABASE_USERNAME}")
//    private String databaseUsername;
//
//    public String getDatabasePassword() {
//        return databaseConfig.getDatabasePassword();
//    }
//
//    @Value("${STRIPE_SECRET_KEY}")
//    private String stripeSecretKey;
//
//}
