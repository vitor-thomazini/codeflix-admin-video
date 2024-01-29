package vitor.thomazini.codeflixadminvideo.domain.genre;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;

import java.util.ArrayList;
import java.util.List;

public class GenreTest {

    @Test
    public void givenValidParams_whenCallsNewGenre_thenShouldInstantiateAGenre() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        // Act
        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        // Assert
        Assertions.assertNotNull(actualGenre);
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories().size());
        Assertions.assertNotNull(actualGenre.createdAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallsNewGenreAndValidate_thenShouldReceiveError() {
        // Arrange
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallsNewGenreAndValidate_thenShouldReceiveError() {
        // Arrange
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    public void givenInvalidNameWithLengthGreaterThan255_whenCallsNewGenreAndValidate_thenShouldReceiveError() {
        // Arrange
        final String expectedName = """
                A certificação de metodologias que nos auxiliam a lidar com a consulta aos diversos militantes aponta para a melhoria de alternativas às soluções ortodoxas.
                Todavia, o início da atividade geral de formação de atitudes facilita a criação dos relacionamentos verticais entre as hierarquias.
                """;
        final var expectedIsActive = true;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 1 and 255 characters";

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            Genre.newGenre(expectedName, expectedIsActive);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    public void givenAnActiveGenre_whenCallDeactivate_thenShouldReceiveOk() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.deletedAt());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.deactivate();

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAnInactiveGenre_whenCallActivate_thenShouldReceiveOk() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = 0;

        final var actualGenre = Genre.newGenre(expectedName, false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.deletedAt());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.activate();

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories().size());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAValidInactiveGenre_whenCallUpdateWithActivate_thenShouldReceiveGenreUpdated() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryId.from("123"));

        final var actualGenre = Genre.newGenre("acao", false);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertFalse(actualGenre.isActive());
        Assertions.assertNotNull(actualGenre.deletedAt());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAValidActiveGenre_whenCallUpdateWithInactivate_thenShouldReceiveGenreUpdated() {
        // Arrange
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.of(CategoryId.from("123"));

        final var actualGenre = Genre.newGenre("acao", true);

        Assertions.assertNotNull(actualGenre);
        Assertions.assertTrue(actualGenre.isActive());
        Assertions.assertNull(actualGenre.deletedAt());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNotNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithEmptyName_thenShouldReceiveNotificationException() {
        // Arrange
        final var expectedName = " ";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryId.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualGenre = Genre.newGenre("acao", false);

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullName_thenShouldReceiveNotificationException() {
        // Arrange
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryId.from("123"));
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualGenre = Genre.newGenre("acao", false);

        // Act
        final var actualException = Assertions.assertThrows(NotificationException.class, () -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        // Assert
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    public void givenAValidGenre_whenCallUpdateWithNullCategories_thenShouldReceiveOK() {
        // Arrange
        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryId>();

        final var actualGenre = Genre.newGenre("acao", expectedIsActive);

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        Assertions.assertDoesNotThrow(() -> {
            actualGenre.update(expectedName, expectedIsActive, expectedCategories);
        });

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAValidEmptyCategoriesGenre_whenCallAddCategory_thenShouldReceiveOK() {
        // Arrange
        final var seriesId = CategoryId.from("123");
        final var moviesId = CategoryId.from("456");

        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesId, moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);

        Assertions.assertEquals(0, actualGenre.categories().size());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.addCategory(seriesId);
        actualGenre.addCategory(moviesId);

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAValidGenreWithTwoCategories_whenCallRemoveCategory_thenShouldReceiveOK() {
        // Arrange
        final var seriesId = CategoryId.from("123");
        final var moviesId = CategoryId.from("456");

        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, List.of(seriesId, moviesId));

        Assertions.assertEquals(2, actualGenre.categories().size());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.removeCategory(seriesId);

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualGenre.updatedAt()));
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAnInvalidNullAsCategoryId_whenCallAddCategory_thenShouldReceiveOK() {
        // Arrange
        final var seriesId = CategoryId.from("123");
        final var moviesId = CategoryId.from("456");

        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesId, moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(2, actualGenre.categories().size());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.addCategory(null);

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.updatedAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }

    @Test
    public void givenAnInvalidNullAsCategoryId_whenCallRemoveCategory_thenShouldReceiveOK() {
        // Arrange
        final var seriesId = CategoryId.from("123");
        final var moviesId = CategoryId.from("456");

        final String expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(seriesId, moviesId);

        final var actualGenre = Genre.newGenre(expectedName, expectedIsActive);
        actualGenre.update(expectedName, expectedIsActive, expectedCategories);

        Assertions.assertEquals(2, actualGenre.categories().size());

        final var actualCreatedAt = actualGenre.createdAt();
        final var actualUpdatedAt = actualGenre.updatedAt();

        // Act
        actualGenre.removeCategory(null);

        // Assert
        Assertions.assertNotNull(actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.categories());
        Assertions.assertEquals(actualCreatedAt, actualGenre.createdAt());
        Assertions.assertEquals(actualUpdatedAt, actualGenre.updatedAt());
        Assertions.assertNotNull(actualGenre.updatedAt());
        Assertions.assertNull(actualGenre.deletedAt());
    }
}
