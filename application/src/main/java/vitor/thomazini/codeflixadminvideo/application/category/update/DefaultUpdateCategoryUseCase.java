package vitor.thomazini.codeflixadminvideo.application.category.update;

import io.vavr.API;
import io.vavr.control.Either;
import vitor.thomazini.codeflixadminvideo.domain.category.Category;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.DomainException;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase {

    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(final UpdateCategoryCommand command) {
        final var categoryId = CategoryId.from(command.id());

        final var category = categoryGateway.findById(categoryId)
                .orElseThrow(notFound(categoryId));

        final var notification = Notification.create();
        category.update(command.name(), command.description(), command.isActive())
                .validate(notification);

        return notification.hasErrors() ? API.Left(notification) : update(category);
    }

    private Either<Notification, UpdateCategoryOutput> update(final Category category) {
        return API.Try(() -> this.categoryGateway.update(category))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private static Supplier<DomainException> notFound(final CategoryId id) {
        return () -> NotFoundException.with(Category.class, id);
    }
}