package vitor.thomazini.codeflixadminvideo.application.genre.update;

import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.DomainException;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotFoundException;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreId;
import vitor.thomazini.codeflixadminvideo.domain.validation.Error;
import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand command) {
        final var id = GenreId.from(command.id());
        final var categories = toCategoryId(command.categories());

        final var genre = this.genreGateway.findById(id)
                .orElseThrow(notFound(id));
        final var notification = Notification.create();
        notification.append(validateCategories(categories));
        notification.validate(() -> genre.update(command.name(), command.isActive(), categories));

        if (notification.hasErrors()) {
            throw new NotificationException(
                    "Could not update Aggregate Genre %s".formatted(id.value()),
                    notification
            );
        }

        return UpdateGenreOutput.from(this.genreGateway.update(genre));
    }

    private ValidationHandler validateCategories(final List<CategoryId> ids) {
        final var notification = Notification.create();
        if (Objects.isNull(ids) || ids.isEmpty()) {
            return notification;
        }

        final var retrieveIds = categoryGateway.existsByIds(ids);
        if (ids.size() != retrieveIds.size()) {
            final var missingIds = new ArrayList<>(ids);
            missingIds.removeAll(retrieveIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryId::value)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: %s".formatted(missingIdsMessage)));
        }

        return notification;
    }

    private static Supplier<DomainException> notFound(final GenreId id) {
        return () -> NotFoundException.with(Genre.class, id);
    }

    private List<CategoryId> toCategoryId(final List<String> categories) {
        return categories.stream()
                .map(CategoryId::from)
                .toList();
    }
}
