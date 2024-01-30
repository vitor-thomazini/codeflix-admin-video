package vitor.thomazini.codeflixadminvideo.application.genre.create;

import vitor.thomazini.codeflixadminvideo.domain.category.CategoryGateway;
import vitor.thomazini.codeflixadminvideo.domain.category.CategoryId;
import vitor.thomazini.codeflixadminvideo.domain.exception.NotificationException;
import vitor.thomazini.codeflixadminvideo.domain.genre.Genre;
import vitor.thomazini.codeflixadminvideo.domain.genre.GenreGateway;
import vitor.thomazini.codeflixadminvideo.domain.validation.Error;
import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase {

    private final CategoryGateway categoryGateway;
    private final GenreGateway genreGateway;

    public DefaultCreateGenreUseCase(
            final CategoryGateway categoryGateway,
            final GenreGateway genreGateway
    ) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand command) {
        final var categories = toCategoryId(command.categories());
        
        final var notification = Notification.create();
        notification.append(validateCategories(categories));

        final var genre = notification.validate(() -> Genre.newGenre(command.name(), command.isActive()));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }

        genre.addCategories(categories);
        return CreateGenreOutput.from(this.genreGateway.create(genre));
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

    private List<CategoryId> toCategoryId(List<String> categories) {
        return categories.stream()
                .map(CategoryId::from)
                .toList();
    }
}
