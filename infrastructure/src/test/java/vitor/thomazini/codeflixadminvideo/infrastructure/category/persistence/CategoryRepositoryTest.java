package vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import vitor.thomazini.codeflixadminvideo.MySQLGatewayTest;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;

@MySQLGatewayTest
class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void givenAnInvalidNullName_whenCallsSave_thenShouldReturnError() {
        // Arrange
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value : vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity.name";

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        // Act
        final Executable action = () -> categoryRepository.save(entity);

        // Assert
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, action);
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullCreatedAt_whenCallsSave_thenShouldReturnError() {
        // Arrange
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value : vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        // Act
        final Executable action = () -> categoryRepository.save(entity);

        // Assert
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, action);
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }

    @Test
    void givenAnInvalidNullUpdatedAt_whenCallsSave_thenShouldReturnError() {
        // Arrange
        final var expectedPropertyName = "updatedAt";
        final var expectedMessage = "not-null property references a null or transient value : vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

        final var category = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setUpdatedAt(null);

        // Act
        final Executable action = () -> categoryRepository.save(entity);

        // Assert
        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, action);
        final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

        Assertions.assertEquals(expectedPropertyName, actualCause.getPropertyName());
        Assertions.assertEquals(expectedMessage, actualCause.getMessage());
    }
}