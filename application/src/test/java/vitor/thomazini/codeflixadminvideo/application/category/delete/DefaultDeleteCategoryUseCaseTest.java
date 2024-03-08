package vitor.thomazini.codeflixadminvideo.application.category.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class DefaultDeleteCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    void givenAValidId_whenCallDeleteCategory_thenShouldBeOk() {
        // Arrange
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.id();

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        // Act
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.value()));

        // Assert
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAnInvalidId_whenCallDeleteCategory_thenShouldBeOk() {
        // Arrange
        final var expectedId = CategoryId.from("123");

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        // Act
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.value()));

        // Assert
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

    @Test
    void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        // Arrange
        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = category.id();
        final var expectedMessageError = "Gateway error";

        doThrow(new IllegalStateException(expectedMessageError)).when(categoryGateway).deleteById(eq(expectedId));

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());


        // Assert
        final var actualException = Assertions.assertThrows(IllegalStateException.class, action);

        Assertions.assertEquals(expectedMessageError, actualException.getMessage());

        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }
}