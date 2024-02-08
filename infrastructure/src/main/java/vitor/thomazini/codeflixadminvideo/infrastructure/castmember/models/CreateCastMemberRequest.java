package vitor.thomazini.codeflixadminvideo.infrastructure.castmember.models;

import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(String name, CastMemberType type) {
}