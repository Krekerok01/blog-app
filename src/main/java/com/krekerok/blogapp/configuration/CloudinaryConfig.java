package com.krekerok.blogapp.configuration;

import com.cloudinary.Cloudinary;
import java.util.HashMap;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    private final String CLOUD_NAME = "dor96itdg";
    private final String API_KEY = "259373515337985";
    private final String API_SECRET = "zE8udXENFpVLUSQtAl0kWKOcx5U";

    @Bean
    public Cloudinary cloudinary(){
        Map<String, String> config = new HashMap<>();
        config.put("cloud_name",CLOUD_NAME);
        config.put("api_key",API_KEY);
        config.put("api_secret",API_SECRET);

        return new Cloudinary(config);
    }
}