package vitor.thomazini.codeflixadminvideo.infrastructure.castmember.presenter;

import vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.get.CastMemberOutput;
import vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.list.CastMemberListOutput;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.models.CastMemberListResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberPresenter {

    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString()
        );
    }
}