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

package de.p2tools.mosaik;

import de.p2tools.mosaik.controller.ProgQuitt;
import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.Icons;
import de.p2tools.mosaik.gui.*;
import de.p2tools.mosaik.gui.dialog.AboutDialogController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;

public class MosaikController extends StackPane {

    Button btnStart = new Button("Start");
    Button btnThumbNail = new Button("Miniaturbilder\n" + "erstellen");
    Button btnMosaik = new Button("Mosaik\n" + "erstellen");
    Button btnPrev = new Button("");
    Button btnNext = new Button("");

    MenuButton menuButton = new MenuButton("");

    BorderPane borderPane = new BorderPane();
    StackPane stackPaneCont = new StackPane();
    private StatusBarController statusBarController;

    private final ProgData progData;


    public MosaikController() {
        progData = ProgData.getInstance();
        init();
    }

    private void init() {
        try {
            // Top-Menü
            HBox hBoxMenueButton = new HBox();
            hBoxMenueButton.setPadding(new Insets(10));
            hBoxMenueButton.setSpacing(20);
            hBoxMenueButton.setAlignment(Pos.CENTER);
            HBox.setHgrow(hBoxMenueButton, Priority.ALWAYS);

            TilePane tilePane = new TilePane();
            tilePane.setHgap(20);
            tilePane.setAlignment(Pos.CENTER);
            HBox.setHgrow(tilePane, Priority.ALWAYS);

            tilePane.getChildren().addAll(btnStart, btnThumbNail, btnMosaik);
            hBoxMenueButton.getChildren().addAll(tilePane, menuButton);

            btnStart.setOnAction(e -> selPanelStart());
            btnStart.setMaxWidth(Double.MAX_VALUE);

            btnThumbNail.setOnAction(e -> selPanelTumb());
            btnThumbNail.setMaxWidth(Double.MAX_VALUE);

            btnMosaik.setOnAction(e -> selPanelMosaik());
            btnMosaik.setMaxWidth(Double.MAX_VALUE);


            // Menü
            final MenuItem miQuitt = new MenuItem("Beenden");
            miQuitt.setOnAction(e -> new ProgQuitt().beenden(true));

            final MenuItem miAbout = new MenuItem("Über dieses Programm");
            miAbout.setOnAction(event -> new AboutDialogController(progData));

            menuButton.getStyleClass().add("btnFunction");
            menuButton.setText("");
            menuButton.setGraphic(new Icons().FX_ICON_TOOLBAR_MENUE_TOP);
            menuButton.getItems().addAll(miAbout, miQuitt);


            // Panes
            progData.guiStart = new GuiStart();

            progData.guiThumbController = new GuiThumbController();
            progData.guiThumbChangeController = new GuiThumbChangeController();
            progData.guiThumb = new GuiThumb();

            progData.guiMosaik = new GuiMosaik();

            stackPaneCont.getChildren().addAll(progData.guiStart, progData.guiThumb, progData.guiMosaik);

            // Statusbar
            statusBarController = new StatusBarController(progData);


            // Button NEXT-PREV
            HBox hBoxPrev = new HBox();
            hBoxPrev.getChildren().addAll(btnPrev);
            hBoxPrev.setAlignment(Pos.CENTER);
            btnPrev.setMaxHeight(Double.MAX_VALUE);
            btnPrev.setGraphic(new Icons().ICON_BUTTON_GUI_PREV);
            btnPrev.setOnAction(a -> setPrev());
            HBox.setHgrow(btnPrev, Priority.ALWAYS);

            HBox hBoxNext = new HBox();
            hBoxNext.getChildren().addAll(btnNext);
            hBoxNext.setAlignment(Pos.CENTER);
            btnNext.setMaxHeight(Double.MAX_VALUE);
            btnNext.setGraphic(new Icons().ICON_BUTTON_GUI_NEXT);
            btnNext.setOnAction(a -> setNext());
            HBox.setHgrow(btnNext, Priority.ALWAYS);

            // ProgGUI
            borderPane.setTop(hBoxMenueButton);
            borderPane.setCenter(stackPaneCont);
            borderPane.setBottom(statusBarController);
            borderPane.setLeft(hBoxPrev);
            borderPane.setRight(hBoxNext);

            this.setPadding(new Insets(0));
            this.getChildren().addAll(borderPane);

            selPanelStart();
            addStartGuiBinding();

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    private void addStartGuiBinding() {
        btnStart.disableProperty().bind(progData.guiStart.allOkProperty().not());
        btnThumbNail.disableProperty().bind(progData.guiStart.allOkProperty().not());
        btnMosaik.disableProperty().bind(progData.guiStart.allOkProperty().not());

        if (!progData.guiStart.isAllOk()) {
            btnNext.setDisable(true);
            btnPrev.setDisable(true);
        }

        progData.guiStart.allOkProperty().addListener(l -> {
            if (!progData.guiStart.isAllOk()) {
                btnNext.setDisable(true);
                btnPrev.setDisable(true);
            } else {
                selPanelStart();
            }
        });
    }

    private void setPrev() {
        Node front = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
        if (front.equals(progData.guiStart)) {

        } else if (front.equals(progData.guiThumb)) {
            selPanelStart();
        } else if (front.equals(progData.guiMosaik)) {
            selPanelTumb();
        }
    }

    private void setNext() {
        Node front = stackPaneCont.getChildren().get(stackPaneCont.getChildren().size() - 1);
        if (front.equals(progData.guiStart)) {
            selPanelTumb();
        } else if (front.equals(progData.guiThumb)) {
            selPanelMosaik();
        }
    }

    private void selPanelStart() {
        btnPrev.setDisable(true);
        btnNext.setDisable(false);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab-sel");
        btnThumbNail.getStyleClass().add("btnTab");
        btnMosaik.getStyleClass().add("btnTab");

        progData.guiStart.toFront();
        progData.guiStart.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Start);
    }

    private void selPanelTumb() {
        btnPrev.setDisable(false);
        btnNext.setDisable(false);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab");
        btnThumbNail.getStyleClass().add("btnTab-sel");
        btnMosaik.getStyleClass().add("btnTab");

        progData.guiThumb.toFront();
        progData.guiThumb.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Thumb);
    }

    private void selPanelMosaik() {
        btnPrev.setDisable(false);
        btnNext.setDisable(true);

        btnStart.getStyleClass().clear();
        btnThumbNail.getStyleClass().clear();
        btnMosaik.getStyleClass().clear();

        btnStart.getStyleClass().add("btnTab");
        btnThumbNail.getStyleClass().add("btnTab");
        btnMosaik.getStyleClass().add("btnTab-sel");

        progData.guiMosaik.toFront();
        progData.guiMosaik.isShown();
        statusBarController.setStatusbarIndex(StatusBarController.StatusbarIndex.Mosaik);
    }
}
