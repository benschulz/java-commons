package de.benshu.commons.core.streams;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Table;
import de.benshu.commons.core.Optional;

import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.stream.Collector;

import static java.util.stream.Collectors.collectingAndThen;

public class Collectors {
    private static String MULTIPLE_VALUES = "Multiple values.";
    private static String NO_VALUES = "No values.";

    public static <E> Collector<E, ?, Optional<E>> singleOrNone() {
        return Collector.of(
                AtomicReference<E>::new,
                (ref, e) -> {
                    if (!ref.compareAndSet(null, e))
                        throw new IllegalArgumentException(MULTIPLE_VALUES);
                },
                (ref1, ref2) -> {
                    if (ref1.get() == null)
                        return ref2;
                    else if (ref2.get() != null)
                        throw new IllegalArgumentException(MULTIPLE_VALUES);
                    else
                        return ref1;
                },
                ref -> Optional.from(ref.get()),
                Collector.Characteristics.UNORDERED,
                Collector.Characteristics.CONCURRENT
        );
    }

    public static <E> Collector<E, ?, E> single() {
        return collectingAndThen(singleOrNone(), r -> {
            for (E e : r)
                return e;
            throw new IllegalStateException(NO_VALUES);
        });
    }

    public static <E extends Map.Entry<? extends K, ? extends V>, K, V> Collector<E, ?, ImmutableBiMap<K, V>> biMap() {
        return biMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <E, K, V> Collector<E, ?, ImmutableBiMap<K, V>> biMap(
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableBiMap::<K, V>builder,
                (b, e) -> b.put(keyFunction.apply(e), valueFunction.apply(e)),
                (left, right) -> left.putAll(right.build()),
                ImmutableBiMap.Builder::build,
                Collector.Characteristics.UNORDERED);
    }

    public static <E extends Map.Entry<? extends K, ? extends V>, K, V> Collector<E, ?, ImmutableMap<K, V>> map() {
        return map(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <E, K, V> Collector<E, ?, ImmutableMap<K, V>> map(
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableMap::<K, V>builder,
                (b, e) -> b.put(keyFunction.apply(e), valueFunction.apply(e)),
                (left, right) -> left.putAll(right.build()),
                ImmutableMap.Builder::build,
                Collector.Characteristics.UNORDERED);
    }

    public static <E extends Map.Entry<? extends K, ? extends V>, K, V> Collector<E, ?, ImmutableMap<K, V>> orderedMap() {
        return orderedMap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <E, K, V> Collector<E, ?, ImmutableMap<K, V>> orderedMap(
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableMap::<K, V>builder,
                (b, e) -> b.put(keyFunction.apply(e), valueFunction.apply(e)),
                (left, right) -> left.putAll(right.build()),
                ImmutableMap.Builder::build);
    }

    public static <E extends Map.Entry<? extends K, ? extends V>, K, V> Collector<E, ?, ImmutableSetMultimap<K, V>> setMultimap() {
        return setMultimap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <E, K, V> Collector<E, ?, ImmutableSetMultimap<K, V>> setMultimap(
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableSetMultimap::<K, V>builder,
                (b, e) -> b.put(keyFunction.apply(e), valueFunction.apply(e)),
                (left, right) -> left.putAll(right.build()),
                ImmutableSetMultimap.Builder::build,
                Collector.Characteristics.UNORDERED);
    }

    public static <E extends Map.Entry<? extends K, ? extends V>, K, V> Collector<E, ?, ImmutableListMultimap<K, V>> listMultimap() {
        return listMultimap(Map.Entry::getKey, Map.Entry::getValue);
    }

    public static <E, K, V> Collector<E, ?, ImmutableListMultimap<K, V>> listMultimap(
            Function<? super E, ? extends K> keyFunction,
            Function<? super E, ? extends V> valueFunction) {
        return Collector.of(
                ImmutableListMultimap::<K, V>builder,
                (b, e) -> b.put(keyFunction.apply(e), valueFunction.apply(e)),
                (left, right) -> left.putAll(right.build()),
                ImmutableListMultimap.Builder::build,
                Collector.Characteristics.UNORDERED);
    }

    public static <E> Collector<E, ?, ImmutableSet<E>> set() {
        return Collector.of(
                ImmutableSet::<E>builder,
                ImmutableSet.Builder::add,
                (left, right) -> left.addAll(right.build()),
                ImmutableSet.Builder::build,
                Collector.Characteristics.UNORDERED);
    }

    public static <E> Collector<E, ?, ImmutableSet<E>> orderedSet() {
        return Collector.of(
                ImmutableSet::<E>builder,
                ImmutableSet.Builder::add,
                (left, right) -> left.addAll(right.build()),
                ImmutableSet.Builder::build);
    }

    public static <E> Collector<E, ?, ImmutableList<E>> list() {
        return Collector.of(
                ImmutableList::<E>builder,
                ImmutableList.Builder::add,
                (left, right) -> left.addAll(right.build()),
                ImmutableList.Builder::build);
    }

    public static <E extends Table.Cell<? extends R, ? extends C, ? extends V>, R, C, V> Collector<E, ?, ImmutableTable<R, C, V>> table() {
        return table(Table.Cell::getRowKey, Table.Cell::getColumnKey, Table.Cell::getValue);
    }

    public static <E, R, C, V> Collector<E, ?, ImmutableTable<R, C, V>> table(
            Function<? super E, ? extends R> rowFunction,
            Function<? super E, ? extends C> columnFunction,
            Function<? super E, ? extends V> valueFunction) {

        return Collector.<E, ImmutableTable.Builder<R, C, V>, ImmutableTable<R, C, V>>of(
                ImmutableTable::<R, C, V>builder,
                (b, e) -> b.put(rowFunction.apply(e), columnFunction.apply(e), valueFunction.apply(e)),
                (left, right) -> left.putAll(right.build()),
                ImmutableTable.Builder::build,
                Collector.Characteristics.UNORDERED);
    }
}
