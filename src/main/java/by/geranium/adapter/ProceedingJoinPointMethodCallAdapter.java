package by.geranium.adapter;

import by.geranium.core.AbstractMethodCall;
import by.geranium.core.MethodCall;
import org.aspectj.lang.ProceedingJoinPoint;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.stream.IntStream;

/**
 * @author Maxim Tereshchenko
 */
public class ProceedingJoinPointMethodCallAdapter extends AbstractMethodCall {

    private static final Map<Class<?>, Class<?>> PRIMITIVE_TO_BOXED_CLASS_MAP = Map.of(
            boolean.class, Boolean.class,
            byte.class, Byte.class,
            short.class, Short.class,
            char.class, Character.class,
            int.class, Integer.class,
            long.class, Long.class,
            float.class, Float.class,
            double.class, Double.class
    );

    private final ProceedingJoinPoint proceedingJoinPoint;

    private ProceedingJoinPointMethodCallAdapter(Method method, ProceedingJoinPoint proceedingJoinPoint) {
        super(method);
        this.proceedingJoinPoint = proceedingJoinPoint;
    }

    public static MethodCall from(ProceedingJoinPoint proceedingJoinPoint) {
        return new ProceedingJoinPointMethodCallAdapter(
                Arrays.stream(proceedingJoinPoint.getSignature().getDeclaringType().getDeclaredMethods())
                        .filter(method -> method.getName().equals(proceedingJoinPoint.getSignature().getName()))
                        .filter(method -> method.getParameterTypes().length == proceedingJoinPoint.getArgs().length)
                        .filter(method -> IntStream.range(0, method.getParameterTypes().length)
                                .filter(index -> Objects.nonNull(proceedingJoinPoint.getArgs()[index]))
                                .allMatch(
                                        index -> isArgumentTypeAssignableFromActualType(
                                                proceedingJoinPoint,
                                                method,
                                                index)
                                )
                        )
                        .findAny()
                        .orElseThrow(IllegalArgumentException::new),
                proceedingJoinPoint
        );
    }

    private static boolean isArgumentTypeAssignableFromActualType(
            ProceedingJoinPoint proceedingJoinPoint,
            Method method,
            int index
    ) {
        return PRIMITIVE_TO_BOXED_CLASS_MAP.getOrDefault(
                method.getParameterTypes()[index],
                method.getParameterTypes()[index]
        )
                .isAssignableFrom(proceedingJoinPoint.getArgs()[index].getClass());
    }

    @Override
    public Class<?> getTargetClass() {
        return proceedingJoinPoint.getTarget().getClass();
    }

    @Override
    public Object proceed() throws Throwable {
        return proceedingJoinPoint.proceed();
    }

    @Override
    protected Object[] getArguments() {
        return proceedingJoinPoint.getArgs();
    }
}
