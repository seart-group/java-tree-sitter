package ch.usi.si.seart.treesitter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.stream.Stream;

class RangeTest extends BaseTest {

    @Test
    void testBuilder() {
        Range expected = new Range(0, 1, _0_0_, _1_1_);
        Range actual = Range.builder()
                .startByte(0)
                .endByte(1)
                .startPoint(_0_0_)
                .endPoint(_1_1_)
                .build();
        Assertions.assertEquals(expected, actual);
        Range modified = actual.toBuilder()
                .endByte(2)
                .endPoint(_2_2_)
                .build();
        Assertions.assertNotEquals(expected, modified);
    }

    private static class BuilderExceptionProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            Point negativeRow = new Point(-1, 0);
            Point negativeCol = new Point(0, -1);
            Executable setNegativeStartByte = () -> Range.builder().startByte(-1);
            Executable setNegativeEndByte = () -> Range.builder().endByte(-1);
            Executable setNullStartPoint = () -> Range.builder().startPoint(null);
            Executable setNullEndPoint = () -> Range.builder().endPoint(null);
            Executable setNegativeStartPointRow = () -> Range.builder().startPoint(negativeRow);
            Executable setNegativeEndPointRow = () -> Range.builder().endPoint(negativeRow);
            Executable setNegativeStartPointCol = () -> Range.builder().startPoint(negativeCol);
            Executable setNegativeEndPointCol = () -> Range.builder().endPoint(negativeCol);
            Executable buildReversedByteRange = () -> Range.builder().startByte(2).endByte(0).build();
            Executable buildReversedPointRange = () -> Range.builder().startPoint(_1_1_).endPoint(_0_0_).build();
            return Stream.of(
                    Arguments.of(IllegalArgumentException.class, setNegativeStartByte),
                    Arguments.of(IllegalArgumentException.class, setNegativeEndByte),
                    Arguments.of(NullPointerException.class, setNullStartPoint),
                    Arguments.of(NullPointerException.class, setNullEndPoint),
                    Arguments.of(IllegalArgumentException.class, setNegativeStartPointRow),
                    Arguments.of(IllegalArgumentException.class, setNegativeEndPointRow),
                    Arguments.of(IllegalArgumentException.class, setNegativeStartPointCol),
                    Arguments.of(IllegalArgumentException.class, setNegativeEndPointCol),
                    Arguments.of(IllegalArgumentException.class, buildReversedByteRange),
                    Arguments.of(IllegalArgumentException.class, buildReversedPointRange)
            );
        }
    }

    @ParameterizedTest(name = "[{index}] {0}")
    @ArgumentsSource(BuilderExceptionProvider.class)
    void testBuilderThrows(Class<Throwable> exception, Executable runnable) {
        Assertions.assertThrows(exception, runnable);
    }
}
