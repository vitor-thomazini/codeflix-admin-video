package vitor.thomazini.codeflixadminvideo.infrastructure.genre.models;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.json.JacksonTester;
import vitor.thomazini.codeflixadminvideo.JacksonTest;

import java.time.Instant;
import java.util.List;

@JacksonTest
class GenreResponseTest {

    @Autowired
    private JacksonTester<GenreResponse> json;

    @Test
    void testMarshall() throws Exception {
        // Arrange
        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedCategories = List.of("123");
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var response = new GenreResponse(
                expectedId,
                expectedName,
                expectedCategories,
                expectedIsActive,
                expectedCreatedAt,
                expectedUpdatedAt,
                expectedDeletedAt
        );

        // Act
        final var actualJson = this.json.write(response);

        // Assert
        Assertions.assertThat(actualJson)
                .hasJsonPathValue("$.id", expectedId)
                .hasJsonPathValue("$.name", expectedName)
                .hasJsonPathValue("$.categories_id", expectedCategories)
                .hasJsonPathValue("$.is_active", expectedIsActive)
                .hasJsonPathValue("$.created_at", expectedCreatedAt.toString())
                .hasJsonPathValue("$.deleted_at", expectedDeletedAt.toString())
                .hasJsonPathValue("$.updated_at", expectedUpdatedAt.toString());
    }

    @Test
    void testUnmarshall() throws Exception {
        // Arrange
        final var expectedId = "123";
        final var expectedName = "Ação";
        final var expectedCategory = "456";
        final var expectedIsActive = false;
        final var expectedCreatedAt = Instant.now();
        final var expectedUpdatedAt = Instant.now();
        final var expectedDeletedAt = Instant.now();

        final var json = """
                {
                  "id": "%s",
                  "name": "%s",
                  "categories_id": ["%s"],
                  "is_active": %s,
                  "created_at": "%s",
                  "deleted_at": "%s",
                  "updated_at": "%s"
                }    
                """.formatted(
                expectedId,
                expectedName,
                expectedCategory,
                expectedIsActive,
                expectedCreatedAt.toString(),
                expectedDeletedAt.toString(),
                expectedUpdatedAt.toString()
        );

        // Act
        final var actualJson = this.json.parse(json);

        // Assert
        Assertions.assertThat(actualJson)
                .hasFieldOrPropertyWithValue("id", expectedId)
                .hasFieldOrPropertyWithValue("name", expectedName)
                .hasFieldOrPropertyWithValue("categories", List.of(expectedCategory))
                .hasFieldOrPropertyWithValue("active", expectedIsActive)
                .hasFieldOrPropertyWithValue("createdAt", expectedCreatedAt)
                .hasFieldOrPropertyWithValue("deletedAt", expectedDeletedAt)
                .hasFieldOrPropertyWithValue("updatedAt", expectedUpdatedAt);
    }
}