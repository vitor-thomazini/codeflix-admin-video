package vitor.thomazini.codeflixadminvideo.infrastructure.category.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import java.time.Instant;

@Entity
@Table(name = "category")
public class CategoryJpaEntity {
    @Id
    private String id;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 4000)
    private String description;

    @Column(name = "isActive", nullable = false)
    private boolean active;

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false, columnDefinition = "DATETIME(6)")
    private Instant updatedAt;

    @Column(name = "deleted_at", columnDefinition = "DATETIME(6)")
    private Instant deletedAt;

    public CategoryJpaEntity() {
    }

    private CategoryJpaEntity(
            final String id,
            final String name,
            final String description,
            final boolean active,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.active = active;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static CategoryJpaEntity from(final Category category) {
        return new CategoryJpaEntity(
                category.id().value(),
                category.name(),
                category.description(),
                category.isActive(),
                category.createdAt(),
                category.updatedAt(),
                category.deletedAt()
        );
    }

    public Category toAggregate() {
        return Category.with(
                CategoryId.from(this.getId()),
                this.getName(),
                this.getDescription(),
                this.isActive(),
                this.getCreatedAt(),
                this.getUpdatedAt(),
                this.getDeletedAt()
        );
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
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