package vitor.thomazini.codeflixadminvideo.infrastructure.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import vitor.thomazini.codeflixadminvideo.application.category.create.CreateCategoryCommand;
import vitor.thomazini.codeflixadminvideo.application.category.create.CreateCategoryOutput;
import vitor.thomazini.codeflixadminvideo.application.category.create.CreateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.delete.DeleteCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.get.GetCategoryByIdUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.retrieve.list.ListCategoriesUseCase;
import vitor.thomazini.codeflixadminvideo.application.category.update.UpdateCategoryCommand;
import vitor.thomazini.codeflixadminvideo.application.category.update.UpdateCategoryOutput;
import vitor.thomazini.codeflixadminvideo.application.category.update.UpdateCategoryUseCase;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;
import vitor.thomazini.codeflixadminvideo.infrastructure.api.CategoryAPI;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CategoryResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CategoryListResponse;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.CreateCategoryRequest;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.models.UpdateCategoryRequest;
import vitor.thomazini.codeflixadminvideo.infrastructure.category.presenters.CategoryApiPresenter;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;
    private final UpdateCategoryUseCase updateCategoryUseCase;
    private final DeleteCategoryUseCase deleteCategoryUseCase;
    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest input) {
        final var command = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id()))
                        .body(output);

        return this.createCategoryUseCase.execute(command)
                .fold(onError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        final var searchQuery = new SearchQuery(page, perPage, search, sort, direction);
        return this.listCategoriesUseCase.execute(searchQuery)
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(final String id) {
        return CategoryApiPresenter.present
                .compose(this.getCategoryByIdUseCase::execute)
                .apply(id);
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCategoryRequest input) {
        final var command = UpdateCategoryCommand.with(
                id,
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = ResponseEntity.unprocessableEntity()::body;

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(command)
                .fold(onError, onSuccess);
    }

    @Override
    public void deleteById(String id) {
        this.deleteCategoryUseCase.execute(id);
    }
}