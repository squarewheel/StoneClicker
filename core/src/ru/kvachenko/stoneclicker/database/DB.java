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

/**
 * @author Sasha Kvachenko
 *         Created on 26.08.2016.
 *         <p>
 *         Class for working with DB.
 *         [ http://stackoverflow.com/questions/15874824/using-a-sqlite-database-in-libgdx ]
 */
public abstract class DB {
    protected String dataBaseName;

    public DB(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    public abstract Result query(String sql);
}
