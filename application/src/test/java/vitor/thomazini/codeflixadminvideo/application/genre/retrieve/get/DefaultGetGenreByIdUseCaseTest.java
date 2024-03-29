package vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DefaultGetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidId_whenCallsGetGenre_shouldReturnGenre() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryId.from("123"),
                CategoryId.from("456")
        );

        final var genre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = genre.id();

        when(genreGateway.findById(any()))
                .thenReturn(Optional.of(genre));

        // Act
        final var actualGenre = useCase.execute(expectedId.value());

        // Assert
        Assertions.assertEquals(expectedId.value(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.deletedAt(), actualGenre.deletedAt());

        verify(genreGateway, times(1)).findById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsGetGenreAndDoesNotExists_thenShouldReturnNotFound() {
        // Arrange
        final var expectedMessage = "Genre with ID 123 was not found";
        final var expectedId = GenreId.from("123");

        when(genreGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        final var actualException = Assertions.assertThrows(NotFoundException.class, action);

        Assertions.assertEquals(expectedMessage, actualException.getMessage());
    }
}
