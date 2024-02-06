package vitor.thomazini.codeflixadminvideo.application.castmember.update;

import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;

public record UpdateCastMemberOutput(String id) {

    public static UpdateCastMemberOutput from(final CastMemberId anId) {
        return new UpdateCastMemberOutput(anId.value());
    }

    public static UpdateCastMemberOutput from(final CastMember aMember) {
        return from(aMember.id());
    }
}