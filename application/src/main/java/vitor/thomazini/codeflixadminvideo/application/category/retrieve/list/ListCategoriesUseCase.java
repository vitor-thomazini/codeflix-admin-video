package vitor.thomazini.codeflixadminvideo.application.category.retrieve.list;

import vitor.thomazini.codeflixadminvideo.application.UseCase;
import vitor.thomazini.codeflixadminvideo.domain.category.CategorySearchQuery;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<CategorySearchQuery, Pagination<CategoryListOutput>> {
}