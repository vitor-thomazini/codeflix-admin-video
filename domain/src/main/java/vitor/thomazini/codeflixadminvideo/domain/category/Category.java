package vitor.thomazini.codeflixadminvideo.domain.category;

import vitor.thomazini.codeflixadminvideo.domain.AggregateRoot;
import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryId> {
    private String name;
    private String description;
    private boolean isActive;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(
            final CategoryId id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = Objects.requireNonNull(createdAt, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(updatedAt, "'updatedAt' should not be null");
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(
            final String name,
            final String description,
            final boolean isActive
    ) {
        final var id = CategoryId.unique();
        final var now = Instant.now();
        final var deletedAt = isActive ? null : now;
        return new Category(id, name, description, isActive, now, now, deletedAt);
    }

    public static Category with(
            final CategoryId id,
            final String name,
            final String description,
            final boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Category(
                id,
                name,
                description,
                isActive,
                createdAt,
                updatedAt,
                deletedAt
        );
    }

    public static Category from(final Category category) {
        return new Category(
                category.id(),
                category.name(),
                category.description(),
                category.isActive(),
                category.createdAt(),
                category.updatedAt(),
                category.deletedAt()
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new CategoryValidator(handler, this).validate();
    }

    public Category activate() {
        this.deletedAt = null;
        this.isActive = true;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (Objects.isNull(this.deletedAt())) {
            this.deletedAt = Instant.now();
        }

        this.isActive = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(
            final String name,
            final String description,
            final boolean isActive
    ) {
        if (isActive) {
            this.activate();
        } else {
            this.deactivate();
        }
        this.name = name;
        this.description = description;
        this.updatedAt = Instant.now();
        return this;
    }

    @Override
    public CategoryId id() {
        return this.id;
    }

    public String name() {
        return this.name;
    }

    public String description() {
        return this.description;
    }

    public boolean isActive() {
        return this.isActive;
    }

    public Instant createdAt() {
        return this.createdAt;
    }

    public Instant updatedAt() {
        return this.updatedAt;
    }

    public Instant deletedAt() {
        return deletedAt;
    }
}
