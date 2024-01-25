package vitor.thomazini.codeflixadminvideo.domain.exception;

import vitor.thomazini.codeflixadminvideo.domain.validation.Error;

import java.util.List;

public class DomainException extends NoStackTraceException {
    protected final List<Error> errors;

    protected DomainException(final String message, final List<Error> errors) {
        super(message);
        this.errors = errors;
    }

    public static DomainException with(final Error error) {
        return new DomainException(error.message(), List.of(error));
    }

    public static DomainException with(final List<Error> errors) {
        return new DomainException("", errors);
    }

    public List<Error> errors() {
        return this.errors;
    }
}