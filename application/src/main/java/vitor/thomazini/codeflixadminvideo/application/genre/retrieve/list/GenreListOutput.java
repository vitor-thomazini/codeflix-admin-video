package vitor.thomazini.codeflixadminvideo.application.genre.retrieve.list;

import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        String id,
        String name,
        boolean isActive,
        List<String> categories,
        Instant createdAt,
        Instant deletedAt
) {

    public static GenreListOutput from(final Genre genre) {
        return new GenreListOutput(
                genre.id().value(),
                genre.name(),
                genre.isActive(),
                genre.categories().stream()
                        .map(CategoryId::value)
                        .toList(),
                genre.createdAt(),
                genre.deletedAt()
        );
    }
}
