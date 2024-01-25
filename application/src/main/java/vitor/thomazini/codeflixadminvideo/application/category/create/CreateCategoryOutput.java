package vitor.thomazini.codeflixadminvideo.application.category.create;

import vitor.thomazini.codeflixadminvideo.domain.category.Category;

public record CreateCategoryOutput(String id) {

    public static CreateCategoryOutput from(final String id) {
        return new CreateCategoryOutput(id);
    }

    public static CreateCategoryOutput from(final Category category) {
        return new CreateCategoryOutput(category.id().value());
    }
}