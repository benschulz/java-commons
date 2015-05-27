package de.benshu.commons.core;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

public final class Pair<A, B> implements Debuggable {
    public static <A, B> ImmutableList<Pair<A, B>> up(ImmutableList<? extends A> as, ImmutableList<? extends B> bs) {
        Preconditions.checkArgument(as.size() == bs.size());

        final ImmutableList.Builder<Pair<A, B>> builder = ImmutableList.builder();
        for (int i = 0; i < as.size(); ++i) {
            builder.add(Pair.of(as.get(i), bs.get(i)));
        }

        return builder.build();
    }

    public static <A, B> Pair<A, B> of(A a, B b) {
        return new Pair<>(a, b);
    }

    public final A a;
    public final B b;

    public Pair(A a, B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Pair<?, ?>))
            return false;

        final Pair<?, ?> other = (Pair<?, ?>) o;
        return a.equals(other.a) && b.equals(other.b);

    }

    @Override
    public int hashCode() {
        int result = a.hashCode();
        result = 31 * result + b.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return debug();
    }

    @Override
    public String debug() {
        return "(" + a + ", " + b + ")";
    }
}
