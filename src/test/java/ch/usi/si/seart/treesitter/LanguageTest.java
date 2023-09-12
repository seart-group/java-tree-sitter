package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.nio.file.Path;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

class LanguageTest extends TestBase {

    @TempDir
    private static Path tmp;

    private static class ValidateProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            Predicate<Language> notInvalid = Predicate.not(Language._INVALID_::equals);
            return Stream.of(Language.values())
                    .filter(notInvalid)
                    .map(Arguments::of);
        }
    }

    @ParameterizedTest
    @ArgumentsSource(ValidateProvider.class)
    void testValidate(Language language) {
        Assertions.assertDoesNotThrow(() -> Language.validate(language));
    }

    private static class ValidateExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(UnsatisfiedLinkError.class, Language._INVALID_)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(ValidateExceptionProvider.class)
    void testValidateThrows(Class<Throwable> throwableType, Language language) {
        Assertions.assertThrows(throwableType, () -> Language.validate(language));
    }

    private static class AssociatedWithProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of("requirements.txt", List.of()),
                    Arguments.of("Dockerfile", List.of(Language.DOCKERFILE)),
                    Arguments.of("__init__.py", List.of(Language.PYTHON)),
                    Arguments.of("Main.java", List.of(Language.JAVA)),
                    Arguments.of("example.h", List.of(
                            Language.C,
                            Language.CPP,
                            Language.OBJECTIVE_C
                    ))
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(AssociatedWithProvider.class)
    void testAssociatedWith(String name, List<Language> expected) {
        Path path = Path.of(tmp.toString(), name);
        Collection<Language> actual = Language.associatedWith(path);
        Assertions.assertNotNull(actual);
        Assertions.assertEquals(expected.size(), actual.size());
        Assertions.assertEquals(expected, actual);
        Assertions.assertThrows(UnsupportedOperationException.class, () -> actual.add(null));
    }

    private static class AssociatedWithExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(NullPointerException.class, null),
                    Arguments.of(IllegalArgumentException.class, tmp)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(AssociatedWithExceptionProvider.class)
    void testAssociatedWithThrows(Class<Throwable> throwableType, Path path) {
        Assertions.assertThrows(throwableType, () -> Language.associatedWith(path));
    }
}