package vitor.thomazini.codeflixadminvideo.application.category.retrieve.list;

import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;

import java.util.Objects;

public class DefaultListCategoriesUseCase extends ListCategoriesUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultListCategoriesUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Pagination<CategoryListOutput> execute(final SearchQuery query) {
        return this.categoryGateway.findAll(query)
                .map(CategoryListOutput::from);
    }
}