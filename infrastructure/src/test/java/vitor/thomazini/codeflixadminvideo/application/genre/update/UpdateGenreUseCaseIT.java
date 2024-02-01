package vitor.thomazini.codeflixadminvideo.application.genre.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
public class UpdateGenreUseCaseIT {

    @Autowired
    private UpdateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var genre = genreGateway.create(
                Genre.newGenre("acao", true)
        );

        final var expectedId = genre.id();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        final var actualGenre = genreRepository.findById(genre.id().value()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIds())
        );
        Assertions.assertEquals(genre.createdAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var series = categoryGateway.create(
                Category.newCategory("Séries", null, true)
        );

        final var genre = genreGateway.create(
                Genre.newGenre("acao", true)
        );

        final var expectedId = genre.id();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.id(), series.id());

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        final var actualGenre = genreRepository.findById(genre.id().value()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIds())
        );
        Assertions.assertEquals(genre.createdAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var genre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = genre.id();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryId>of();

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.deletedAt());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        final var actualGenre = genreRepository.findById(genre.id().value()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIds())
        );
        Assertions.assertEquals(genre.createdAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenre_thenShouldReturnNotificationException() {
        // Arrange
        final var genre = genreGateway.create(Genre.newGenre("acao", true));

        final var expectedId = genre.id();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().get(0).message());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).update(any());
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnNotificationException() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filems", null, true)
        );
        final var series = CategoryId.from("456");
        final var documentaries = CategoryId.from("789");

        final var genre = genreGateway.create(
                Genre.newGenre("acao", true)
        );

        final var expectedId = genre.id();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.id(), series, documentaries);

        final var expectedErrorCount = 2;
        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors().get(1).message());

        verify(genreGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).existsByIds(eq(expectedCategories));
        verify(genreGateway, times(0)).update(any());
    }

    private List<String> asString(final List<CategoryId> ids) {
        return ids.stream()
                .map(CategoryId::value)
                .toList();
    }
}
