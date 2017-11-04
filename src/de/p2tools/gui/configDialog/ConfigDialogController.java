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

package de.p2tools.gui.configDialog;

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Daten;
import de.p2tools.gui.dialog.MTDialog;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;


public class ConfigDialogController extends MTDialog {

    private TabPane tabPane = new TabPane();
    private Button btnOk = new Button("Ok");

    private final Daten daten;

    public ConfigDialogController() {
        super(null, Config.CONFIG_DIALOG_SIZE, "Einstellungen", true);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10));
        vBox.setSpacing(10);

        vBox.getChildren().add(tabPane);
        VBox.setVgrow(tabPane, Priority.ALWAYS);

        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER_RIGHT);
        hBox.getChildren().add(btnOk);
        vBox.getChildren().add(hBox);

        this.daten = Daten.getInstance();
        init(vBox, true);
    }

    @Override
    public void make() {

        btnOk.setOnAction(a -> close());
        initPanel();
    }

    public void close() {
        super.close();
    }


    private void initPanel() {
        try {

            AnchorPane configPane = new ConfigPaneController();
            Tab tab = new Tab("Allgemein");
            tab.setClosable(false);
            tab.setContent(configPane);
            tabPane.getTabs().add(tab);

        } catch (final Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}