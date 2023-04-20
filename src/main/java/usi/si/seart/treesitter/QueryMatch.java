package usi.si.seart.treesitter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QueryMatch {

    int id;
    int pattern_index;
    QueryCapture[] captures;

    @Override
    public String toString() {
        String joined = Stream.of(captures)
                .map(QueryCapture::toString)
                .collect(Collectors.joining(", "));
        return String.format("QueryMatch(id: %d, pattern: %d, captures: [%s])", id, pattern_index, joined);
    }
}
