package vitor.thomazini.codeflixadminvideo.domain;

import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;

import java.util.Objects;

public abstract class Entity<ID extends Identifier> {
    protected final ID id;

    protected Entity(final ID id) {
        Objects.requireNonNull(id, "'id' should not be null");
        this.id = id;
    }

    public abstract void validate(ValidationHandler handler);

    public ID id() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final var entity = (Entity<?>) o;
        return Objects.equals(id(), entity.id());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id());
    }
}
