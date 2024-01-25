package vitor.thomazini.codeflixadminvideo.domain.category;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vitor.thomazini.codeflixadminvideo.domain.exception.DomainException;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.ThrowsValidationHandler;

public class CategoryTest {

    @Test
    public void givenAValidParams_whenCallsNewCategory_thenInstantiateACategory() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        // Act
        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidNullName_whenCallsNewCategoryAndValidate_thenShouldReceiveError() {
        // Arrange
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().get(0).message());
    }

    @Test
    public void givenAnInvalidEmptyName_whenCallsNewCategoryAndValidate_thenShouldReceiveError() {
        // Arrange
        final String expectedName = "   ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be empty";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameLessThan3_whenCallsNewCategoryAndValidate_thenShouldReceiveError() {
        // Arrange
        final String expectedName = "Fi ";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        final var actualException = Assertions.assertThrows(DomainException.class, () -> {
            actualCategory.validate(new ThrowsValidationHandler());
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().get(0).message());
    }

    @Test
    public void givenAnInvalidNameMoreThan255_whenCallsNewCategoryAndValidate_thenShouldReceiveError() {
        // Arrange
        final String expectedName = """
                A certificação de metodologias que nos auxiliam a lidar com a consulta aos diversos militantes aponta para a melhoria de alternativas às soluções ortodoxas.
                Todavia, o início da atividade geral de formação de atitudes facilita a criação dos relacionamentos verticais entre as hierarquias.
                """;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";
        final var expectedErrorCount = 1;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);


        // Act
        final var actualException = Assertions.assertThrows(DomainException.class,
                () -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().get(0).message());
    }

    @Test
    public void givenAValidEmptyDescription_whenCallsNewCategory_thenInstantiateAValidCategory() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "  ";
        final var expectedIsActive = true;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAValidIsActiveFalse_whenCallsNewCategory_thenInstantiateACategory() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var actualCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        // Act
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertNotNull(actualCategory);
        Assertions.assertNotNull(actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.createdAt());
        Assertions.assertNotNull(actualCategory.updatedAt());
        Assertions.assertNotNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAValidActiveCategory_whenCallsDeactivate_thanReturnCategoryInactivated() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;


        final var category = Category.newCategory(expectedName, expectedDescription, true);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.createdAt();
        final var updatedAt = category.updatedAt();

        // Assert
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.deletedAt());

        // Act
        final var actualCategory =  category.deactivate();
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.createdAt());
        Assertions.assertTrue(actualCategory.updatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAValidInactiveCategory_whenCallsActivate_thanReturnCategoryActivated() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, false);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.createdAt();
        final var updatedAt = category.updatedAt();

        // Assert
        Assertions.assertFalse(category.isActive());
        Assertions.assertNotNull(category.deletedAt());

        // Act
        final var actualCategory =  category.activate();
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.createdAt());
        Assertions.assertTrue(actualCategory.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Film", "A categoria", expectedIsActive);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.createdAt();
        final var updatedAt = category.updatedAt();

        // Act
        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertEquals(createdAt, actualCategory.createdAt());
        Assertions.assertTrue(actualCategory.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(actualCategory.deletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        // Arrange
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;

        final var category = Category.newCategory("Film", "A categoria", true);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.createdAt();
        final var updatedAt = category.updatedAt();

        // Assert
        Assertions.assertTrue(category.isActive());
        Assertions.assertNull(category.deletedAt());

        // Act
        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);
        Assertions.assertDoesNotThrow(() -> actualCategory.validate(new ThrowsValidationHandler()));

        // Assert
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertFalse(category.isActive());
        Assertions.assertEquals(createdAt, actualCategory.createdAt());
        Assertions.assertTrue(actualCategory.updatedAt().isAfter(updatedAt));
        Assertions.assertNotNull(category.deletedAt());
    }

    @Test
    public void givenAValidCategory_whenCallUpdateWithInvalidParams_thenReturnCategoryUpdated() {
        // Arrange
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Filmes", "A categoria", true);
        Assertions.assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        final var createdAt = category.createdAt();
        final var updatedAt = category.updatedAt();

        // Act
        final var actualCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        // Assert
        Assertions.assertEquals(category.id(), actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertTrue(category.isActive());
        Assertions.assertEquals(createdAt, actualCategory.createdAt());
        Assertions.assertTrue(actualCategory.updatedAt().isAfter(updatedAt));
        Assertions.assertNull(category.deletedAt());
    }
}