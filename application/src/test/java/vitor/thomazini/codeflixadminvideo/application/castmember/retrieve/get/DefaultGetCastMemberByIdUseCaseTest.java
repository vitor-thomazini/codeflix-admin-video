package vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.get;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.name;

class DefaultGetCastMemberByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCastMemberByIdUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    void givenAValidId_whenCallsGetCastMember_shouldReturnIt() {
        // Arrange
        final var expectedName = name();
        final var expectedType = type();
        final var aMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = aMember.id();

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.of(aMember));

        // Act
        final var actualOutput = useCase.execute(expectedId.value());

        // Assert
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.value(), actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedType, actualOutput.type());
        Assertions.assertEquals(aMember.createdAt(), actualOutput.createdAt());
        Assertions.assertEquals(aMember.updatedAt(), actualOutput.updatedAt());

        verify(castMemberGateway).findById(expectedId);
    }

    @Test
    void givenAInvalidId_whenCallsGetCastMemberAndDoesNotExists_shouldReturnNotFoundException() {
        // Arrange
        final var expectedId = CastMemberId.from("123");

        final var expectedErrorMessage = "CastMember with ID 123 was not found";

        when(castMemberGateway.findById(any()))
                .thenReturn(Optional.empty());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        final var actualOutput = Assertions.assertThrows(NotFoundException.class, action);

        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());

        verify(castMemberGateway).findById(eq(expectedId));
    }
}