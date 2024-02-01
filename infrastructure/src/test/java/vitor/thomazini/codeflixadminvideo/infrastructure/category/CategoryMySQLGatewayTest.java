package vitor.thomazini.codeflixadminvideo.infrastructure.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.MySQLGatewayTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryRepository;

import java.util.List;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAValidCategory_whenCallsCreate_thenShouldReturnANewCategory() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        final var actualCategory = categoryGateway.create(category);

        // Assert
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(category.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(category.deletedAt(), actualCategory.deletedAt());
        Assertions.assertNull(actualCategory.deletedAt());

        final var actualEntity = categoryRepository.findById(category.id().value()).get();
        Assertions.assertEquals(category.id().value(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(category.createdAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(category.updatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertEquals(category.deletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallsUpdate_thenShouldReturnACategoryUpdated() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Film", null, true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, categoryRepository.count());

        final var actualInvalidEntity = categoryRepository.findById(category.id().value()).get();
        Assertions.assertEquals("Film", actualInvalidEntity.getName());
        Assertions.assertNull(actualInvalidEntity.getDescription());
        Assertions.assertTrue(actualInvalidEntity.isActive());

        final var updatedCategory = Category.from(category)
                .update(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var actualCategory = categoryGateway.update(updatedCategory);

        // Assert
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(updatedCategory.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(updatedCategory.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(updatedCategory.updatedAt(), actualCategory.updatedAt());
        Assertions.assertTrue(category.updatedAt().isBefore(actualCategory.updatedAt()));
        Assertions.assertEquals(updatedCategory.deletedAt(), actualCategory.deletedAt());
        Assertions.assertNull(actualCategory.deletedAt());

        final var actualEntity = categoryRepository.findById(category.id().value()).get();
        Assertions.assertEquals(category.id().value(), actualEntity.getId());
        Assertions.assertEquals(expectedName, actualEntity.getName());
        Assertions.assertEquals(expectedDescription, actualEntity.getDescription());
        Assertions.assertEquals(expectedIsActive, actualEntity.isActive());
        Assertions.assertEquals(updatedCategory.createdAt(), actualEntity.getCreatedAt());
        Assertions.assertEquals(updatedCategory.updatedAt(), actualEntity.getUpdatedAt());
        Assertions.assertTrue(category.updatedAt().isBefore(actualCategory.updatedAt()));
        Assertions.assertEquals(updatedCategory.deletedAt(), actualEntity.getDeletedAt());
        Assertions.assertNull(actualEntity.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenTryDeleteIt_thenShouldDeleteCategory() {
        // Arrange
        final var category = Category.newCategory("Filmes", null, true);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, categoryRepository.count());

        // Act
        categoryGateway.deleteById(category.id());

        // Assert
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidCategoryId_whenTryDeleteIt_thenShouldDeleteCategory() {
        // Arrange
        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        categoryGateway.deleteById(CategoryId.from("invalid"));

        // Assert
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAPrePersistedCategoryAndValidCategoryId_whenCallsFindById_thenShouldReturnCategory() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        Assertions.assertEquals(1, categoryRepository.count());

        // Act
        final var actualCategory = categoryGateway.findById(category.id()).get();

        // Assert
        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(category.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(category.deletedAt(), actualCategory.deletedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAValidCategoryIdNotStored_whenCallsFindById_thenShouldReturnEmpty() {
        // Arrange
        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        final var actualCategory = categoryGateway.findById(CategoryId.from("empty"));

        // Assert
        Assertions.assertTrue(actualCategory.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_thenShouldReturnPaginated() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentaries = Category.newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");

        // Act
        final var actualResult = categoryGateway.findAll(query);

        // Assert
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentaries.id(), actualResult.items().get(0).id());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_thenShouldReturnEmptyPage() {
        // Arrange
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        Assertions.assertEquals(0, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");

        // Act
        final var actualResult = categoryGateway.findAll(query);

        // Assert
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedTotal, actualResult.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_thenShouldReturnPaginated() {
        // Arrange 0
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var movies = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentaries = Category.newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        // Act 0
        var query = new SearchQuery(0, 1, "", "name", "asc");
        var actualResult = categoryGateway.findAll(query);

        // Assert 0
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentaries.id(), actualResult.items().get(0).id());

        // Arrange 1
        expectedPage += 1;
        query = new SearchQuery(1, 1, "", "name", "asc");

        // Act 1

        actualResult = categoryGateway.findAll(query);

        // Assert 1
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies.id(), actualResult.items().get(0).id());

        // Arrange 2
        expectedPage += 1;
        query = new SearchQuery(2, 1, "", "name", "asc");

        // Act 2

        actualResult = categoryGateway.findAll(query);

        // Assert 2
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(series.id(), actualResult.items().get(0).id());

    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAllAndTermsMatchesCategoryName_thenShouldReturnPaginated() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Filmes", null, true);
        final var series = Category.newCategory("Séries", null, true);
        final var documentaries = Category.newCategory("Documentários", null, true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "doc", "name", "asc");

        // Act
        final var actualResult = categoryGateway.findAll(query);

        // Assert
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(documentaries.id(), actualResult.items().get(0).id());
    }

    @Test
    public void givenPrePersistedCategoriesAndMaisAssistidaAsTerms_whenCallsFindAllAndTermsMatchesCategoryDescription_thenShouldReturnPaginated() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var movies = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
        final var documentaries = Category.newCategory("Documentários", "A categoria menos assistida", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "MAIS ASSISTIDA", "name", "asc");

        // Act
        final var actualResult = categoryGateway.findAll(query);

        // Assert
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedPerPage, actualResult.items().size());
        Assertions.assertEquals(movies.id(), actualResult.items().get(0).id());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
        // Arrange
        final var movies = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var series = Category.newCategory("Séries", "Uma categoria assistida", true);
        final var documentaries = Category.newCategory("Documentários", "A categoria menos assistida", true);

        Assertions.assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
                CategoryJpaEntity.from(movies),
                CategoryJpaEntity.from(series),
                CategoryJpaEntity.from(documentaries)
        ));

        Assertions.assertEquals(3, categoryRepository.count());

        final var expectedIds = List.of(movies.id(), series.id());

        final var ids = List.of(movies.id(), series.id(), CategoryId.from("123"));

        // Act
        final var actualResult = categoryGateway.existsByIds(ids);

        // Assert
        Assertions.assertTrue(
                expectedIds.size() == actualResult.size() &&
                        expectedIds.containsAll(actualResult)
        );
    }
}