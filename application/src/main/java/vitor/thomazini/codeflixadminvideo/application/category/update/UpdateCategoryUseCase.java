package vitor.thomazini.codeflixadminvideo.application.category.update;

import io.vavr.control.Either;
import vitor.thomazini.codeflixadminvideo.application.UseCase;
import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

public abstract class UpdateCategoryUseCase
        extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {
}