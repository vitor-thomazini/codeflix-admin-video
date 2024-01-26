package vitor.thomazini.codeflixadminvideo.infrastructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.AbstractEnvironment;
import vitor.thomazini.codeflixadminvideo.infrastructure.configuration.WebServerConfig;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {
		System.setProperty(AbstractEnvironment.DEFAULT_PROFILES_PROPERTY_NAME, "development");
		SpringApplication.run(WebServerConfig.class, args);
	}
}
