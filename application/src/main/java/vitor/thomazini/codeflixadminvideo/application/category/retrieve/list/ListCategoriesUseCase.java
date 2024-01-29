package vitor.thomazini.codeflixadminvideo.application.category.retrieve.list;

import vitor.thomazini.codeflixadminvideo.application.UseCase;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;

public abstract class ListCategoriesUseCase
        extends UseCase<SearchQuery, Pagination<CategoryListOutput>> {
}