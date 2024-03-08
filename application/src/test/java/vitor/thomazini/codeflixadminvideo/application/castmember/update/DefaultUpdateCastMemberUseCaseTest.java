package vitor.thomazini.codeflixadminvideo.application.castmember.update;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.name;

class DefaultUpdateCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidCommand_whenCallsUpdateCastMember_shouldReturnItsIdentifier() {
        // Arrange
        final var aMember = CastMember.newCastMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.id();
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.value(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(CastMember.with(aMember)));

        when(castMemberGateway.update(any()))
                .thenAnswer(returnsFirstArg());

        // Act
        final var actualOutput = useCase.execute(aCommand);

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway).update(argThat(aUpdatedMember ->
                Objects.equals(expectedId, aUpdatedMember.id())
                        && Objects.equals(expectedName, aUpdatedMember.name())
                        && Objects.equals(expectedType, aUpdatedMember.type())
                        && Objects.equals(aMember.createdAt(), aUpdatedMember.createdAt())
                        && aMember.updatedAt().isBefore(aUpdatedMember.updatedAt())
        ));
    }

    @Test
    void givenAInvalidName_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // Arrange
        final var aMember = CastMember.newCastMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.id();
        final String expectedName = null;
        final var expectedType = CastMemberType.ACTOR;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'name' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.value(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        // Act
        final Executable action = () -> useCase.execute(aCommand);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(castMemberGateway).findById(expectedId);
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    void givenAInvalidType_whenCallsUpdateCastMember_shouldThrowsNotificationException() {
        // Arrange
        final var aMember = CastMember.newCastMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = aMember.id();
        final var expectedName = name();
        final CastMemberType expectedType = null;

        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "'type' should not be null";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.value(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        // Act
        final Executable action = () -> useCase.execute(aCommand);

        // Assert
        final var actualException = Assertions.assertThrows(NotificationException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.errors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.errors().getFirst().message());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }

    @Test
    void givenAInvalidId_whenCallsUpdateCastMember_shouldThrowsNotFoundException() {
        // Arrange
        final var aMember = CastMember.newCastMember("vin diesel", CastMemberType.DIRECTOR);

        final var expectedId = CastMemberId.from("123");
        final var expectedName = name();
        final var expectedType = type();

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        final var aCommand = UpdateCastMemberCommand.with(
                expectedId.value(),
                expectedName,
                expectedType
        );

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        // Act
        final Executable action = () -> useCase.execute(aCommand);

        // Assert
        final var actualException = Assertions.assertThrows(NotFoundException.class, action);

        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
        verify(castMemberGateway, times(0)).update(any());
    }
}