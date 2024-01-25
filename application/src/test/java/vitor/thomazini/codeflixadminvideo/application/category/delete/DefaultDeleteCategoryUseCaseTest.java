package vitor.thomazini.codeflixadminvideo.application.category.delete;

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
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultDeleteCategoryUseCaseTest {

    @InjectMocks
    private DefaultDeleteCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;


    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallDeleteCategory_thenShouldBeOk() {
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
    public void givenAnInvalidId_whenCallDeleteCategory_thenShouldBeOk() {
        // Arrange
        final var expectedId = CategoryId.from("123");

        doNothing().when(categoryGateway).deleteById(eq(expectedId));

        // Act
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.value()));

        // Assert
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
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
        verify(categoryGateway, times(1)).deleteById(eq(expectedId));
    }

}