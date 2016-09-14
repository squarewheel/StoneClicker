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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.SQLException;

/**
 * @author Sasha Kvachenko
 *         Created on 13.09.2016.
 *         <p>
 *         Database connecton for android.
 */
public class AndroidDB extends DB {
    private class DBConnection extends SQLiteOpenHelper {
        public DBConnection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            stmt = db;
            // TODO: load db from dump
            System.out.println("create");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            stmt = db;
            System.out.println("upgrade");
        }
    }

    protected SQLiteOpenHelper connection;
    protected SQLiteDatabase stmt;

    public AndroidDB(Context context, String DBName) {
        super(DBName);
        connection = new DBConnection(context, this.dataBaseName, null, 1);
        stmt = connection.getReadableDatabase();
    }

    @Override
    public Result query(String sql) {
            return new AndroidResult(stmt.rawQuery(sql, null));
    }
}
