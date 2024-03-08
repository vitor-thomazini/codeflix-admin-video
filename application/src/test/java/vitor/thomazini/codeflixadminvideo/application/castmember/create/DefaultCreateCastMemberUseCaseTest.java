package vitor.thomazini.codeflixadminvideo.application.castmember.create;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.name;

class DefaultCreateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidCommand_whenCallsCreateCastMember_shouldReturnIt() {
        // Arrange
        final var expectedName = name();
        final var expectedType = type();

        final var aCommand = CreateCastMemberCommand.with(expectedName, expectedType);

        when(castMemberGateway.create(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        verify(castMemberGateway).create(argThat(aMember ->
                Objects.nonNull(aMember.id())
                        && Objects.equals(expectedName, aMember.name())
                        && Objects.equals(expectedType, aMember.type())
                        && Objects.nonNull(aMember.createdAt())
                        && Objects.nonNull(aMember.updatedAt())
        ));
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