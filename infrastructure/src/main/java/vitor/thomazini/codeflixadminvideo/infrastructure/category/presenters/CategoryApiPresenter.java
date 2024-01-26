package vitor.thomazini.codeflixadminvideo.infrastructure.category.presenters;

import vitor.thomazini.codeflixadminvideo.application.category.retrieve.get.CategoryOutput;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.list.CategoryListOutput;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CategoryApiOutput;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CategoryListApiOutput;

import java.util.function.Function;

public interface CategoryApiPresenter {

    Function<CategoryOutput, CategoryApiOutput> present =
            output -> new CategoryApiOutput(
                    output.id().value(),
                    output.name(),
                    output.description(),
                    output.isActive(),
                    output.createdAt(),
                    output.updatedAt(),
                    output.deletedAt()
            );

    static CategoryListApiOutput present(final CategoryListOutput output) {
        return new CategoryListApiOutput(
                output.id().value(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}