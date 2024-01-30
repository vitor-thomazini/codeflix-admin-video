package vitor.thomazini.codeflixadminvideo.application.category.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultUpdateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidCommand_whenCallUpdateCategory_thenShouldReturnCategoryId() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

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

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.from(category)));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(updatedCategory ->
                Objects.equals(expectedName, updatedCategory.name())
                        && Objects.equals(expectedDescription, updatedCategory.description())
                        && Objects.equals(expectedIsActive, updatedCategory.isActive())
                        && Objects.equals(expectedId, updatedCategory.id())
                        && Objects.equals(category.createdAt(), updatedCategory.createdAt())
                        && category.updatedAt().isBefore(updatedCategory.updatedAt())
                        && Objects.isNull(updatedCategory.deletedAt())
        ));
    }

    @Test
    void givenAnInvalidName_whenCallUpdateCategory_thenShouldReturnDomainException() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = category.id();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = UpdateCategoryCommand.with(expectedId.value(), expectedName, expectedDescription, expectedIsActive);

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.from(category)));

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

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.from(category)));
        when(categoryGateway.update(any())).thenAnswer(returnsFirstArg());

        // Pre-Assert
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.deletedAt());

        // Act
        final var actualOutput = useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(updatedCategory ->
                Objects.equals(expectedName, updatedCategory.name())
                        && Objects.equals(expectedDescription, updatedCategory.description())
                        && Objects.equals(expectedIsActive, updatedCategory.isActive())
                        && Objects.equals(expectedId, updatedCategory.id())
                        && Objects.equals(category.createdAt(), updatedCategory.createdAt())
                        && category.updatedAt().isBefore(updatedCategory.updatedAt())
                        && Objects.nonNull(updatedCategory.deletedAt())
        ));
    }

    @Test
    void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnAException() {
        // Arrange
        final var category = Category.newCategory("Film", null, true);

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

        when(categoryGateway.findById(eq(expectedId)))
                .thenReturn(Optional.of(Category.from(category)));
        when(this.categoryGateway.update(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // Act
        final var notification = this.useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
        verify(categoryGateway, times(1)).update(argThat(updatedCategory ->
                Objects.equals(expectedName, updatedCategory.name())
                        && Objects.equals(expectedDescription, updatedCategory.description())
                        && Objects.equals(expectedIsActive, updatedCategory.isActive())
                        && Objects.equals(expectedId, updatedCategory.id())
                        && Objects.equals(category.createdAt(), updatedCategory.createdAt())
                        && category.updatedAt().isBefore(updatedCategory.updatedAt())
                        && Objects.isNull(updatedCategory.deletedAt())
        ));
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

        when(categoryGateway.findById(eq(CategoryId.from(expectedId))))
                .thenReturn(Optional.empty());

        // Act
        final var actualException = Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(command));

        // Assert
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(eq(CategoryId.from(expectedId)));
        verify(categoryGateway, times(0)).update(any());
    }
}