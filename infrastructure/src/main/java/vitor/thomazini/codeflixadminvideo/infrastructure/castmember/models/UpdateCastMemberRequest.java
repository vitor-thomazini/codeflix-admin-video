package vitor.thomazini.codeflixadminvideo.infrastructure.castmember.models;

import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;

public record UpdateCastMemberRequest(String name, CastMemberType type) {
}