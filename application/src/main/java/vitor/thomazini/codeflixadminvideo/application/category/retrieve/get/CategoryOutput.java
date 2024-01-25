package vitor.thomazini.codeflixadminvideo.application.category.retrieve.get;

import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import java.time.Instant;

public record CategoryOutput(
        CategoryId id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static CategoryOutput from(final Category category) {
        return new CategoryOutput(
                category.id(),
                category.name(),
                category.description(),
                category.isActive(),
                category.createdAt(),
                category.updatedAt(),
                category.deletedAt()
        );
    }
}