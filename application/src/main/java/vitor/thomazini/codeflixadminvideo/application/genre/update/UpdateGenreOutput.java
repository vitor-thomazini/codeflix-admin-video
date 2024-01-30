package vitor.thomazini.codeflixadminvideo.application.genre.update;

import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;

public record UpdateGenreOutput(String id) {

    public static UpdateGenreOutput from(final Genre genre) {
        return new UpdateGenreOutput(genre.id().value());
    }
}
