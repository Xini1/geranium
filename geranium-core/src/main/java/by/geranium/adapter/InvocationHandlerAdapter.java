package by.geranium.adapter;

import by.geranium.core.Geranium;
import lombok.RequiredArgsConstructor;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Maxim Tereshchenko
 */
@RequiredArgsConstructor
public class InvocationHandlerAdapter implements InvocationHandler {

    private final Geranium geranium;
    private final Object original;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        return geranium.logMethodCall(ReflectiveMethodCall.from(original, method, args));
    }
}
