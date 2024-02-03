package vitor.thomazini.codeflixadminvideo.domain.castmember;

import vitor.thomazini.codeflixadminvideo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberId extends Identifier {
    private String value;

    private CastMemberId(final String anId) {
        Objects.requireNonNull(anId);
        this.value = anId;
    }

    public static CastMemberId unique() {
        return CastMemberId.from(UUID.randomUUID().toString());
    }

    public static CastMemberId from(final String anId) {
        return new CastMemberId(anId);
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberId that = (CastMemberId) o;
        return value().equals(that.value());
    }

    @Override
    public int hashCode() {
        return Objects.hash(value());
    }
}
