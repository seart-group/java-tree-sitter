package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Stream;

class LanguageTest extends TestBase {

    @TempDir
    private static Path tmp;

    @Test
    void testValidate() {
        Stream.of(Language.values())
                .filter(Predicate.not(Language._INVALID_::equals))
                .map(language -> (Executable) () -> Language.validate(language))
                .forEach(Assertions::assertDoesNotThrow);
    }

    @Test
    void testValidateThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> Language.validate(null));
        Assertions.assertThrows(UnsatisfiedLinkError.class, () -> Language.validate(Language._INVALID_));
    }

    @Test
    void testAssociatedWithCandidates() {
        Path path;
        Collection<Language> candidates;

        path = Path.of(tmp.toString(), "Dockerfile");
        candidates = Language.associatedWith(path);
        Assertions.assertNotNull(candidates);
        Assertions.assertEquals(1, candidates.size());
        Assertions.assertTrue(candidates.contains(Language.DOCKERFILE));

        path = Path.of(tmp.toString(), "__init__.py");
        candidates = Language.associatedWith(path);
        Assertions.assertNotNull(candidates);
        Assertions.assertEquals(1, candidates.size());
        Assertions.assertTrue(candidates.contains(Language.PYTHON));

        path = Path.of(tmp.toString(), "Main.java");
        candidates = Language.associatedWith(path);
        Assertions.assertNotNull(candidates);
        Assertions.assertEquals(1, candidates.size());
        Assertions.assertTrue(candidates.contains(Language.JAVA));

        path = Path.of(tmp.toString(), "example.h");
        candidates = Language.associatedWith(path);
        Assertions.assertNotNull(candidates);
        Assertions.assertEquals(3, candidates.size());
        Assertions.assertTrue(candidates.containsAll(Set.of(
                Language.C,
                Language.CPP,
                Language.OBJECTIVE_C
        )));
    }

    @Test
    void testAssociatedWithThrows() {
        Assertions.assertThrows(NullPointerException.class, () -> Language.associatedWith(null));
        Assertions.assertThrows(IllegalArgumentException.class, () -> Language.associatedWith(tmp));
    }
}