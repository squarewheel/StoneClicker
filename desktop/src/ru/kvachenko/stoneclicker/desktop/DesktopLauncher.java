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

package ru.kvachenko.stoneclicker.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.badlogic.gdx.tools.texturepacker.TexturePacker.Settings;
import ru.kvachenko.stoneclicker.StoneClicker;
import ru.kvachenko.stoneclicker.database.DesktopDB;

public class DesktopLauncher {
	public static void main (String[] arg) {
        // TexturePacker
        Settings settings = new Settings();
        settings.maxWidth = 2048;
        settings.maxHeight = 2048;
        //TexturePacker.process(settings, "android/assets/images", "android/assets", "images");
        TexturePacker.process(settings, "images", "./", "images");

		// App
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Stone Clicker";
        config.width = 320;
        config.height = 480;
		new LwjglApplication(new StoneClicker(new DesktopDB("stoneclicker")), config);
	}
}
