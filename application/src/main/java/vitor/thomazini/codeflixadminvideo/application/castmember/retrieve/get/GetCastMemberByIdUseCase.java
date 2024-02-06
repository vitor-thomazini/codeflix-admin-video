package vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.get;

import vitor.thomazini.codeflixadminvideo.application.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
    extends UseCase<String, CastMemberOutput>
    permits DefaultGetCastMemberByIdUseCase {
}