package vitor.thomazini.codeflixadminvideo.domain.utils;

import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class CollectionUtils {

    private CollectionUtils() {
    }

    public static <IN, OUT> Set<OUT> mapTo(final Set<IN> list, final Function<IN, OUT> mapper) {
        if (list == null) {
            return null;
        }

        return list.stream()
                .map(mapper)
                .collect(Collectors.toSet());
    }
}