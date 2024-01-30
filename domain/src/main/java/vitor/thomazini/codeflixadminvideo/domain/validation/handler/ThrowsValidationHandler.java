package vitor.thomazini.codeflixadminvideo.domain.validation.handler;

import vitor.thomazini.codeflixadminvideo.domain.exception.DomainException;
import vitor.thomazini.codeflixadminvideo.domain.validation.Error;
import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;

import java.util.List;

public class ThrowsValidationHandler implements ValidationHandler {

    @Override
    public ValidationHandler append(final Error error) {
        throw DomainException.with(error);
    }

    @Override
    public ValidationHandler append(final ValidationHandler handler) {
        throw DomainException.with(handler.getErrors());
    }

    @Override
    public <T> T validate(final Validation<T> validation) {
        try {
            return validation.validate();
        } catch (Exception e) {
            throw DomainException.with(new Error(e.getMessage()));
        }
    }

    @Override
    public List<Error> getErrors() {
        return List.of();
    }
}