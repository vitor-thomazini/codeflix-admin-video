package vitor.thomazini.codeflixadminvideo.application.category.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import vitor.thomazini.codeflixadminvideo.application.category.create.CreateCategoryCommand;
import vitor.thomazini.codeflixadminvideo.application.category.create.DefaultCreateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;

import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DefaultCreateCategoryUseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGateway.create(any()))
                .then(returnsFirstArg());

        // Act
        final var actualOutput = this.useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(Mockito.argThat(category ->
                Objects.equals(expectedName, category.name())
                        && Objects.equals(expectedDescription, category.description())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.id())
                        && Objects.nonNull(category.createdAt())
                        && Objects.nonNull(category.updatedAt())
                        && Objects.isNull(category.deletedAt())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        // Arrange
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var notification = this.useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(this.categoryGateway, times(0)).create(any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_thenShouldReturnInactiveCategoryId() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGateway.create(any())).then(returnsFirstArg());

        // Act
        final var actualOutput = this.useCase.execute(command).get();

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(categoryGateway, times(1)).create(Mockito.argThat(category ->
                Objects.equals(expectedName, category.name())
                        && Objects.equals(expectedDescription, category.description())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.id())
                        && Objects.nonNull(category.createdAt())
                        && Objects.nonNull(category.updatedAt())
                        && Objects.nonNull(category.deletedAt())
        ));
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_thenShouldReturnAException() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var command = CreateCategoryCommand.with(expectedName, expectedDescription, expectedIsActive);

        when(this.categoryGateway.create(any())).thenThrow(new IllegalStateException(expectedErrorMessage));

        // Act
        final var notification = this.useCase.execute(command).getLeft();

        // Assert
        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(1)).create(Mockito.argThat(category ->
                Objects.equals(expectedName, category.name())
                        && Objects.equals(expectedDescription, category.description())
                        && Objects.equals(expectedIsActive, category.isActive())
                        && Objects.nonNull(category.id())
                        && Objects.nonNull(category.createdAt())
                        && Objects.nonNull(category.updatedAt())
                        && Objects.isNull(category.deletedAt())
        ));
    }
}