package vitor.thomazini.codeflixadminvideo.application.genre.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreRepository;

@IntegrationTest
class DeleteGenreUseCaseIT {

    @Autowired
    private DeleteGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    void givenAValidGenreId_whenCallsDeleteGenre_thenShouldDeleteGenre() {
        // Arrange
        final var genre = genreGateway.create(
                Genre.newGenre("Ação", true)
        );

        final var expectedId = genre.id();

        Assertions.assertEquals(1, genreRepository.count());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertDoesNotThrow(action);
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    void givenAnInvalidGenreId_whenCallsDeleteGenre_thenShouldBeOk() {
        // Arrange
        genreGateway.create(
                Genre.newGenre("Ação", true)
        );

        final var expectedId = GenreId.from("123");

        Assertions.assertEquals(1, genreRepository.count());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertDoesNotThrow(action);
        Assertions.assertEquals(1, genreRepository.count());
    }
}