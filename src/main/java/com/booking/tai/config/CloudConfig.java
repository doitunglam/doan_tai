package com.booking.tai.config;

import com.cloudinary.Cloudinary;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
@SpringBootApplication
public class CloudConfig {
    @Bean
    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", "cloudapivnua123");
        config.put("api_key", "371274747248841");
        config.put("api_secret", "gKDWuubZm0-CAKiq59jq1qc14fU");
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
