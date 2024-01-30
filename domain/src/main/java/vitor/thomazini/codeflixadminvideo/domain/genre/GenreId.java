package vitor.thomazini.codeflixadminvideo.domain.genre;

import vitor.thomazini.codeflixadminvideo.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreId extends Identifier {
    private final String value;

    private GenreId(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static GenreId unique() {
        return GenreId.from(UUID.randomUUID());
    }

    public static GenreId from(final String id) {
        return new GenreId(id);
    }

    public static GenreId from(final UUID id) {
        return new GenreId(id.toString().toLowerCase());
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreId that = (GenreId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
