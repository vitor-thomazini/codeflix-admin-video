package vitor.thomazini.codeflixadminvideo.application.castmember.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.name;

class DefaultDeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // Arrange
        final var aMember = CastMember.newCastMember(name(), type());
        final var expectedId = aMember.id();

        doNothing().when(castMemberGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());


        // Assert
        Assertions.assertDoesNotThrow(action);

        verify(castMemberGateway).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // Arrange
        final var expectedId = CastMemberId.from("123");

        doNothing().when(castMemberGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertDoesNotThrow(action);

        verify(castMemberGateway).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // Arrange
        final var aMember = CastMember.newCastMember(name(), type());
        final var expectedId = aMember.id();

        doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertThrows(IllegalStateException.class, action);

        verify(castMemberGateway).deleteById(eq(expectedId));
    }
}