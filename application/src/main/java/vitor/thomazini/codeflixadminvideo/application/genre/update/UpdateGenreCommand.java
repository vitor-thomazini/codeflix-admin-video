package vitor.thomazini.codeflixadminvideo.application.genre.update;

import java.util.List;

public record UpdateGenreCommand(
        String id,
        String name,
        Boolean isActive,
        List<String> categories
) {

    public static UpdateGenreCommand with(
            final String id,
            final String name,
            final Boolean isActive,
            final List<String> categories
    ) {
        return new UpdateGenreCommand(id, name, isActive != null && isActive, categories);
    }
}
