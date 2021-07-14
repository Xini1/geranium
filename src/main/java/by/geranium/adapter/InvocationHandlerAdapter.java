package by.geranium.adapter;

import by.geranium.core.Advice;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class InvocationHandlerAdapter implements InvocationHandler {

    private final Advice advice;
    private final Object original;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return advice.logMethodCall(ReflectiveMethodCall.from(original, method, args));
    }
}
