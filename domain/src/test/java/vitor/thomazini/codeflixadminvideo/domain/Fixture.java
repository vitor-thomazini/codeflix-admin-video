package vitor.thomazini.codeflixadminvideo.domain;

import com.github.javafaker.Faker;
import vitor.thomazini.codeflixadminvideo.domain.castmember.CastMemberType;

public final class Fixture {

    private static final Faker FAKER = new Faker();

    private Fixture() {}

    public static String name() {
        return FAKER.name().fullName();
    }


    public static final class CastMembers {

        public static CastMemberType type() {
            return FAKER.options().option(CastMemberType.values());
        }
    }
}