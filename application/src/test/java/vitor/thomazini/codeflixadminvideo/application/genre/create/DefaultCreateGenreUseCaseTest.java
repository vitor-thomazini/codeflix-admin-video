package vitor.thomazini.codeflixadminvideo.application.genre.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DefaultCreateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway, genreGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(genreGateway, times(1)).create(argThat(genre ->
                Objects.equals(expectedName, genre.name()) &&
                        Objects.equals(expectedIsActive, genre.isActive()) &&
                        Objects.equals(expectedCategories, genre.categories()) &&
                        Objects.nonNull(genre.id()) &&
                        Objects.nonNull(genre.createdAt()) &&
                        Objects.nonNull(genre.updatedAt()) &&
                        Objects.isNull(genre.deletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryId.from("123"),
                CategoryId.from("456")
        );

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(expectedCategories);

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);

        verify(genreGateway, times(1)).create(argThat(genre ->
                Objects.equals(expectedName, genre.name()) &&
                        Objects.equals(expectedIsActive, genre.isActive()) &&
                        Objects.equals(expectedCategories, genre.categories()) &&
                        Objects.nonNull(genre.id()) &&
                        Objects.nonNull(genre.createdAt()) &&
                        Objects.nonNull(genre.updatedAt()) &&
                        Objects.isNull(genre.deletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryId>of();

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(genreGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(command);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(genreGateway, times(1)).create(argThat(genre ->
                Objects.equals(expectedName, genre.name()) &&
                        Objects.equals(expectedIsActive, genre.isActive()) &&
                        Objects.equals(expectedCategories, genre.categories()) &&
                        Objects.nonNull(genre.id()) &&
                        Objects.nonNull(genre.createdAt()) &&
                        Objects.nonNull(genre.updatedAt()) &&
                        Objects.nonNull(genre.deletedAt())
        ));
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallsCreateGenre_thenShouldReturnDomainException() {
        // Arrange
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidNullName_whenCallsCreateGenre_thenShouldReturnDomainException() {
        // Arrange
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(categoryGateway, times(0)).existsByIds(any());
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenreAndSomeCategoriesDoesExists_thenShouldReturnDomainException() {
        // Arrange
        final var movies = CategoryId.from("123");
        final var series = CategoryId.from("456");
        final var documentaries = CategoryId.from("789");

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies,series, documentaries);

        final var expectedErrorMessage = "Some categories could not be found: 123, 789";
        final var expectedErrorCount = 1;

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(0)).create(any());
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateGenreAndSomeCategoriesDoesExists_thenShouldReturnDomainException() {
        // Arrange
        final var movies = CategoryId.from("123");
        final var series = CategoryId.from("456");
        final var documentaries = CategoryId.from("789");

        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies,series, documentaries);

        final var expectedErrorMessageOne = "Some categories could not be found: 123, 789";
        final var expectedErrorMessageTwo = "'name' should not be empty";
        final var expectedErrorCount = 2;

        final var command =
                CreateGenreCommand.with(expectedName, expectedIsActive, asString(expectedCategories));

        when(categoryGateway.existsByIds(any()))
                .thenReturn(List.of(series));

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            useCase.execute(command);
        });

        // Assert
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.errors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.errors().get(1).message());

        verify(categoryGateway, times(1)).existsByIds(expectedCategories);
        verify(genreGateway, times(0)).create(any());
    }

    public List<String> asString(final List<CategoryId> categories) {
        return categories.stream()
                .map(CategoryId::value)
                .toList();
    }
}
