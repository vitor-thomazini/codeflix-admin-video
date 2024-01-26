package vitor.thomazini.codeflixadminvideo.infrastructure.category.presenters;

import vitor.thomazini.codeflixadminvideo.application.category.retrieve.get.CategoryOutput;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.list.CategoryListOutput;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CategoryResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CategoryListResponse;

import java.util.function.Function;

public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryResponse> present =
            output -> new CategoryResponse(
                    output.id().value(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt()
            );

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().value(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}