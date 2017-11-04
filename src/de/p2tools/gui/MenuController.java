/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * https://sourceforge.net/projects/mtplayer/
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.gui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.VBox;

public class MenuController extends VBox {

    public enum StartupMode {

        Film, DOWNLOAD, ABO
    }


    private final StartupMode startupMode;

    public MenuController(StartupMode sm) {
        startupMode = sm;

        setPadding(new Insets(20, 10, 10, 10));
        setSpacing(20);
        setAlignment(Pos.TOP_CENTER);

        switch (startupMode) {
            case Film:
                new FilmMenu(this).init();
                break;
            case DOWNLOAD:
                new DownloadMenu(this).init();
                break;
        }
    }


}