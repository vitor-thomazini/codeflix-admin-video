package vitor.thomazini.codeflixadminvideo.application.castmember.delete;

import vitor.thomazini.codeflixadminvideo.application.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
    extends UnitUseCase<String>
    permits DefaultDeleteCastMemberUseCase {
}