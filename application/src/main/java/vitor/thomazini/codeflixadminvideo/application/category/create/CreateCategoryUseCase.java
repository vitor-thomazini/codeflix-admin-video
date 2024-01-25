package vitor.thomazini.codeflixadminvideo.application.category.create;

import io.vavr.control.Either;
import vitor.thomazini.codeflixadminvideo.application.UseCase;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

public abstract class CreateCategoryUseCase
        extends UseCase<CreateCategoryCommand, Either<Notification, CreateCategoryOutput>> {
}