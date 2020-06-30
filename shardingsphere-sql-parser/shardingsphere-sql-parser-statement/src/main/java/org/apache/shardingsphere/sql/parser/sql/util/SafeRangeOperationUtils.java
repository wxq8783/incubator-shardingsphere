/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shardingsphere.sql.parser.sql.util;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Range;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Safe range operation utility class.
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class SafeRangeOperationUtils {
    
    /**
     * Execute intersection method by safe mode.
     *
     * @param range range
     * @param connectedRange connected range
     * @return the intersection result of two ranges
     */
    public static Range<Comparable<?>> safeIntersection(final Range<Comparable<?>> range, final Range<Comparable<?>> connectedRange) {
        try {
            return range.intersection(connectedRange);
        } catch (final ClassCastException ex) {
            Comparable<?> rangeLowerEndpoint = range.hasLowerBound() ? range.lowerEndpoint() : null;
            Comparable<?> rangeUpperEndpoint = range.hasUpperBound() ? range.upperEndpoint() : null;
            Comparable<?> connectedRangeLowerEndpoint = connectedRange.hasLowerBound() ? connectedRange.lowerEndpoint() : null;
            Comparable<?> connectedRangeUpperEndpoint = connectedRange.hasUpperBound() ? connectedRange.upperEndpoint() : null;
            Class<?> clazz = getTargetNumericType(Lists.newArrayList(rangeLowerEndpoint, rangeUpperEndpoint, connectedRangeLowerEndpoint, connectedRangeUpperEndpoint));
            if (clazz == null) {
                throw ex;
            }
            Range<Comparable<?>> newRange = createTargetNumericTypeRange(range, clazz);
            Range<Comparable<?>> newConnectedRange = createTargetNumericTypeRange(connectedRange, clazz);
            return newRange.intersection(newConnectedRange);
        }
    }
    
    /**
     * Execute closed method by safe mode.
     *
     * @param lowerEndpoint lower endpoint
     * @param upperEndpoint upper endpoint
     * @return new range
     */
    public static Range<Comparable<?>> safeClosed(final Comparable<?> lowerEndpoint, final Comparable<?> upperEndpoint) {
        try {
            return Range.closed(lowerEndpoint, upperEndpoint);
        } catch (final ClassCastException ex) {
            Class<?> clazz = getTargetNumericType(Lists.newArrayList(lowerEndpoint, upperEndpoint));
            if (clazz == null) {
                throw ex;
            }
            return Range.closed(parseNumberByClazz(lowerEndpoint.toString(), clazz), parseNumberByClazz(upperEndpoint.toString(), clazz));
        }
    }
    
    /**
     * Execute contains method by safe mode.
     *
     * @param range range
     * @param endpoint endpoint
     * @return whether the endpoint is included in the range
     */
    public static boolean safeContains(final Range<Comparable<?>> range, final Comparable<?> endpoint) {
        try {
            return range.contains(endpoint);
        } catch (final ClassCastException ex) {
            Comparable<?> rangeUpperEndpoint = range.hasUpperBound() ? range.upperEndpoint() : null;
            Comparable<?> rangeLowerEndpoint = range.hasLowerBound() ? range.lowerEndpoint() : null;
            Class<?> clazz = getTargetNumericType(Lists.newArrayList(rangeLowerEndpoint, rangeUpperEndpoint, endpoint));
            if (clazz == null) {
                throw ex;
            }
            Range<Comparable<?>> newRange = createTargetNumericTypeRange(range, clazz);
            return newRange.contains(parseNumberByClazz(endpoint.toString(), clazz));
        }
    }
    
    private static Range<Comparable<?>> createTargetNumericTypeRange(final Range<Comparable<?>> range, final Class<?> clazz) {
        if (range.hasLowerBound() && range.hasUpperBound()) {
            Comparable<?> lowerEndpoint = parseNumberByClazz(range.lowerEndpoint().toString(), clazz);
            Comparable<?> upperEndpoint = parseNumberByClazz(range.upperEndpoint().toString(), clazz);
            return Range.range(lowerEndpoint, range.lowerBoundType(), upperEndpoint, range.upperBoundType());
        }
        if (!range.hasLowerBound() && !range.hasUpperBound()) {
            return Range.all();
        }
        if (range.hasLowerBound()) {
            Comparable<?> lowerEndpoint = parseNumberByClazz(range.lowerEndpoint().toString(), clazz);
            return Range.downTo(lowerEndpoint, range.lowerBoundType());
        }
        Comparable<?> upperEndpoint = parseNumberByClazz(range.upperEndpoint().toString(), clazz);
        return Range.upTo(upperEndpoint, range.upperBoundType());
    }
    
    private static Class<?> getTargetNumericType(final List<Comparable<?>> endpoints) {
        Preconditions.checkNotNull(endpoints, "getTargetNumericType param endpoints can not be null.");
        Set<Class<?>> clazzSet = endpoints.stream().filter(Objects::nonNull).map(Comparable::getClass).collect(Collectors.toSet());
        if (clazzSet.contains(BigDecimal.class)) {
            return BigDecimal.class;
        }
        if (clazzSet.contains(Double.class)) {
            return Double.class;
        }
        if (clazzSet.contains(Float.class)) {
            return Float.class;
        }
        if (clazzSet.contains(BigInteger.class)) {
            return BigInteger.class;
        }
        if (clazzSet.contains(Long.class)) {
            return Long.class;
        }
        if (clazzSet.contains(Integer.class)) {
            return Integer.class;
        }
        return null;
    }
    
    @SneakyThrows
    private static Comparable<?> parseNumberByClazz(final String number, final Class<?> clazz) {
        return (Comparable<?>) clazz.getConstructor(String.class).newInstance(number);
    }
}
