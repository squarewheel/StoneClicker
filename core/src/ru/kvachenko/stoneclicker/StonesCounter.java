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
    //private String[] prefix;
    private TreeMap<Integer, String> prefix;

    public StonesCounter() {
        counter = new BigInteger("0");
        //prefix = new String[]{"k", "M", "G", "T", "P", "E", "Z", "Y"};
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
        int stringLength = value.length();
        int lastCharIndex = 3;
        int grade = 27;

        if (stringLength > grade) return "too much";

        //if (stringLength > 3) {
            System.out.print("grade: " + stringLength);
            System.out.println(" prefix: " + prefix.lowerEntry(stringLength).getValue());

            for (; grade > 3; grade--) {
                if (stringLength == grade)
                    return value.substring(0, lastCharIndex) + "," +
                            value.charAt(lastCharIndex) +
                            prefix.lowerEntry(stringLength).getValue();

                lastCharIndex--;
                if (lastCharIndex <= 0) lastCharIndex = 3;
            }
        //}
//        // Millions
//        if (counter.toString().length() > 8)
//            return counter.toString().substring(0, 3) + "," + counter.toString().charAt(3) + "M";
//        else if (counter.toString().length() > 7)
//            return counter.toString().substring(0, 2) + "," + counter.toString().charAt(2) + "M";
//        else if (counter.toString().length() > 6)
//            return counter.toString().charAt(0) + "," + counter.toString().charAt(1) + "M";
//
//        // Thousands
//        else if (counter.toString().length() > 5)
//            return counter.toString().substring(0, 3) + "," + counter.toString().charAt(3) + "k";
//        else if (counter.toString().length() > 4)
//            return counter.toString().substring(0, 2) + "," + counter.toString().charAt(2) + "k";
//        else if (counter.toString().length() > 3)
//            return counter.toString().charAt(0) + "," + counter.toString().charAt(1) + "k";

        // Default
        return counter.toString();
    }

    public void addStones(BigInteger stones) {
        counter = counter.add(stones);
    }
}
