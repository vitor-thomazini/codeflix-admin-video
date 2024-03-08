package vitor.thomazini.codeflixadminvideo.application.castmember.delete;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;
import vitor.thomazini.codeflixadminvideo.IntegrationTest;
import vitor.thomazini.codeflixadminvideo.domain.Fixture;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.persistence.CastMemberRepository;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@IntegrationTest
class DeleteCastMemberUseCaseIT {

    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @SpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // Arrange
        final var aMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());
        final var aMemberTwo = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = aMember.id();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMemberTwo));

        Assertions.assertEquals(2, this.castMemberRepository.count());

        // Act
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.value()));

        // Assert
        Assertions.assertEquals(1, this.castMemberRepository.count());
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId.value()));
        Assertions.assertTrue(this.castMemberRepository.existsById(aMemberTwo.id().value()));

        verify(castMemberGateway).deleteById(expectedId);
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // Arrange
        this.castMemberRepository.saveAndFlush(
                CastMemberJpaEntity.from(
                        CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type())
                )
        );

        final var expectedId = CastMemberId.from("123");

        Assertions.assertEquals(1, this.castMemberRepository.count());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertDoesNotThrow(action);
        Assertions.assertEquals(1, this.castMemberRepository.count());

        verify(castMemberGateway).deleteById(expectedId);
    }

    @Test
    void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // Arrange
        final var aMember = CastMember.newCastMember(Fixture.name(), Fixture.CastMembers.type());

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.id();

        Assertions.assertEquals(1, this.castMemberRepository.count());

        doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(any());

        // Act
        final Executable action = () -> useCase.execute(expectedId.value());

        // Assert
        Assertions.assertThrows(IllegalStateException.class, action);
        Assertions.assertEquals(1, this.castMemberRepository.count());

        verify(castMemberGateway).deleteById(expectedId);
    }
}