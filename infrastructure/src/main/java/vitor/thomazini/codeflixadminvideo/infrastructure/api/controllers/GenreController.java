package vitor.thomazini.codeflixadminvideo.infrastructure.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vitor.thomazini.codeflixadminvideo.application.genre.create.CreateGenreCommand;
import vitor.thomazini.codeflixadminvideo.application.genre.create.CreateGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.delete.DeleteGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.get.GetGenreByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.retrieve.list.ListGenreUseCase;
import vitor.thomazini.codeflixadminvideo.application.genre.update.UpdateGenreCommand;
import vitor.thomazini.codeflixadminvideo.application.genre.update.UpdateGenreUseCase;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.api.GenreAPI;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.models.CreateGenreRequest;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.models.GenreListResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.models.GenreResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.models.UpdateGenreRequest;
import vitor.thomazini.codeflixadminvideo.infrastructure.genre.presenters.GenreApiPresenter;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreAPI {

    private final CreateGenreUseCase createGenreUseCase;
    private final DeleteGenreUseCase deleteGenreUseCase;
    private final GetGenreByIdUseCase getGenreByIdUseCase;
    private final ListGenreUseCase listGenreUseCase;
    private final UpdateGenreUseCase updateGenreUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final ListGenreUseCase listGenreUseCase,
            final UpdateGenreUseCase updateGenreUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.listGenreUseCase = Objects.requireNonNull(listGenreUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest input) {
        final var aCommand = CreateGenreCommand.with(
                input.name(),
                input.isActive(),
                input.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var aQuery = new SearchQuery(page, perPage, search, sort, direction);
        return this.listGenreUseCase.execute(aQuery)
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String id) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateGenreRequest input) {
        final var aCommand = UpdateGenreCommand.with(
                id,
                input.name(),
                input.isActive(),
                input.categories()
        );
        final var output = this.updateGenreUseCase.execute(aCommand);
        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteGenreUseCase.execute(id);
    }
}