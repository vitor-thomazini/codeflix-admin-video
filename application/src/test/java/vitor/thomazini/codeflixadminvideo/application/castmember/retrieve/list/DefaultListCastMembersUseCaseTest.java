package vitor.thomazini.codeflixadminvideo.application.castmember.retrieve.list;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import vitor.thomazini.codeflixadminvideo.application.UseCaseTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberGateway;
import vitor.thomazini.codeflixadminvideo.domain.pagination.Pagination;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static vitor.thomazini.codeflixadminvideo.application.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.application.Fixture.name;

public class DefaultListCastMembersUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListCastMembersUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembers_shouldReturnAll() {
        // Arrange
        final var members = List.of(
                CastMember.newMember(name(), type()),
                CastMember.newMember(name(), type())
        );

        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;

        final var expectedItems = members.stream()
                .map(CastMemberListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                members
        );

        when(castMemberGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualOutput = useCase.execute(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndIsEmpty_shouldReturn() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var members = List.<CastMember>of();
        final var expectedItems = List.<CastMemberListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                members
        );

        when(castMemberGateway.findAll(any()))
                .thenReturn(expectedPagination);

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualOutput = useCase.execute(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());

        verify(castMemberGateway).findAll(eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListCastMembersAndGatewayThrowsRandomException_shouldException() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "Algo";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";

        final var expectedErrorMessage = "Gateway error";

        when(castMemberGateway.findAll(any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualException = Assertions.assertThrows(IllegalStateException.class, () -> {
            useCase.execute(aQuery);
        });

        // Assert
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        verify(castMemberGateway).findAll(eq(aQuery));
    }
}