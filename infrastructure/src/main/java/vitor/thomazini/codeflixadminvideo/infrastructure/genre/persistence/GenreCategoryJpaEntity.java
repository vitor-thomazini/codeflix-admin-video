package vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence;

import jakarta.persistence.*;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import java.util.Objects;

@Entity
@Table(name = "genres_categories")
public class GenreCategoryJpaEntity {

    @EmbeddedId
    private GenreCategoryId id;

    @ManyToOne
    @MapsId("genreId")
    private GenreJpaEntity genre;

    public GenreCategoryJpaEntity() {}

    private GenreCategoryJpaEntity(final GenreJpaEntity genre, final CategoryId categoryId) {
        this.id = GenreCategoryId.from(genre.getId(), categoryId.value());
        this.genre = genre;
    }

    public static GenreCategoryJpaEntity from(final GenreJpaEntity genre, final CategoryId categoryId) {
        return new GenreCategoryJpaEntity(genre, categoryId);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreCategoryJpaEntity that = (GenreCategoryJpaEntity) o;
        return Objects.equals(id, that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public GenreCategoryId getId() {
        return id;
    }

    public void setId(GenreCategoryId id) {
        this.id = id;
    }

    public GenreJpaEntity getGenre() {
        return genre;
    }

    public void setGenre(GenreJpaEntity genre) {
        this.genre = genre;
    }
}
