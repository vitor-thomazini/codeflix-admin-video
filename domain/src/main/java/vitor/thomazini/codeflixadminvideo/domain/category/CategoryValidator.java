package vitor.thomazini.codeflixadminvideo.domain.category;

import vitor.thomazini.codeflixadminvideo.domain.validation.Error;
import vitor.thomazini.codeflixadminvideo.domain.validation.ValidationHandler;
import vitor.thomazini.codeflixadminvideo.domain.validation.Validator;

public final class CategoryValidator extends Validator {

    public static final int NAME_MAX_LENGTH = 255;
    public static final int NAME_MIN_LENGTH = 3;
    private final Category category;

    public CategoryValidator(final ValidationHandler handler, final Category category) {
        super(handler);
        this.category = category;
    }

    @Override
    public void validate() {
        checkNameConstraint();
    }

    private void checkNameConstraint() {
        final var name = this.category.name();
        if (this.category.name() == null) {
            this.validationHandler().append(new Error("'name' should not be null"));
            return;
        }

        if (this.category.name().isBlank()) {
            this.validationHandler().append(new Error("'name' should not be empty"));
            return;
        }

        final var length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
        }
    }
}