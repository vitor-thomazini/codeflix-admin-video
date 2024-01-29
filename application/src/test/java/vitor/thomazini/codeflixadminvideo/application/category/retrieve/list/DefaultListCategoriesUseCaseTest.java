package vitor.thomazini.codeflixadminvideo.application.category.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class DefaultListCategoriesUseCaseTest {

    @InjectMocks
    private DefaultListCategoriesUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidQuery_whenCallListCategories_thenShouldReturnCategories() {
        // Arrange
        final var categories = List.of(
                Category.newCategory("Filmes", null, true),
                Category.newCategory("Series", null, true)
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedItemsCount = 2;
        final var expectedResults = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        // Act
        final var actualResult = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResults, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenHasNoResults_thenShouldReturnEmptyCategories() {
        // Arrange
        final var categories = List.<Category>of();

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);
        final var expectedPagination = new Pagination<>(expectedPage, expectedPerPage, categories.size(), categories);
        final var expectedItemsCount = 0;
        final var expectedResults = expectedPagination.map(CategoryListOutput::from);

        when(categoryGateway.findAll(eq(query))).thenReturn(expectedPagination);

        // Act
        final var actualResult = useCase.execute(query);

        // Assert
        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedResults, actualResult);
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(categories.size(), actualResult.total());
    }

    @Test
    public void givenAValidQuery_whenGatewayThrowsException_thenShouldReturnException() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";
        final var query = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        when(categoryGateway.findAll(eq(query)))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        // Act
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(query));

        // Assert
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }
}