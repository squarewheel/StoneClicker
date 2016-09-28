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
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import sun.misc.IOUtils;
import sun.security.tools.policytool.Resources_es;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;

/**
 * @author Sasha Kvachenko
 *         Created on 13.09.2016.
 *         <p>
 *         Database connecton for android.
 */
public class AndroidDB extends DB {
    private Context context;
    protected SQLiteOpenHelper connection;
    protected SQLiteDatabase stmt;

    private class DBConnection extends SQLiteOpenHelper {
        public DBConnection(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            //Gdx.app.log("DB", "Connection created.");
            System.out.println("HELLO.");
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            stmt = db;
            String sqlQuery = "";
            InputStreamReader is;
            // TODO: load db from dump
            //FileHandle sqlDump = Gdx.files.internal("database/stoneclicker.sql");
            //String sqlQuery = sqlDump.readString();
            //stmt.execSQL(Gdx.files.internal("database/stoneclicker.sql").readString());
            try {
                //is = new InputStreamReader(context.getAssets().open("database/stoneclicker.sql"));
                //BufferedReader reader = new BufferedReader(is);
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(context.getAssets().open("database/stoneclicker.sql")));
                String line;
                //while ((line = reader.readLine()) != null) sqlQuery = sqlQuery + "\n" + line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    stmt.execSQL(line);
                }
            }
            catch (IOException e) {
                e.printStackTrace();
            }

            //System.out.println(sqlQuery);

            //stmt.execSQL(sqlDump.readString());
            //stmt.execSQL(sqlQuery);

            System.out.println("created db: " + stmt.getPath());
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            System.out.println("upgrade");
            stmt = db;
        }
    }

    public AndroidDB(Context context, String DBName) {
        super(DBName);
        this.context = context;
        connection = new DBConnection(context, this.dataBaseName + ".db", null, 1);
        stmt = connection.getWritableDatabase();

        System.out.println("writable db: " + stmt.getPath());
    }

    @Override
    public Result query(String sql) {
            return new AndroidResult(stmt.rawQuery(sql, null));
    }
}
