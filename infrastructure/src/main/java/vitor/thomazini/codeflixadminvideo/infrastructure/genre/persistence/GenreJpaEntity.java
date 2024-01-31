package vitor.thomazini.codeflixadminvideo.infrastructure.genre.persistence;

import jakarta.persistence.*;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "genres")
public class GenreJpaEntity {

    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "active", nullable = false)
    private boolean active;

    @OneToMany(
            mappedBy = "genre",
            cascade = CascadeType.ALL,
            fetch = FetchType.EAGER,
            orphanRemoval = true
    )
    private Set<GenreCategoryJpaEntity> categories;

    @Column(
            name = "created_at",
            nullable = false,
            columnDefinition = "DATETIME(6)"
    )
    private Instant createdAt;

    @Column(
            name = "updated_at",
            nullable = false,
            columnDefinition = "DATETIME(6)"
    )
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public GenreJpaEntity() {}

    private GenreJpaEntity(
            final String id,
            final String name,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.categories =new HashSet<>();
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static GenreJpaEntity from(final Genre genre) {
        final var entity = new GenreJpaEntity(
                genre.id().value(),
                genre.name(),
                genre.isActive(),
                genre.createdAt(),
                genre.updatedAt(),
                genre.deletedAt()
        );

        genre.categories()
                .forEach(entity::addCategory);

        return entity;
    }

    public Genre toAggregate() {
        return Genre.with(
                GenreId.from(this.getId()),
                this.getName(),
                this.isActive(),
                this.getCategoriesIds(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
    }

    private void addCategory(final CategoryId id) {
        this.categories.add(GenreCategoryJpaEntity.from(this, id));
    }

    private void removeCategory(final CategoryId id) {
        this.categories.remove(GenreCategoryJpaEntity.from(this, id));
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Set<GenreCategoryJpaEntity> getCategories() {
        return categories;
    }

    public List<CategoryId> getCategoriesIds() {
        return categories.stream()
                .map(it -> CategoryId.from(it.getId().getCategoryId()))
                .toList();
    }

    public void setCategories(Set<GenreCategoryJpaEntity> categories) {
        this.categories = categories;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Instant deletedAt) {
        this.deletedAt = deletedAt;
    }
}
