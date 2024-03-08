package vitor.thomazini.codeflixadminvideo.application.castmember.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.persistence.CastMemberRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.name;

@IntegrationTest
class CreateCastMemberUseCaseIT {

    @Autowired
    private CreateCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        // Arrange
        final var expectedName = name();
        final var expectedType = type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // Act
        final var actualOutput = useCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualMember = this.castMemberRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualMember.getName());
        Assertions.assertEquals(expectedType, actualMember.getType());
        Assertions.assertNotNull(actualMember.getCreatedAt());
        Assertions.assertNotNull(actualMember.getUpdatedAt());
        Assertions.assertEquals(actualMember.getCreatedAt(), actualMember.getUpdatedAt());

        verify(castMemberGateway).create(any());
    }

    @Test
    void givenAInvalidName_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        // Arrange
        final String expectedName = null;
        final var expectedType = type();

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // Act
        final Executable action = () -> useCase.execute(aCommand);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(castMemberGateway, times(0)).create(any());
    }

    @Test
    void givenAInvalidType_whenCallsCreateCastMember_shouldThrowsNotificationException() {
        // Arrange
        final var expectedName = name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        // Act
        final Executable action = () -> useCase.execute(aCommand);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(castMemberGateway, times(0)).create(any());
    }
}