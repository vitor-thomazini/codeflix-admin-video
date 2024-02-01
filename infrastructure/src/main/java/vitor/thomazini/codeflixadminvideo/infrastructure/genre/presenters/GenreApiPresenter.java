package vitor.thomazini.codeflixadminvideo.infrastructure.genre.presenters;

import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get.GenreOutput;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.list.GenreListOutput;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.models.GenreListResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {

    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}