package vitor.thomazini.codeflixadminvideo.domain.castmember;

import io.vavr.CheckedFunction0;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import vitor.thomazini.codeflixadminvideo.domain.UnitTest;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;

import java.util.function.Supplier;

class CastMemberTest extends UnitTest {

    @Test
    void givenAValidParams_whenCallsNewMember_thenInstantiateACastMember() {
        // Arrange
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        // Act
        final var actualMember = CastMember.newCastMember(expectedName, expectedType);

        // Assert
        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertNotNull(actualMember.createdAt());
        Assertions.assertNotNull(actualMember.updatedAt());
        Assertions.assertEquals(actualMember.createdAt(), actualMember.updatedAt());
    }

    @Test
    void givenAInvalidNullName_whenCallsNewMember_shouldReceiveANotification() {
        // Arrange
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        // Act
        final Executable action = () -> CastMember.newCastMember(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    void givenAInvalidEmptyName_whenCallsNewMember_shouldReceiveANotification() {
        // Arrange
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        // Act
        final Executable action = () -> CastMember.newCastMember(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    void givenAInvalidNameWithLengthMoreThan255_whenCallsNewMember_shouldReceiveANotification() {
        // Arrange
        final var expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        // Act
        final Executable action = () -> CastMember.newCastMember(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    void givenAInvalidNullType_whenCallsNewMember_shouldReceiveANotification() {
        // Arrange
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        // Act
        final Executable action = () -> CastMember.newCastMember(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().get(0).message());
    }

    @Test
    void givenAValidCastMember_whenCallUpdate_shouldReceiveUpdated() {
        // Arrange
        final var expectedName = "Vin Diesel";
        final var expectedType = CastMemberType.ACTOR;

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.id());

        final var actualID = actualMember.id();
        final var actualCreatedAt = actualMember.createdAt();
        final var actualUpdatedAt = actualMember.updatedAt();

        // Act
        actualMember.update(expectedName, expectedType);

        // Assert
        Assertions.assertEquals(actualID, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(actualCreatedAt, actualMember.createdAt());
        Assertions.assertTrue(actualUpdatedAt.isBefore(actualMember.updatedAt()));
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithInvalidNullName_shouldReceiveNotification() {
        // Arrange
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.id());

        // Act
        final Executable action = () -> actualMember.update(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    public void givenAValidCastMember_whenCallUpdateWithInvalidEmptyName_shouldReceiveNotification() {
        // Arrange
        final var expectedName = " ";
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be empty";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.id());

        // Act
        final Executable action = () -> actualMember.update(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithLengthMoreThan255_shouldReceiveNotification() {
        // Arrange
        final var expectedName = """
                Gostaria de enfatizar que o consenso sobre a necessidade de qualificação auxilia a preparação e a
                composição das posturas dos órgãos dirigentes com relação às suas atribuições.
                Do mesmo modo, a estrutura atual da organização apresenta tendências no sentido de aprovar a
                manutenção das novas proposições.
                """;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' must be between 3 and 255 characters";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.id());

        // Act
        final Executable action = () -> actualMember.update(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }

    @Test
    void givenAValidCastMember_whenCallUpdateWithInvalidNullType_shouldReceiveNotification() {
        // Arrange
        final var expectedName = "Vin Diesel";
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var actualMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);

        Assertions.assertNotNull(actualMember);
        Assertions.assertNotNull(actualMember.id());

        // Act
        final Executable action = () -> actualMember.update(expectedName, expectedType);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());
    }
}