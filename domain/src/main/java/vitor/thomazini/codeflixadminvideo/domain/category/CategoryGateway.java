package vitor.thomazini.codeflixadminvideo.domain.category;

import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CategoryGateway {

    Category create(Category category);

    void deleteById(CategoryId id);

    Optional<Category> findById(CategoryId id);

    Category update(Category category);

    Pagination<Category> findAll(SearchQuery query);

    List<CategoryId> existsByIds(Iterable<CategoryId> ids);
}
