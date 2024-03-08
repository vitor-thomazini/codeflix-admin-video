package vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;

import java.util.List;

@IntegrationTest
class GetGenreByIdUseCaseIT {

    @Autowired
    private GetGenreByIdUseCase useCase;

    @Autowired
    private CategoryGateway categoryGateway;

    @Autowired
    private GenreGateway genreGateway;

    @Test
    void givenAValidId_whenCallsGetGenre_thenShouldReturnGenre() {
        // Arrange
        final var series = categoryGateway.create(
                Category.newCategory("Séries", null, true)
        );

        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(series.id(), movies.id());

        final var genre = genreGateway.create(
                Genre.newGenre(expectedName, expectedIsActive)
                        .addCategories(expectedCategories)
        );

        final var expectedId = genre.id();

        // Act
        final var actualGenre = useCase.execute(expectedId.value());

        // Assert
        Assertions.assertEquals(expectedId.value(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.categories().size()
                        && asString(expectedCategories).containsAll(actualGenre.categories())
        );
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.deletedAt(), actualGenre.deletedAt());
    }

    @Test
    void givenAValidId_whenCallsGetGenreAndDoesNotExists_thenShouldReturnNotFound() {
        // Arrange
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        final var expectedId = GenreId.from("123");

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        final var actualException = Assertions.assertThrows(NotFoundException.class, action);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private List<String> asString(final List<CategoryId> ids) {
        return ids.stream()
                .map(CategoryId::value)
                .toList();
    }
}