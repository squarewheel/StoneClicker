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

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sasha Kvachenko
 *         Created on 26.08.2016.
 *         <p>
 *         Query result for desktop app.
 */
public class DesktopResult implements Result {
    private ResultSet result;

    public DesktopResult(ResultSet r) {
        result = r;
    }

    /**
     *  Moves the cursor froward one row from its current position.
     *  @return true if the new current row is valid; false if there are no more rows or error occur.
     */
    @Override
    public boolean next() {
        try {
            return result.next();
        }
        catch (SQLException e) {
            e.printStackTrace();
            return true;
        }
    }

    @Override
    public boolean isEmpty() {
       try {
           return (result.getRow() <= 0);
       }
       catch (SQLException e) {
           e.printStackTrace();
           return true;
       }
    }

    /**
     *  @param columnLabel the label is the name of the column.
     *  @return column index of the given column name or -1 if error occur.
     */
    @Override
    public int findColumn(String columnLabel) {
        try {
            return result.findColumn(columnLabel);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *  @param columnIndex index of the column.
     *  @return the column value; if the value is SQL NULL, the value returned is 0; -1 if error occur.
     */
    @Override
    public int getInt(int columnIndex) {
        try {
            return result.getInt(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
            return -1;
        }
    }

    /**
     *  @param columnIndex index of the column.
     *  @return the column value; if the value is SQL NULL or if error occur, the value returned null.
     */
    @Override
    public String getString(int columnIndex) {
        try {
            return result.getString(columnIndex);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
