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

package de.p2tools.controller;

import de.p2tools.controller.config.Config;
import de.p2tools.controller.config.Const;
import de.p2tools.controller.config.ProgData;
import de.p2tools.controller.config.ProgInfos;
import de.p2tools.gui.dialog.MTAlert;
import de.p2tools.mLib.configFile.ConfigFile;
import de.p2tools.mLib.tools.Log;
import de.p2tools.mLib.tools.SysMsg;
import javafx.application.Platform;
import org.apache.commons.lang3.time.FastDateFormat;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class ProgSave {
    final ProgData progData;
    private boolean alreadyMadeBackup = false;
    private boolean open = true;

    public ProgSave() {
        progData = ProgData.getInstance();
    }


    private void save() {
        ConfigFile configFile = new ConfigFile("/tmp/usb/test");
        configFile.addConfigs(Config.getTagName(), Config.getConfigsArr());
        configFile.addConfigs(progData.mosaikData);
        configFile.addConfigs(progData.wallpaperData);
        configFile.addConfigs(progData.thumbCollectionList);
        configFile.writeConfigFile();

        configFile.readConfigFile(new ArrayList<>(Arrays.asList(progData.mosaikData, progData.wallpaperData)));
    }

    public void allesSpeichern() {
        save();

        konfigCopy();
        try (IoXmlSchreiben writer = new IoXmlSchreiben(progData)) {
            writer.datenSchreiben();
        } catch (final Exception ex) {
            ex.printStackTrace();
        }

        if (ProgData.reset) {
            // das Programm soll beim nächsten Start mit den Standardeinstellungen gestartet werden
            // dazu wird den Ordner mit den Einstellungen umbenannt
            String dir1 = ProgInfos.getSettingsDirectory_String();
            if (dir1.endsWith(File.separator)) {
                dir1 = dir1.substring(0, dir1.length() - 1);
            }

            try {
                final Path path1 = Paths.get(dir1);
                final String dir2 = dir1 + "--" + FastDateFormat.getInstance("yyyy.MM.dd__HH.mm.ss").format(new Date());

                Files.move(path1, Paths.get(dir2), StandardCopyOption.REPLACE_EXISTING);
                Files.deleteIfExists(path1);
            } catch (final IOException e) {
                SysMsg.sysMsg("Die Einstellungen konnten nicht zurückgesetzt werden.");
                Platform.runLater(() -> {
                    new MTAlert().showErrorAlert("Fehler", "Einstellungen zurückgesetzen",
                            "Die Einstellungen konnten nicht zurückgesetzt werden.\n\n"
                                    + "Sie müssen jetzt das Programm beenden, dann den Ordner:\n\n"
                                    + ProgInfos.getSettingsDirectory_String()
                                    + "\n\n"
                                    + "von Hand löschen und das Programm wieder starten.");
                    open = false;
                });
                while (open) {
                    try {
                        wait(100);
                    } catch (final Exception ignored) {
                    }
                }
                Log.errorLog(465690123, e);
            }
        }
    }

    /**
     * Create backup copies of settings file.
     */
    private void konfigCopy() {
        if (!alreadyMadeBackup) {
            // nur einmal pro Programmstart machen
            SysMsg.sysMsg("-------------------------------------------------------");
            SysMsg.sysMsg("Einstellungen sichern");

            try {
                final Path xmlFilePath = new ProgInfos().getXmlFilePath();
                long creatTime = -1;

                Path xmlFilePathCopy_1 = ProgInfos.getSettingsDirectory().resolve(Const.CONFIG_FILE_COPY + 1);
                if (Files.exists(xmlFilePathCopy_1)) {
                    final BasicFileAttributes attrs = Files.readAttributes(xmlFilePathCopy_1, BasicFileAttributes.class);
                    final FileTime d = attrs.lastModifiedTime();
                    creatTime = d.toMillis();
                }

                if (creatTime == -1 || creatTime < getHeute_0Uhr()) {
                    // nur dann ist die letzte Kopie älter als einen Tag
                    for (int i = Const.MAX_COPY_BACKUPFILE; i > 1; --i) {
                        xmlFilePathCopy_1 = ProgInfos.getSettingsDirectory().resolve(Const.CONFIG_FILE_COPY + (i - 1));
                        final Path xmlFilePathCopy_2 = ProgInfos.getSettingsDirectory().resolve(Const.CONFIG_FILE_COPY + i);
                        if (Files.exists(xmlFilePathCopy_1)) {
                            Files.move(xmlFilePathCopy_1, xmlFilePathCopy_2, StandardCopyOption.REPLACE_EXISTING);
                        }
                    }
                    if (Files.exists(xmlFilePath)) {
                        Files.move(xmlFilePath,
                                ProgInfos.getSettingsDirectory().resolve(Const.CONFIG_FILE_COPY + 1),
                                StandardCopyOption.REPLACE_EXISTING);
                    }
                    SysMsg.sysMsg("Einstellungen wurden gesichert");
                } else {
                    SysMsg.sysMsg("Einstellungen wurden heute schon gesichert");
                }
            } catch (final IOException e) {
                SysMsg.sysMsg("Die Einstellungen konnten nicht komplett gesichert werden!");
                Log.errorLog(795623147, e);
            }

            alreadyMadeBackup = true;
            SysMsg.sysMsg("-------------------------------------------------------");
        }
    }

    /**
     * Return the number of milliseconds from today´s midnight.
     *
     * @return Number of milliseconds from today´s midnight.
     */
    private long getHeute_0Uhr() {
        final Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        return cal.getTimeInMillis();
    }
}
