package vitor.thomazini.codeflixadminvideo.application.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.application.category.delete.DeleteCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryRepository;

import java.util.Arrays;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.eq;

@IntegrationTest
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallDeleteCategory_thenShouldBeOk() {
        // Arrange
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.id();

        save(category);

        Assertions.assertEquals(1, categoryRepository.count());

        // Act
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.value()));

        // Assert
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallDeleteCategory_thenShouldBeOk() {
        // Arrange
        final var expectedId = CategoryId.from("123");

        Assertions.assertEquals(0, categoryRepository.count());

        // Act
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.value()));

        // Assert
        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        // Arrange
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.id();
        final var expectedMessageError = "Gateway error";

        doThrow(new IllegalStateException(expectedMessageError)).when(categoryGateway).deleteById(eq(expectedId));

        // Act
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.value()));

        // Assert
        Assertions.assertEquals(expectedMessageError, actualException.getMessage());
    }

    private void save(final Category... categories) {
        final var categoriesList = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();
        categoryRepository.saveAllAndFlush(categoriesList);
    }
}