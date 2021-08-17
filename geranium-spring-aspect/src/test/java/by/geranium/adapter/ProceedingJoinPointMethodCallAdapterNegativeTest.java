package by.geranium.adapter;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.SourceLocation;
import org.aspectj.runtime.internal.AroundClosure;
import org.junit.jupiter.api.Test;

/**
 * @author Maxim Tereshchenko
 */
class ProceedingJoinPointMethodCallAdapterNegativeTest {

    @Test
    void givenMethodWasNotFound_whenProceedingJoinPointMethodCallAdapterFrom_thenIllegalArgumentException() {
        var testJoinPoint = new TestJoinPoint(new TestSignature(ClassWithoutMethods.class, "method name"));

        assertThatThrownBy(() -> ProceedingJoinPointMethodCallAdapter.from(testJoinPoint))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Could not find method name in " +
                        "by.geranium.adapter.ProceedingJoinPointMethodCallAdapterNegativeTest$ClassWithoutMethods");
    }

    private static class TestJoinPoint implements ProceedingJoinPoint {

        private final Signature signature;

        private TestJoinPoint(Signature signature) {
            this.signature = signature;
        }

        @Override
        public void set$AroundClosure(AroundClosure arc) {
        }

        @Override
        public Object proceed() {
            return null;
        }

        @Override
        public Object proceed(Object[] args) {
            return null;
        }

        @Override
        public String toShortString() {
            return null;
        }

        @Override
        public String toLongString() {
            return null;
        }

        @Override
        public Object getThis() {
            return null;
        }

        @Override
        public Object getTarget() {
            return null;
        }

        @Override
        public Object[] getArgs() {
            return new Object[0];
        }

        @Override
        public Signature getSignature() {
            return signature;
        }

        @Override
        public SourceLocation getSourceLocation() {
            return null;
        }

        @Override
        public String getKind() {
            return null;
        }

        @Override
        public StaticPart getStaticPart() {
            return null;
        }
    }

    private static class TestSignature implements Signature {

        private final Class<?> type;
        private final String methodName;

        private TestSignature(Class<?> type, String methodName) {
            this.type = type;
            this.methodName = methodName;
        }

        @Override
        public String toShortString() {
            return null;
        }

        @Override
        public String toLongString() {
            return null;
        }

        @Override
        public String getName() {
            return methodName;
        }

        @Override
        public int getModifiers() {
            return 0;
        }

        @Override
        public Class getDeclaringType() {
            return type;
        }

        @Override
        public String getDeclaringTypeName() {
            return null;
        }
    }

    private static class ClassWithoutMethods {
    }
}