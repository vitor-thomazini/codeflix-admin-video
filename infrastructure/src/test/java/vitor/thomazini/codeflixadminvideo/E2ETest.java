package vitor.thomazini.codeflixadminvideo;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import vitor.thomazini.codeflixadminvideo.infrastructure.configuration.WebServerConfig;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-e2e")
@SpringBootTest(classes = WebServerConfig.class)
@AutoConfigureMockMvc
@ExtendWith(MySQLCleanUpExtension.class)
public @interface E2ETest {
}