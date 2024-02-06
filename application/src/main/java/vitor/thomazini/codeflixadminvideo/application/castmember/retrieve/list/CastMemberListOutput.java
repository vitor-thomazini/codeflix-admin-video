package vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.list;

import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberListOutput(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt
) {

    public static CastMemberListOutput from(final CastMember aMember) {
        return new CastMemberListOutput(
                aMember.id().value(),
                aMember.name(),
                aMember.type(),
                aMember.createdAt()
        );
    }
}