package vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get;

import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static GenreOutput from(final Genre genre) {
        return new GenreOutput(
                genre.id().value(),
                genre.name(),
                genre.isActive(),
                genre.categories().stream()
                        .map(CategoryId::value)
                        .toList(),
                genre.createdAt(),
                genre.updatedAt(),
                genre.deletedAt()
        );
    }
}
