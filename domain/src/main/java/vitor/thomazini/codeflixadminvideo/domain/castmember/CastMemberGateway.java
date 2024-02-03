package vitor.thomazini.codeflixadminvideo.domain.castmember;

import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    CastMember create(CastMember aCastMember);

    void deleteById(CastMemberId anId);

    Optional<CastMember> findById(CastMemberId anId);

    CastMember update(CastMember aCastMember);

    Pagination<CastMember> findAll(SearchQuery aQuery);

    List<CastMemberId> existsByIds(Iterable<CastMemberId> ids);
}