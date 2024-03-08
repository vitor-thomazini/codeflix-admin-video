package vitor.thomazini.codeflixadminvideo.application.category.retrieve.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DefaultGetCategoryByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallGetCategory_thenShouldReturnCategory() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = category.id();

        when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.of(category));

        // Act
        final var actualCategory = useCase.execute(expectedId.value());

        // Assert
        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(category.updatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(category.createdAt(), actualCategory.createdAt());
        Assertions.assertEquals(category.deletedAt(), actualCategory.deletedAt());

        verify(categoryGateway, times(1)).findById(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallGetCategory_thenShouldReturnNotFound() {
        // Arrange
        final var expectedErrorMessage = "Category with ID 123 was not found";
        final var expectedId = CategoryId.from("123");

        when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());


        // Assert
        final var actualException = Assertions.assertThrows(NotFoundException.class, action);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(expectedId);
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        // Arrange
        final var expectedErrorMessage = "Gateway error";
        final var expectedId = CategoryId.from("123");

        when(categoryGateway.findById(expectedId))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        final var actualException = Assertions.assertThrows(IllegalStateException.class, action);

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(categoryGateway, times(1)).findById(expectedId);
    }
}