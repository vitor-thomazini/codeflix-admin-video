package vitor.thomazini.codeflixadminvideo.domain.category;

import vitor.thomazini.codeflixadminvideo.domain.Identifier;
import vitor.thomazini.codeflixadminvideo.domain.utils.IdUtils;

import java.util.Objects;
import java.util.UUID;

public class CategoryId extends Identifier {
    private final String value;

    private CategoryId(final String value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    public static CategoryId unique() {
        return CategoryId.from(IdUtils.uuid());
    }

    public static CategoryId from(final String id) {
        return new CategoryId(id);
    }

    @Override
    public String value() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CategoryId that = (CategoryId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
