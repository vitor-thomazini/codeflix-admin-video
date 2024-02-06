package vitor.thomazini.codeflixadminvideo.application.castmember.create;

import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;

public record CreateCastMemberOutput(
        String id
) {

    public static CreateCastMemberOutput from(final CastMemberId anId) {
        return new CreateCastMemberOutput(anId.value());
    }

    public static CreateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.id());
    }
}