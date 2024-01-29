package vitor.thomazini.codeflixadminvideo.domain.genre;

import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Genre create(Genre genre);

    void deleteById(GenreId id);

    Optional<Genre> findById(GenreId id);

    Genre update(Genre genre);

    Pagination<Genre> findAll(SearchQuery query);
}
