package vitor.thomazini.codeflixadminvideo.infrastructure.genre.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import vitor.thomazini.codeflixadminvideo.JacksonTest;

import java.util.List;

@JacksonTest
class UpdateGenreRequestTest {

    @Autowired
    private JacksonTester<UpdateGenreRequest> json;

    @Test
    void testUnmarshall() throws Exception {
        // Arrange
        final var expectedName = "Ação";
        final var expectedCategory = "123";
        final var expectedIsActive = true;

        final var json = """
        {
          "name": "%s",
          "categories_id": ["%s"],
          "is_active": %s
        }    
        """.formatted(expectedName, expectedCategory, expectedIsActive);

        // Act
        final var actualJson = this.json.parse(json);

        // Assert
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}