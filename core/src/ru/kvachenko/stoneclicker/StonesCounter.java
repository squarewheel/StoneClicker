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

import java.math.BigInteger;
import java.util.TreeMap;

/**
 * @author Sasha Kvachenko
 *         Created on 18.08.2016.
 *         <p>
 *         Class for store and representation big int values.
 */
public class StonesCounter {
    private BigInteger counter;
    private TreeMap<Integer, String> prefix;

    public StonesCounter() {
        counter = new BigInteger("0");
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

    public String getStones() {
        String value = counter.toString();
        int lastCharIndex = 3;
        int grade = 27;

        if (value.length() > grade) return "too much";

        // Debug
        //System.out.print("grade: " + value.length());
        //System.out.println(" prefix: " + prefix.lowerEntry(value.length()).getValue());

        for (; grade > 3; grade--) {
            if (value.length() == grade)
                return value.substring(0, lastCharIndex) + "," +
                        value.charAt(lastCharIndex) +
                        prefix.lowerEntry(value.length()).getValue();

            lastCharIndex--;
            if (lastCharIndex <= 0) lastCharIndex = 3;
        }

        return counter.toString();
    }

    public void addStones(BigInteger stones) {
        counter = counter.add(stones);
    }

    public void removeStones(BigInteger stones) { counter = counter.subtract(stones); }
}
