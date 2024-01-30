package vitor.thomazini.codeflixadminvideo.application.genre.create;

import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;

public record CreateGenreOutput(String id) {

    public static CreateGenreOutput from(final String id) {
        return new CreateGenreOutput(id);
    }

    public static CreateGenreOutput from(final Genre genre) {
        return new CreateGenreOutput(genre.id().value());
    }
}