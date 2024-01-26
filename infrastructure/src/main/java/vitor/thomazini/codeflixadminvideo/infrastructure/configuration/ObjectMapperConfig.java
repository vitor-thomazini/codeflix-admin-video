package vitor.thomazini.codeflixadminvideo.infrastructure.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vitor.thomazini.codeflixadminvideo.infrastructure.configuration.json.Json;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper() {
        return Json.mapper();
    }
}