package vitor.thomazini.codeflixadminvideo.domain.exception;

import vitor.thomazini.codeflixadminvideo.domain.validation.handler.Notification;

public class NotificationException extends DomainException {

    public NotificationException(final String message, final Notification notification) {
        super(message, notification.getErrors());
    }
}
