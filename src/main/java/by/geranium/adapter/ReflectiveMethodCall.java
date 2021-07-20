package by.geranium.adapter;

import by.geranium.core.AbstractMethodCall;
import by.geranium.core.MethodCall;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author Maxim Tereshchenko
 */
public class ReflectiveMethodCall extends AbstractMethodCall {

    private final Object object;
    private final Object[] arguments;

    private ReflectiveMethodCall(Object object, Method method, Object[] arguments) {
        super(method);
        this.object = object;
        this.arguments = arguments;
    }

    public static MethodCall from(Object object, Method interfaceMethod, Object[] arguments) {
        return new ReflectiveMethodCall(
                object,
                Arrays.stream(object.getClass().getDeclaredMethods())
                        .filter(classMethod -> classMethod.getName().equals(interfaceMethod.getName()))
                        .filter(classMethod -> Arrays.equals(
                                classMethod.getParameterTypes(),
                                interfaceMethod.getParameterTypes()
                        ))
                        .findAny()
                        .orElseThrow(IllegalArgumentException::new),
                arguments
        );
    }

    @Override
    public Class<?> getTargetClass() {
        return object.getClass();
    }

    @Override
    public Object proceed() throws Throwable {
        try {
            return getInvokedMethod().invoke(object, arguments);
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Override
    protected Object[] getArguments() {
        return arguments;
    }
}
