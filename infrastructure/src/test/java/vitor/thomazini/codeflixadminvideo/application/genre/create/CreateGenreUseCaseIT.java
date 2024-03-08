package vitor.thomazini.codeflixadminvideo.application.genre.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreRepository;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@IntegrationTest
class CreateGenreUseCaseIT {

    @Autowired
    private CreateGenreUseCase useCase;

    @SpyBean
    private CategoryGateway categoryGateway;

    @SpyBean
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.id());

        final var command = CreateGenreCommand.with(
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIds())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithoutCategories_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var command = CreateGenreCommand.with(
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIds())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryId>of();

        final var command = CreateGenreCommand.with(
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualGenre = genreRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertTrue(
                expectedCategories.size() == actualGenre.getCategoriesIds().size()
                        && expectedCategories.containsAll(actualGenre.getCategoriesIds())
        );
        Assertions.assertNotNull(actualGenre.getCreatedAt());
        Assertions.assertNotNull(actualGenre.getUpdatedAt());
        Assertions.assertNotNull(actualGenre.getDeletedAt());
    }

    @Test
   void givenAInvalidEmptyName_whenCallsCreateGenre_thenShouldReturnDomainException() {
        // Arrange
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var command = CreateGenreCommand.with(
                expectedName, 
                expectedIsActive, 
                asString(expectedCategories)
        );

        // Act
        final Executable action = () -> useCase.execute(command);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAInvalidNullName_whenCallsCreateGenre_thenShouldReturnDomainException() {
        // Arrange
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateGenreCommand.with(
                expectedName, 
                expectedIsActive, 
                asString(expectedCategories)
        );

        // Act
        final Executable action = () -> useCase.execute(command);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    void givenAInvalidName_whenCallsCreateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnDomainException() {
        // Arrange
        final var series = categoryGateway.create(
                Category.newCategory("Séries", null, true)
        );

        final var movies = CategoryId.from("456");
        final var documentaries = CategoryId.from("789");

        final var expectName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series.id(), documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var command = CreateGenreCommand.with(
                expectName,
                expectedIsActive,
                asString(expectedCategories)
        );

        // Act
        final Executable action = () -> useCase.execute(command);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors().get(1).message());

        verify(categoryGateway, times(1)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    private List<String> asString(final List<CategoryId> categories) {
        return categories.stream()
                .map(CategoryId::value)
                .toList();
    }
}