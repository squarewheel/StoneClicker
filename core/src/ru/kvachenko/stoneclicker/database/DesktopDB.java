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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.sql.*;

/**
 * @author Sasha Kvachenko
 *         Created on 26.08.2016.
 *         <p>
 *         Database connection for desktop app.
 */
public class DesktopDB extends DB {
    protected Connection connection;
    protected Statement stmt;

    public DesktopDB(String DBName) {
        super(DBName);

        String DBPath = "android/assets/database/";
        boolean newDB = true;
        // TODO: check db existing
        //FileHandle databaseFile = Gdx.files.local("android/assets/database/" + DBName + ".db");
        //FileHandle databaseFile = Gdx.files.local(DBName + ".db");
        //if (databaseFile.exists()) newDB = false;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite:" + DBPath + DBName + ".db");
            stmt = connection.createStatement();
        }
        catch (ClassNotFoundException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        catch (SQLException e) {
            System.out.println(e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }

        if (newDB) onCreate();
    }

    @Override
    public Result query(String sql) {
        try {
            return new DesktopResult(stmt.executeQuery(sql));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
