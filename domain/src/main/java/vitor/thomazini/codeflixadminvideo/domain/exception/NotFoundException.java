package vitor.thomazini.codeflixadminvideo.domain.exception;

import vitor.thomazini.codeflixadminvideo.domain.AggregateRoot;
import vitor.thomazini.codeflixadminvideo.domain.Identifier;
import vitor.thomazini.codeflixadminvideo.domain.validation.Error;

import java.util.Collections;
import java.util.List;

public class NotFoundException extends DomainException {

    protected NotFoundException(final  String message, final List<Error> errors) {
        super(message, errors);
    }

    public static NotFoundException with(
            final Class<? extends AggregateRoot<?>> aggregate,
            final Identifier id
    ) {
        final var error = "%s with ID %s was not found".formatted(
                aggregate.getSimpleName(),
                id.value()
        );
        return new NotFoundException(error, Collections.emptyList());
    }
}