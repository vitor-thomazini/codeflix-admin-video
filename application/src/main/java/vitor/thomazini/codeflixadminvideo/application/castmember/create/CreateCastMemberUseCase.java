package vitor.thomazini.codeflixadminvideo.application.castmember.create;

import vitor.thomazini.codeflixadminvideo.application.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase {
}