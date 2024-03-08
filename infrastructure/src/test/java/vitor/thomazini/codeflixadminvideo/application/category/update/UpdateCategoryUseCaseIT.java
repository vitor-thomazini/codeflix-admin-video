package vitor.thomazini.codeflixadminvideo.application.category.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.application.category.update.UpdateCategoryCommand;
import vitor.thomazini.codeflixadminvideo.application.category.update.UpdateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryRepository;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@IntegrationTest
class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    void givenAValidCommand_whenCallUpdateCategory_thenShouldReturnCategoryId() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

        save(category);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.id();

        final var command = UpdateCategoryCommand.with(
                expectedId.value(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        // Act
        final var actualOutput = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.createdAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(category.updatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAnInvalidName_whenCallUpdateCategory_thenShouldReturnDomainException() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

        save(category);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.id();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.value(), expectedName, expectedDescription, expectedIsActive);

        Assertions.assertEquals(1, categoryRepository.count());

        // Act
        final var notification = this.useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(this.categoryGateway, times(0)).update(any());
    }

    @Test
    void givenAValidCommandWithInactiveCategory_whenCallUpdateCategory_thenShouldReturnInactiveCategoryId() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

        save(category);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = category.id();

        final var command = UpdateCategoryCommand.with(
                expectedId.value(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.deletedAt());

        // Act
        final var actualOutput = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory = categoryRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.createdAt(), actualCategory.getCreatedAt());
        Assertions.assertTrue(category.updatedAt().isBefore(actualCategory.getUpdatedAt()));
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnAException() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

        save(category);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.id();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(
                expectedId.value(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        // Act
        final var notification = this.useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory = categoryRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(category.name(), actualCategory.getName());
        Assertions.assertEquals(category.description(), actualCategory.getDescription());
        Assertions.assertEquals(category.isActive(), actualCategory.isActive());
        Assertions.assertEquals(category.createdAt(), actualCategory.getCreatedAt());
        Assertions.assertEquals(category.updatedAt(), actualCategory.getUpdatedAt());
        Assertions.assertEquals(category.deletedAt(), actualCategory.getDeletedAt());
    }

    @Test
    void givenACommandWithInvalidID_whenCallUpdateCategory_thenShouldReturnInactiveCategoryId() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = "123";

        final var command = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        // Act
        final Executable action = () -> useCase.execute(command);

        // Assert
        final var actualException = Assertions.assertThrows(NotFoundException.class, action);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... categories) {
        final var categoriesList = Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList();
        categoryRepository.saveAllAndFlush(categoriesList);
    }
}