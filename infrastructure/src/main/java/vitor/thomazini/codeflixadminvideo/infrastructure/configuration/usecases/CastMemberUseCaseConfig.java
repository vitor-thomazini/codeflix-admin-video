package vitor.thomazini.codeflixadminvideo.infrastructure.configuration.usecases;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import vitor.thomazini.codeflixadminvideo.application.castmember.create.CreateCastMemberUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.create.DefaultCreateCastMemberUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.delete.DeleteCastMemberUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.get.DefaultGetCastMemberByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.list.DefaultListCastMembersUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.list.ListCastMembersUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.update.DefaultUpdateCastMemberUseCase;
import vitor.thomazini.codeflixadminvideo.application.castmember.update.UpdateCastMemberUseCase;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listCastMembersUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }
}