/*
 * Copyright (c) 2016 Kvachenko A. [feedback@kvachenko.ru]
 *
 * This file is part of program StoneClicker.
 *
 * This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *   See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package ru.kvachenko.stoneclicker;

import java.math.BigDecimal;
import java.util.TreeMap;

/**
 * @author Sasha Kvachenko
 *         Created on 18.08.2016.
 *         <p>
 *         Class for store and representation big int values.
 */
public class Counter {
    private static TreeMap<Integer, String> prefix;
    private BigDecimal counter;

    static {
        prefix = new TreeMap<Integer, String>();
        prefix.put(24, "Y");
        prefix.put(21, "Z");
        prefix.put(18, "E");
        prefix.put(15, "P");
        prefix.put(12, "T");
        prefix.put(9, "G");
        prefix.put(6, "M");
        prefix.put(3, "K");
        prefix.put(0, "");
    }

    public Counter(BigDecimal initialValue) {
        counter = new BigDecimal(initialValue.toString());
    }

    public Counter(int initialValue) {
        counter = new BigDecimal(initialValue);
    }

    public Counter() {
        counter = new BigDecimal("0");
    }

    /**
     *  Return shorted value of given number. E.g. 10000 will be presented as 10k.
     *  If number less than thee chars return number as is, otherwise return truncated value.
     *
     *  @param num BigDecimal number.
     *  @return shorted value as string.
     */
    public static String shortedValueOf(BigDecimal num) {
        String value = num.toBigInteger().toString();
        int lastCharIndex = 3;                          // Print up to three characters
        int grade = prefix.lastKey() + lastCharIndex;   // Maximum possible to print grade of number

        if (value.length() > grade) return "too much";

        // Debug
        //System.out.print("grade: " + value.length());
        //System.out.println(" prefix: " + prefix.lowerEntry(value.length()).getValue());

        for (; grade > 3; grade--) {
            if (value.length() == grade) {
                value = value.substring(0, lastCharIndex) + "," +
                        value.charAt(lastCharIndex) +
                        prefix.lowerEntry(value.length()).getValue();
                break;
            }
            lastCharIndex--;
            if (lastCharIndex <= 0) lastCharIndex = 3;
        }

        return value;
    }

    public BigDecimal getCounter() {
        return counter;
    }

    /**
     * @return current value of stones counter as string.
     */
    public String getValue() {
        return shortedValueOf(counter);
    }

    public void increaseValue(BigDecimal num) {
        counter = counter.add(num);
    }

    public void reduceValue(BigDecimal num) { counter = counter.subtract(num); }

    /**
     * Compare given value and counter value.
     *
     * @param val BigDecimal number to compare with counter.
     * @return -1, 0, or 1 as this BigDecimal is numerically less than, equal to, or greater than val.
     */
    //public int compareTo(BigDecimal val) { return counter.compareTo(val); }
}
