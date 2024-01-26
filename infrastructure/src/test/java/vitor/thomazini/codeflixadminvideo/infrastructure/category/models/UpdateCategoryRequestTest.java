package vitor.thomazini.codeflixadminvideo.infrastructure.category.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import vitor.thomazini.codeflixadminvideo.JacksonTest;

import java.io.IOException;

@JacksonTest
public class UpdateCategoryRequestTest {

    @Autowired
    private JacksonTester<UpdateCategoryRequest> json;

    @Test
    public void testMarshall() throws IOException {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var response = new UpdateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        // Act
        final var actualJson = this.json.write(response);

        // Assert
        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.description", expectedDescription)
                .hasJsonPathValue("$.is_active", expectedIsActive);
    }

    @Test
    public void testUnmarshall() throws IOException {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var json = """
            {
                "name": "%s",
                "description": "%s",
                "is_active": %s
            }
        """.formatted(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var actualJson = this.json.parse(json);

        // Assert
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("description", expectedDescription)
                .hasFieldOrPropertyWithValue("active", expectedIsActive);
    }
}