package net.c0ffee1.quartz.core.utils;

public interface Result<T, E> {
    boolean isSuccess();
    T getSuccess();
    E getFailure();

    static <T, E> Result<T, E> success(T value) {
        return new Result<>() {
            @Override
            public boolean isSuccess() {
                return true;
            }

            @Override
            public T getSuccess() {
                return value;
            }

            @Override
            public E getFailure() {
                throw new IllegalStateException("Attempted to get failure from a success");
            }
        };
    }

    static <T, E> Result<T, E> failure(E error) {
        return new Result<>() {
            @Override
            public boolean isSuccess() {
                return false;
            }

            @Override
            public T getSuccess() {
                throw new IllegalStateException("Attempted to get success from a failure");
            }

            @Override
            public E getFailure() {
                return error;
            }
        };
    }
}
