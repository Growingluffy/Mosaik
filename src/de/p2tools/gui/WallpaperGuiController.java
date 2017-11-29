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

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.data.Icons;
import de.p2tools.controller.genWallpaper.GenWallpaper;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.tools.DirFileChooser;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class WallpaperGuiController extends AnchorPane {

    private final ProgData progData;
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox vBoxCont = new VBox();
    private final Label lblDesst = new Label("Fototapete speichern");
    private final TextField txtDest = new TextField();
    private final Button btnDest = new Button("");
    private final Button btnCreate = new Button("Fototapete erstellen");


    public WallpaperGuiController() {
        progData = ProgData.getInstance();

        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);
        scrollPane.setContent(vBoxCont);

        AnchorPane.setLeftAnchor(scrollPane, 0.0);
        AnchorPane.setBottomAnchor(scrollPane, 0.0);
        AnchorPane.setRightAnchor(scrollPane, 0.0);
        AnchorPane.setTopAnchor(scrollPane, 0.0);

        initCont();
        getChildren().addAll(scrollPane);
    }

    public void isShown() {

    }


    private void initCont() {
        // DEST
        btnDest.setOnAction(event -> {
            DirFileChooser.FileChooser(ProgData.getInstance().primaryStage, txtDest);
        });
        btnDest.setGraphic(new Icons().ICON_BUTTON_FILE_OPEN);

        final Button btnHelpDest = new Button("");
        btnHelpDest.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpDest.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        txtDest.textProperty().bindBidirectional(Config.WALLPAPER_DEST_PATH.getStringProperty());
        txtDest.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(txtDest, Priority.ALWAYS);

        HBox hBoxDest = new HBox(10);
        hBoxDest.getChildren().addAll(txtDest, btnDest, btnHelpDest);


        // Thumbsize
        final Button btnHelpSlider = new Button("");
        btnHelpSlider.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSlider.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        Slider sliderSize = new Slider();
        sliderSize.setMin(5);
        sliderSize.setMax(25);

        sliderSize.setValue(Config.WALLPAPER_THUMB_PIXEL.getInt() / 10);
        IntegerProperty iProp = new SimpleIntegerProperty();
        iProp.bind(sliderSize.valueProperty());

        NumberBinding nb = Bindings.multiply(iProp, 10);
        Config.WALLPAPER_THUMB_PIXEL.getIntegerProperty().bind(nb);

        Label lblSlider = new Label("");
        lblSlider.textProperty().bind(Bindings.format("%d", Config.WALLPAPER_THUMB_PIXEL.getIntegerProperty()));


        // Anzahl Thumbs
        final Button btnHelpSliderCount = new Button("");
        btnHelpSliderCount.setGraphic(new Icons().ICON_BUTTON_HELP);
        btnHelpSliderCount.setOnAction(a -> new MTAlert().showHelpAlert("Dateimanager", HelpText.FILEMANAGER));

        Slider sliderCount = new Slider();
        sliderCount.setMin(1);
        sliderCount.setMax(100);

        sliderCount.setValue(Config.WALLPAPER_NUM_THUMBS_WIDTH.getInt() / 10);
        IntegerProperty iPropCount = new SimpleIntegerProperty();
        iPropCount.bind(sliderCount.valueProperty());

        NumberBinding nbCount = Bindings.multiply(iPropCount, 10);
        Config.WALLPAPER_NUM_THUMBS_WIDTH.getIntegerProperty().bind(nbCount);

        Label lblSliderCount = new Label("");
        lblSliderCount.textProperty().bind(Bindings.format("%d", Config.WALLPAPER_NUM_THUMBS_WIDTH.getIntegerProperty()));


        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(0));
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        GridPane.setHgrow(sliderSize, Priority.ALWAYS);
        GridPane.setHgrow(sliderCount, Priority.ALWAYS);

        gridPane.add(new Label("Größe der Miniaturbilder (Pixel):"), 0, 0);
        gridPane.add(sliderSize, 1, 0);
        gridPane.add(lblSlider, 2, 0);
        gridPane.add(btnHelpSlider, 3, 0);
        gridPane.add(new Label("Anzahl Miniaturbilder im Mosaik (Breite):"), 0, 1);
        gridPane.add(sliderCount, 1, 1);
        gridPane.add(lblSliderCount, 2, 1);
        gridPane.add(btnHelpSliderCount, 3, 1);

        // import all
        vBoxCont.setSpacing(10);
        vBoxCont.setPadding(new Insets(10));
        vBoxCont.getChildren().addAll(lblDesst, hBoxDest, gridPane, btnCreate);


        btnCreate.setOnAction(a -> {
            if (!txtDest.getText().isEmpty()) {
                new GenWallpaper(progData.selectedThumbCollection,
                        txtDest.getText(),
                        Config.WALLPAPER_NUM_THUMBS_WIDTH.getInt(),
                        Config.WALLPAPER_THUMB_PIXEL.getInt()).tus();
            }
        });

    }
}
