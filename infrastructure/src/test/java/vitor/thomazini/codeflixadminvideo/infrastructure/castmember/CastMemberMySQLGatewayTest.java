package vitor.thomazini.codeflixadminvideo.infrastructure.castmember;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import vitor.thomazini.codeflixadminvideo.MySQLGatewayTest;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMember;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberId;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;
import vitor.thomazini.codeflixadminvideo.domain.pagination.SearchQuery;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.persistence.CastMemberJpaEntity;
import vitor.thomazini.codeflixadminvideo.infrastructure.castmember.persistence.CastMemberRepository;

import java.util.List;

import static vitor.thomazini.codeflixadminvideo.domain.Fixture.CastMembers.type;
import static vitor.thomazini.codeflixadminvideo.domain.Fixture.name;

@MySQLGatewayTest
class CastMemberMySQLGatewayTest {

    @Autowired
    private CastMemberMySQLGateway castMemberGateway;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @Test
    void givenAValidCastMember_whenCallsCreate_shouldPersistIt() {
        // Arrange
        final var expectedName = name();
        final var expectedType = type();

        final var aMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = aMember.id();

        Assertions.assertEquals(0, castMemberRepository.count());

        // Act
        final var actualMember = castMemberGateway.create(CastMember.with(aMember));

        // Assert
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(aMember.createdAt(), actualMember.createdAt());
        Assertions.assertEquals(aMember.updatedAt(), actualMember.updatedAt());

        final var persistedMember = castMemberRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedId.value(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.createdAt(), persistedMember.getCreatedAt());
        Assertions.assertEquals(aMember.updatedAt(), persistedMember.getUpdatedAt());
    }

    @Test
    void givenAValidCastMember_whenCallsUpdate_shouldRefreshIt() {
        // Arrange
        final var expectedName = name();
        final var expectedType = CastMemberType.ACTOR;

        final var aMember = CastMember.newCastMember("vind", CastMemberType.DIRECTOR);
        final var expectedId = aMember.id();

        final var currentMember = castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());
        Assertions.assertEquals("vind", currentMember.getName());
        Assertions.assertEquals(CastMemberType.DIRECTOR, currentMember.getType());

        // Act
        final var actualMember = castMemberGateway.update(
                CastMember.with(aMember).update(expectedName, expectedType)
        );

        // Assert
        Assertions.assertEquals(1, castMemberRepository.count());

        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(aMember.createdAt(), actualMember.createdAt());
        Assertions.assertTrue(aMember.updatedAt().isBefore(actualMember.updatedAt()));

        final var persistedMember = castMemberRepository.findById(expectedId.value()).get();

        Assertions.assertEquals(expectedId.value(), persistedMember.getId());
        Assertions.assertEquals(expectedName, persistedMember.getName());
        Assertions.assertEquals(expectedType, persistedMember.getType());
        Assertions.assertEquals(aMember.createdAt(), persistedMember.getCreatedAt());
        Assertions.assertTrue(aMember.updatedAt().isBefore(persistedMember.getUpdatedAt()));
    }

    @Test
    void givenTwoCastMembersAndOnePersisted_whenCallsExistsByIds_shouldReturnPersistedID() {
        // Arrange
        final var aMember = CastMember.newCastMember("Vin", CastMemberType.DIRECTOR);

        final var expectedItems = 1;
        final var expectedId = aMember.id();

        Assertions.assertEquals(0, castMemberRepository.count());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        // Act
        final var actualMember = castMemberGateway.existsByIds(List.of(CastMemberId.from("123"), expectedId));

        // Assert
        Assertions.assertEquals(expectedItems, actualMember.size());
        Assertions.assertEquals(expectedId.value(), actualMember.getFirst().value());
    }


    @Test
    void givenAValidCastMember_whenCallsDeleteById_shouldDeleteIt() {
        // Arrange
        final var aMember = CastMember.newCastMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        castMemberGateway.deleteById(aMember.id());

        // Assert
        Assertions.assertEquals(0, castMemberRepository.count());
    }

    @Test
    void givenAnInvalidId_whenCallsDeleteById_shouldBeIgnored() {
        // Arrange
        final var aMember = CastMember.newCastMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        castMemberGateway.deleteById(CastMemberId.from("123"));

        // Assert
        Assertions.assertEquals(1, castMemberRepository.count());
    }

    @Test
    void givenAValidCastMember_whenCallsFindById_shouldReturnIt() {
        // Arrange
        final var expectedName = name();
        final var expectedType = type();

        final var aMember = CastMember.newCastMember(expectedName, expectedType);
        final var expectedId = aMember.id();

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        final var actualMember = castMemberGateway.findById(expectedId).get();

        // Assert
        Assertions.assertEquals(expectedId, actualMember.id());
        Assertions.assertEquals(expectedName, actualMember.name());
        Assertions.assertEquals(expectedType, actualMember.type());
        Assertions.assertEquals(aMember.createdAt(), actualMember.createdAt());
        Assertions.assertEquals(aMember.updatedAt(), actualMember.updatedAt());
    }

    @Test
    void givenAnInvalidId_whenCallsFindById_shouldReturnEmpty() {
        // Arrange
        final var aMember = CastMember.newCastMember(name(), type());

        castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        Assertions.assertEquals(1, castMemberRepository.count());

        // Act
        final var actualMember = castMemberGateway.findById(CastMemberId.from("123"));

        // Assert
        Assertions.assertTrue(actualMember.isEmpty());
    }

    @Test
    void givenEmptyCastMembers_whenCallsFindAll_shouldReturnEmpty() {
        // Arrange
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
    }

    @ParameterizedTest
    @CsvSource({
            "vin,0,10,1,1,Vin Diesel",
            "taran,0,10,1,1,Quentin Tarantino",
            "jas,0,10,1,1,Jason Momoa",
            "har,0,10,1,1,Kit Harington",
            "MAR,0,10,1,1,Martin Scorsese",
    })
    void givenAValidTerm_whenCallsFindAll_shouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // Arrange
        mockMembers();

        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Jason Momoa",
            "name,desc,0,10,5,5,Vin Diesel",
            "createdAt,asc,0,10,5,5,Kit Harington",
            "createdAt,desc,0,10,5,5,Martin Scorsese",
    })
    void givenAValidSortAndDirection_whenCallsFindAll_shouldReturnSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedName
    ) {
        // Arrange
        mockMembers();

        final var expectedTerms = "";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedName, actualPage.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Jason Momoa;Kit Harington",
            "1,2,2,5,Martin Scorsese;Quentin Tarantino",
            "2,2,1,5,Vin Diesel",
    })
    void givenAValidPagination_whenCallsFindAll_shouldReturnPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedNames
    ) {
        // Arrange
        mockMembers();

        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery =
                new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        // Act
        final var actualPage = castMemberGateway.findAll(aQuery);

        // Assert
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (final var expectedName : expectedNames.split(";")) {
            Assertions.assertEquals(expectedName, actualPage.items().get(index).name());
            index++;
        }
    }

    private void mockMembers() {
        castMemberRepository.saveAllAndFlush(List.of(
                CastMemberJpaEntity.from(CastMember.newCastMember("Kit Harington", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Vin Diesel", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Quentin Tarantino", CastMemberType.DIRECTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Jason Momoa", CastMemberType.ACTOR)),
                CastMemberJpaEntity.from(CastMember.newCastMember("Martin Scorsese", CastMemberType.DIRECTOR))
        ));
    }
}