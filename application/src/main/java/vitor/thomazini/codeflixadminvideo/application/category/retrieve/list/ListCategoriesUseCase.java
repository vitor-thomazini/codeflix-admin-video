package vitor.thomazini.codeflixadminvideo.application.category.retrieve.list;

import vitor.thomazini.codeflixadminvideo.application.UseCase;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}