package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class PredicateTest extends BaseTest {

    private static final String expected = "(#any-of? @variable \"document\" \"window\" \"console\")";

    private static final Query query = Query.builder()
            .language(Language.JAVASCRIPT)
            .pattern("((identifier) @variable "+expected+")")
            .build();

    private static final Pattern pattern = query.getPatterns().stream()
            .findFirst()
            .orElseThrow();

    @AfterAll
    static void afterAll() {
        query.close();
    }

    @Test
    void test() {
        Predicate predicate = pattern.getPredicates().stream()
                .findFirst()
                .orElseGet(Assertions::fail);
        List<Predicate.Step> steps = predicate.getSteps();
        Assertions.assertFalse(steps.isEmpty());
        Assertions.assertEquals(6, steps.size());
        Assertions.assertEquals(
                Predicate.Step.Type.STRING,
                steps.stream()
                        .map(Predicate.Step::getType)
                        .findFirst()
                        .orElse(null)
        );
        Assertions.assertEquals(expected, predicate.toString());
    }
}
