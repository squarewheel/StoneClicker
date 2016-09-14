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

package ru.kvachenko.stoneclicker.database;

import android.database.Cursor;

/**
 * @author Sasha Kvachenko
 *         Created on 13.09.2016.
 *         <p>
 *         Query result for android app.
 */
public class AndroidResult implements Result {
    private Cursor result;

    public AndroidResult(Cursor c) {
        result = c;
    }

    /**
     *  @param columnLabel the label is the name of the column.
     *  @return column index of the given column name or -1 if error occur.
     */
    @Override
    public int findColumn(String columnLabel) {
        return result.getColumnIndex(columnLabel);
    }

    /**
     *  Moves the cursor froward one row from its current position.
     *  @return true if the new current row is valid; false if there are no more rows or error occur.
     */
    @Override
    public boolean next() {
        if (result.isBeforeFirst()) System.out.println("its fine. really");
        return result.moveToNext();
    }

    /**
     * @return true if numbers of rows in the cursor equals to 0.
     */
    @Override
    public boolean isEmpty() {
        return result.getCount()==0;
    }

    /**
     *  @param columnIndex index of the column.
     *  @return the column value.
     */
    @Override
    public String getString(int columnIndex) {
        return result.getString(columnIndex);
    }

    /**
     *  @param columnIndex index of the column.
     *  @return the column value.
     */
    @Override
    public int getInt(int columnIndex) {
        return result.getInt(columnIndex);
    }
}
