package vitor.thomazini.codeflixadminvideo.application.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.list.ListCategoriesUseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryRepository;

import java.util.stream.Stream;

@IntegrationTest
public class ListCategoryUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void mockUp() {
        final var categories = Stream.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Netflix Originals", "Títulos de autoria da Netflix", true),
                Category.newCategory("Amazon Originals", "Títulos de autoria da Amazon Prime", true),
                Category.newCategory("Documentários",null, true),
                Category.newCategory("Sports", null, true),
                Category.newCategory("Kids", "Categoria para crianças", true),
                Category.newCategory("Series", null, true)
        ).map(CategoryJpaEntity::from).toList();
        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerm_whenTermDoesNotMatchesPrePersisted_thenShouldReturnEmptyPage() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "adsasdas dadas";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedItemsCount = 0;
        final var expectedTotal = 0;

        final var query = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualResult = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
            "fil,0,10,1,1,Filmes",
            "net,0,10,1,1,Netflix Originals",
            "ZON,0,10,1,1,Amazon Originals",
            "Ki,0,10,1,1,Kids",
            "criANças,0,10,1,1,Kids",
            "da Amazon,0,10,1,1,Amazon Originals",
    })
    public void givenAValidTerm_whenCallsListCategories_thenShouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        // Arrange
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var query = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualResult = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Amazon Originals",
            "name,desc,0,10,7,7,Sports",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Series"
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesOrdered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        // Arrange
        final var expectedTerms = "";

        final var query = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualResult = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Amazon Originals;Documentários",
            "1,2,2,7,Filmes;Kids",
            "2,2,2,7,Netflix Originals;Series",
            "3,2,1,7,Sports",
    })
    public void givenAValidPage_whenCallsListCategories_thenShouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {
        // Arrange
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTerms = "";

        final var query = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        // Act
        final var actualResult = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        int index = 0;
        for (String expectedName : expectedCategoriesName.split(";")) {
            final var actualName = actualResult.items().get(index).name();
            Assertions.assertEquals(expectedName, actualName);
            index += 1;
        }
    }
}