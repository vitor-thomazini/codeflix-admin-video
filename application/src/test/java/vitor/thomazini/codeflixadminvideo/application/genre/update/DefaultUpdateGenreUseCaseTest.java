package vitor.thomazini.codeflixadminvideo.application.genre.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultUpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var genre = Genre.newGenre("acao", true);

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

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.from(genre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(genreGateway, times(1)).update(argThat(updatedGenre ->
                Objects.equals(expectedId.value(), updatedGenre.id().value()) &&
                        Objects.equals(expectedName, updatedGenre.name()) &&
                        Objects.equals(expectedIsActive, updatedGenre.isActive()) &&
                        Objects.equals(expectedCategories, updatedGenre.categories()) &&
                        Objects.equals(genre.createdAt(), updatedGenre.createdAt()) &&
                        genre.updatedAt().isBefore(updatedGenre.updatedAt()) &&
                        Objects.isNull(updatedGenre.deletedAt())
        ));
    }

    @Test
    void givenAValidCommandWithCategories_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var movies = CategoryId.from("123");
        final var series = CategoryId.from("456");

        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.id();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series);

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.from(genre)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(1)).update(argThat(updatedGenre ->
                Objects.equals(expectedId.value(), updatedGenre.id().value()) &&
                        Objects.equals(expectedName, updatedGenre.name()) &&
                        Objects.equals(expectedIsActive, updatedGenre.isActive()) &&
                        Objects.equals(expectedCategories, updatedGenre.categories()) &&
                        Objects.equals(genre.createdAt(), updatedGenre.createdAt()) &&
                        genre.updatedAt().isBefore(updatedGenre.updatedAt()) &&
                        Objects.isNull(updatedGenre.deletedAt())
        ));
    }

    @Test
    void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var genre = Genre.newGenre("acao", true);

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

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.from(genre)));

        when(genreGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.deletedAt());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(genreGateway, times(1)).update(argThat(updatedGenre ->
                Objects.equals(expectedId.value(), updatedGenre.id().value()) &&
                        Objects.equals(expectedName, updatedGenre.name()) &&
                        Objects.equals(expectedIsActive, updatedGenre.isActive()) &&
                        Objects.equals(expectedCategories, updatedGenre.categories()) &&
                        Objects.equals(genre.createdAt(), updatedGenre.createdAt()) &&
                        genre.updatedAt().isBefore(updatedGenre.updatedAt()) &&
                        Objects.nonNull(updatedGenre.deletedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenre_thenShouldReturnNotificationException() {
        // Arrange
        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.id();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.from(genre)));

        // Act
        final Executable action = () -> useCase.execute(command);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).update(any());
    }

    @Test
    void givenAnInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnNotificationException() {
        // Arrange
        final var movies = CategoryId.from("123");
        final var series = CategoryId.from("456");
        final var documentaries = CategoryId.from("789");

        final var genre = Genre.newGenre("acao", true);

        final var expectedId = genre.id();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies, series, documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var command = UpdateGenreCommand.with(
                expectedId.value(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(Genre.from(genre)));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(movies));

        // Act
        final Executable action = () -> useCase.execute(command);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors().get(1).message());

        verify(genreGateway, times(1)).findById(expectedId);
        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(0)).update(any());
    }
}
