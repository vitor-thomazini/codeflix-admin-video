package vitor.thomazini.codeflixadminvideo.application.castmember.update;

import vitor.thomazini.codeflixadminvideo.application.UseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase {
}