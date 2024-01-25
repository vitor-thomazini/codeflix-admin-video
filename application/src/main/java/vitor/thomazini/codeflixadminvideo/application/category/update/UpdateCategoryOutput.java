package vitor.thomazini.codeflixadminvideo.application.category.update;

import vitor.thomazini.codeflixadminvideo.domain.category.Category;

public record UpdateCategoryOutput(String id) {

    public static UpdateCategoryOutput from(final String id) {
        return new UpdateCategoryOutput(id);
    }

    public static UpdateCategoryOutput from(final Category category) {
        return new UpdateCategoryOutput(category.id().value());
    }
}