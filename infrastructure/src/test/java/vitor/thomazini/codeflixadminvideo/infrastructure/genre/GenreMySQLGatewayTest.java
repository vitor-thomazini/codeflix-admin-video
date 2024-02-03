package vitor.thomazini.codeflixadminvideo.infrastructure.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.MySQLGatewayTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.CategoryMySQLGateway;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreCategoryJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence.GenreRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Set;

@MySQLGatewayTest
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_thenShouldPersistGenre() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.id());

        final var genre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = genre.id();

        Assertions.assertEquals(0, genreRepository.count());

        // Act
        final var actualGenre = genreGateway.create(genre);

        // Assert
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.deletedAt(), actualGenre.deletedAt());
        Assertions.assertNull(actualGenre.deletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoriesIds());
        Assertions.assertEquals(genre.createdAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(genre.updatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(genre.deletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_thenShouldPersistGenre() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var genre = Genre.newGenre(expectedName, expectedIsActive);

        final var expectedId = genre.id();

        Assertions.assertEquals(0, genreRepository.count());

        // Act
        final var actualGenre = genreGateway.create(genre);

        // Assert
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(genre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(genre.deletedAt(), actualGenre.deletedAt());
        Assertions.assertNull(actualGenre.deletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoriesIds());
        Assertions.assertEquals(genre.createdAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(genre.updatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertEquals(genre.deletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenreWithCategories_thenShouldPersistGenre() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var series = categoryGateway.create(
                Category.newCategory("Séries", null, true)
        );

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.id(), series.id());

        final var genre = Genre.newGenre("acao", expectedIsActive);

        final var expectedId = genre.id();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals("acao", genre.name());
        Assertions.assertEquals(0, genre.categories().size());

        final var updatedGenre = Genre.from(genre).update(expectedName, expectedIsActive, expectedCategories);

        // Act
        final var actualGenre = genreGateway.update(updatedGenre);

        // Assert
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.categories()));
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.updatedAt()));
        Assertions.assertEquals(genre.deletedAt(), actualGenre.deletedAt());
        Assertions.assertNull(actualGenre.deletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(persistedGenre.getCategoriesIds()));
        Assertions.assertEquals(genre.createdAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.deletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsCreateGenreCleaningCategories_thenShouldPersistGenre() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var series = categoryGateway.create(
                Category.newCategory("Séries", null, true)
        );

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var genre = Genre.newGenre("acao", expectedIsActive)
                .addCategories(List.of(movies.id(), series.id()));

        final var expectedId = genre.id();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals("acao", genre.name());
        Assertions.assertEquals(2, genre.categories().size());

        final var updatedGenre = Genre.from(genre).update(expectedName, expectedIsActive, expectedCategories);

        // Act
        final var actualGenre = genreGateway.update(updatedGenre);

        // Assert
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.updatedAt()));
        Assertions.assertEquals(genre.deletedAt(), actualGenre.deletedAt());
        Assertions.assertNull(actualGenre.deletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoriesIds());
        Assertions.assertEquals(genre.createdAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertEquals(genre.deletedAt(), persistedGenre.getDeletedAt());
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreInactive_whenCallsCreateGenreActivating_thenShouldPersistGenre() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryId>of();

        final var genre = Genre.newGenre(expectedName, false);

        final var expectedId = genre.id();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertFalse(genre.isActive());
        Assertions.assertNotNull(genre.deletedAt());

        final var updatedGenre = Genre.from(genre).update(expectedName, expectedIsActive, expectedCategories);

        // Act
        final var actualGenre = genreGateway.update(updatedGenre);

        // Assert
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.updatedAt()));
        Assertions.assertNull(actualGenre.deletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoriesIds());
        Assertions.assertEquals(genre.createdAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreActive_whenCallsCreateGenreInactivating_thenShouldPersistGenre() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryId>of();

        final var genre = Genre.newGenre(expectedName, true);

        final var expectedId = genre.id();

        Assertions.assertEquals(0, genreRepository.count());

        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertTrue(genre.isActive());
        Assertions.assertNull(genre.deletedAt());

        final var updatedGenre = Genre.from(genre).update(expectedName, expectedIsActive, expectedCategories);

        // Act
        final var actualGenre = genreGateway.update(updatedGenre);

        // Assert
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(genre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.deletedAt());

        final var persistedGenre = genreRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, persistedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persistedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persistedGenre.getCategoriesIds());
        Assertions.assertEquals(genre.createdAt(), persistedGenre.getCreatedAt());
        Assertions.assertEquals(updatedGenre.updatedAt(), persistedGenre.getUpdatedAt());
        Assertions.assertTrue(genre.updatedAt().isBefore(persistedGenre.getUpdatedAt()));
        Assertions.assertNotNull(persistedGenre.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_thenShouldDeleteGenre() {
        // Arrange
        final var genre = Genre.newGenre("Ação", true);
        genreRepository.saveAndFlush(GenreJpaEntity.from(genre));

        Assertions.assertEquals(1, genreRepository.count());

        // Act
        genreGateway.deleteById(genre.id());

        // Assert
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_thenShouldDeleteGenre() {
        // Arrange
        Assertions.assertEquals(0, genreRepository.count());

        // Act
        genreGateway.deleteById(GenreId.from("123"));

        // Assert
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_thenShouldReturnGenre() {
        // Arrange
        final var movies = categoryGateway.create(
                Category.newCategory("Filmes", null, true)
        );

        final var series = categoryGateway.create(
                Category.newCategory("Séries", null, true)
        );

        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(movies.id(), series.id());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive)
                .addCategories(expectedCategories);

        final var expectedId = aGenre.id();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));

        Assertions.assertEquals(1, genreRepository.count());

        // Act
        final var actualGenre = genreGateway.findById(expectedId).get();

        // Assert
        Assertions.assertEquals(expectedId, actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.categories()));
        Assertions.assertEquals(aGenre.createdAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.updatedAt(), actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAInvalidGenreId_whenCallsFindById_thenShouldReturnEmpty() {
        // Arrange
        final var expectedId = GenreId.from("123");

        Assertions.assertEquals(0, genreRepository.count());

        // Act
        final var actualGenre = genreGateway.findById(expectedId);

        // Assert
        Assertions.assertTrue(actualGenre.isEmpty());
    }

    // 00

    @Test
    public void givenEmptyGenres_whenCallFindAll_shouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "aç,Ação",
            "dr,Drama",
            "com,Comédia romântica",
            "cien,Ficção científica",
            "terr,Terror",
    })
    public void givenAValidTerm_whenCallsFindAll_thenShouldReturnFiltered(
            final String expectedTerms,
            final String expectedGenreName
    ) {
        // Arrange
        mockGenres();

        final int expectedPage = 0;
        final int expectedPerPage = 10;
        final int expectedItemsCount = 1;
        final long expectedTotal = 1;
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualPage = genreGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().getFirst().name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,Ação",
            "name,desc,Terror",
            "createdAt,asc,Comédia romântica",
            "createdAt,desc,Ficção científica",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnOrdered(
            final String expectedSort,
            final String expectedDirection,
            final String expectedGenreName
    ) {
        // Arrange
        mockGenres();

        final int expectedPage = 0;
        final int expectedPerPage = 1;
        final int expectedItemsCount = 1;
        final long expectedTotal = 5;
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualPage = genreGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().getFirst().name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,Ação;Comédia romântica",
            "1,2,Drama;Ficção científica",
            "2,1,Terror",
    })
    public void givenAValidPaging_whenCallsFindAll_thenShouldReturnPaged(
            final int expectedPage,
            final int expectedItemsCount,
            final String expectedGenres
    ) {
        // Arrange
        mockGenres();

        final int expectedPerPage = 2;
        final long expectedTotal = 5;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualPage = genreGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedGenres.split(";")) {
            final var actualName = actualPage.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index += 1;
        }
    }

    private void mockGenres() {
        genreRepository.saveAllAndFlush(List.of(
                GenreJpaEntity.from(Genre.newGenre("Comédia romântica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção científica", true))
        ));
    }

    private List<CategoryId> sorted(final List<CategoryId> categories) {
        return categories.stream()
                .sorted(Comparator.comparing(CategoryId::value))
                .toList();
    }
}
