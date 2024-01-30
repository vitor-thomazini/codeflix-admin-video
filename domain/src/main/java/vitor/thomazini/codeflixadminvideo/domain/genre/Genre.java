package vitor.thomazini.codeflixadminvideo.domain.genre;

import vitor.thomazini.codeflixadminvideo.domain.AggregateRoot;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.utils.InstantUtils;
import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Genre extends AggregateRoot<GenreId> {
    private String name;
    private boolean active;
    private List<CategoryId> categories;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    public Genre(
            final GenreId genreId,
            final String name,
            final boolean active,
            final List<CategoryId> categories,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(genreId);
        this.name = name;
        this.active = active;
        this.categories = categories;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
        this.selfValidate();
    }


    public static Genre newGenre(final String name, final boolean isActive) {
        final var id = GenreId.unique();
        final var now = InstantUtils.now();
        final var deletedAt = isActive ? null : now;
        return new Genre(id, name, isActive, new ArrayList<>(), now, now, deletedAt);
    }

    public static Genre with(
            final GenreId id,
            final String name,
            final boolean isActive,
            final List<CategoryId> categories,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        return new Genre(id, name, isActive, categories, createdAt, updatedAt, deletedAt);
    }

    public static Genre from(final Genre genre) {
        return new Genre(
                genre.id(),
                genre.name(),
                genre.isActive(),
                new ArrayList<>(genre.categories()),
                genre.createdAt(),
                genre.updatedAt(),
                genre.deletedAt()
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new GenreValidator(this, handler).validate();
    }

    public Genre update(final String name, final boolean isActive, final List<CategoryId> categories) {
        if (isActive) {
            this.activate();
        } else {
            this.deactivate();
        }
        this.name = name;
        this.categories = new ArrayList<>(categories == null ? Collections.emptyList() : categories);
        this.updatedAt = InstantUtils.now();
        this.selfValidate();
        return this;
    }

    public Genre activate() {
        this.updatedAt = InstantUtils.now();
        this.deletedAt = null;
        this.active = true;
        return this;
    }

    public Genre deactivate() {
        if (this.deletedAt() == null) {
            this.deletedAt = InstantUtils.now();
        }
        this.updatedAt = InstantUtils.now();
        this.active = false;
        return this;
    }

    public Genre addCategory(final CategoryId categoryId) {
        if (categoryId == null) {
            return this;
        }

        this.categories.add(categoryId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre addCategories(final List<CategoryId> categories) {
        if (categories == null || categories.isEmpty()) {
            return this;
        }

        this.categories.addAll(categories);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public Genre removeCategory(final CategoryId categoryId) {
        if (categoryId == null) {
            return this;
        }
        this.categories.remove(categoryId);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String name() {
        return this.name;
    }

    public boolean isActive() {
        return this.active;
    }

    public List<CategoryId> categories() {
        return Collections.unmodifiableList(this.categories);
    }

    public Instant createdAt() {
        return this.createdAt;
    }

    public Instant updatedAt() {
        return this.updatedAt;
    }

    public Instant deletedAt() {
        return this.deletedAt;
    }

    private void selfValidate() {
        final var notification = Notification.create();
        this.validate(notification);

        if (notification.hasErrors()) {
            throw new NotificationException("Failed to create a Aggregate Genre", notification);
        }
    }
}
