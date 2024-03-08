package vitor.thomazini.codeflixadminvideo.application.genre.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DefaultDeleteGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteGenreUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_thenShouldDeleteGenre() {
        // Arrange
        final var genre = Genre.newGenre("Ação", true);

        final var expectedId = genre.id();

        doNothing().when(genreGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertDoesNotThrow(action);

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
        // Arrange
        final var expectedId = GenreId.from("invalid-id");

        doNothing().when(genreGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertDoesNotThrow(action);

        verify(genreGateway, times(1)).deleteById(expectedId);
    }

    @Test
    void givenAValidGenreId_whenCallsDeleteGenreAndGatewayThrowsUnexpectedError_thenShouldReceiveException() {
        // Arrange
        final var genre = Genre.newGenre("Ação", true);

        final var expectedId = genre.id();

        doThrow(new IllegalStateException("Gateway error"))
                .when(genreGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertThrows(IllegalStateException.class, action);

        verify(genreGateway, times(1)).deleteById(expectedId);
    }
}
