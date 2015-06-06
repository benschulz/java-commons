package de.benshu.commons.core;

import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

// TODO remove in favor of java.util.Optional
public abstract class Optional<T> implements Iterable<T> {
    public static final class None<T> extends Optional<T> {
        @Override
        public ImmutableList<T> asList() {
            return ImmutableList.of();
        }

        @Override
        public ImmutableSet<T> asSet() {
            return ImmutableSet.of();
        }

        @Override
        public Optional<T> filter(Predicate<? super T> predicate) {
            return this;
        }

        @Override
        public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> mapper) {
            return none();
        }

        @Override
        public T get() {
            throw new NoSuchElementException();
        }

        @Override
        public T getOrSupply(Supplier<? extends T> fallback) {
            return fallback.get();
        }

        @Override
        public T getOrReturn(T fallback) {
            return fallback;
        }

        @Override
        public <E extends Exception> T getOrThrow(E exception) throws E {
            throw exception;
        }

        @Override
        public boolean isEqualTo(T other) {
            return false;
        }

        @Override
        public boolean isPresent() {
            return false;
        }

        @Override
        public <U> Optional<U> map(Function<? super T, ? extends U> transformer) {
            return none();
        }

        @Override
        public Optional<T> or(Supplier<Optional<T>> alternativeSupplier) {
            return alternativeSupplier.get();
        }

        @Override
        public Stream<T> stream() {
            return Stream.empty();
        }

        @Override
        public String toString() {
            return "NONE";
        }
    }

    public static final class Some<T> extends Optional<T> {
        private final T value;

        public Some(T value) {
            Preconditions.checkNotNull(value);

            this.value = value;
        }

        @Override
        public ImmutableList<T> asList() {
            return ImmutableList.of(value);
        }

        @Override
        public ImmutableSet<T> asSet() {
            return ImmutableSet.of(value);
        }

        @Override
        public Optional<T> filter(Predicate<? super T> predicate) {
            if (predicate.test(value)) {
                return this;
            } else {
                return none();
            }
        }

        @Override
        public <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> transformer) {
            for (U u : transformer.apply(value)) {
                return some(u);
            }

            return none();
        }

        @Override
        public T get() {
            return value;
        }

        @Override
        public T getOrSupply(Supplier<? extends T> fallback) {
            return get();
        }

        @Override
        public T getOrReturn(T fallback) {
            return value;
        }

        @Override
        public <E extends Exception> T getOrThrow(E exception) throws E {
            return get();
        }

        @Override
        public boolean isEqualTo(T other) {
            return value.equals(other);
        }

        @Override
        public boolean isPresent() {
            return true;
        }

        @Override
        public <U> Optional<U> map(Function<? super T, ? extends U> transformer) {
            return new Some<>(transformer.apply(value));
        }

        @Override
        public Optional<T> or(Supplier<Optional<T>> alternativeSupplier) {
            return this;
        }

        @Override
        public Stream<T> stream() {
            return Stream.of(value);
        }

        @Override
        public String toString() {
            return "SOME(" + value.toString() + ")";
        }
    }

    public static <T> Optional<T> cast(Class<? extends T> klazz, Object object) {
        if (klazz.isInstance(object)) {
            return some(klazz.cast(object));
        } else {
            return none();
        }
    }

    public static <T> Optional<T> from(java.util.Optional<T> value) {
        return from(value.orElse(null));
    }

    public static <T> Optional<T> from(T value) {
        if (value == null) {
            return none();
        } else {
            return some(value);
        }
    }

    public static <T> Optional.None<T> none() {
        return new None<>();
    }

    public static <T> Optional.None<T> none(Class<? extends T> klazz) {
        return new None<>();
    }

    public static <T> Optional.Some<T> some(T value) {
        return new Some<>(value);
    }

    private Optional() {}

    public abstract ImmutableList<T> asList();

    public abstract ImmutableSet<T> asSet();

    public abstract Optional<T> filter(Predicate<? super T> predicate);

    public abstract <U> Optional<U> flatMap(Function<? super T, ? extends Optional<? extends U>> transformer);

    public abstract T get();

    public abstract T getOrSupply(Supplier<? extends T> fallback);

    public abstract T getOrReturn(final T fallback);

    public abstract <E extends Exception> T getOrThrow(E exception) throws E;

    public abstract boolean isEqualTo(T other);

    public abstract boolean isPresent();

    public final Iterator<T> iterator() {
        return asList().iterator();
    }

    public abstract <U> Optional<U> map(Function<? super T, ? extends U> transformer);

    public abstract Optional<T> or(Supplier<Optional<T>> alternativeSupplier);

    public abstract Stream<T> stream();
}
