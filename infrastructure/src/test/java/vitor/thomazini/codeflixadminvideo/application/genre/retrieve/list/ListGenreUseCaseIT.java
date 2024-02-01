package vitor.thomazini.codeflixadminvideo.application.genre.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreRepository;

import java.util.List;

@IntegrationTest
public class ListGenreUseCaseIT {

    @Autowired
    private ListGenreUseCase useCase;

    @Autowired
    private GenreGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidQuery_whenCallsListGenre_thenShouldReturnGenres() {
        // Arrange
        final var genres = List.of(
                Genre.newGenre("Ação", true),
                Genre.newGenre("Aventura", true)
        );

        genreRepository.saveAllAndFlush(
                genres.stream()
                        .map(GenreJpaEntity::from)
                        .toList()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var query = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualOutput = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertTrue(
                expectedItems.size() == actualOutput.items().size()
                        && expectedItems.containsAll(actualOutput.items())
        );
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_thenShouldReturnGenres() {
        // Arrange
        final var genres = List.<Genre>of();

        genreRepository.saveAllAndFlush(
                genres.stream()
                        .map(GenreJpaEntity::from)
                        .toList()
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var expectedItems = List.<GenreListOutput>of();

        final var query = new SearchQuery(
                expectedPage, 
                expectedPerPage, 
                expectedTerms, 
                expectedSort, 
                expectedDirection
        );

        // Act
        final var actualOutput = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());
    }
}