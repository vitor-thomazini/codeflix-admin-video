package vitor.thomazini.codeflixadminvideo.application.category.retrieve.list;

import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;

import java.time.Instant;

public record CategoryListOutput(
        CategoryId id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {

    public static CategoryListOutput from(final Category category) {
        return new CategoryListOutput(
                category.id(),
                category.name(),
                category.description(),
                category.isActive(),
                category.createdAt(),
                category.deletedAt()
        );
    }
}