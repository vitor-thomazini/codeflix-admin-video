package vitor.thomazini.codeflixadminvideo;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test-integration")
@DataJpaTest
@ContextConfiguration(classes = MySQLGatewayTest.ContextConfiguration.class)
@ExtendWith(MySQLCleanUpExtension.class)
public @interface MySQLGatewayTest {

    @ComponentScan(
            basePackages = "vitor.thomazini.codeflixadminvideo",
            includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway"),
            }
    )
    class ContextConfiguration {
    }
}