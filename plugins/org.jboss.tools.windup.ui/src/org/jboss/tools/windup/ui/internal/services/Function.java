package org.jboss.tools.windup.ui.internal.services;

import java.util.Objects;

@FunctionalInterface
public interface Function<T> {
    void accept(T t);
    default Function<T> andThen(Function<? super T> after) {
        Objects.requireNonNull(after);
        return (T t) -> { accept(t); after.accept(t); };
    }
}
