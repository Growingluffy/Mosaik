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


package de.p2tools.mLib.configFile;

import de.p2tools.mLib.configFile.config.Config;
import de.p2tools.mLib.configFile.config.ConfigConfigsList;
import de.p2tools.mLib.tools.Duration;
import de.p2tools.mLib.tools.Log;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

class LoadConfigFile implements AutoCloseable {

    private XMLInputFactory inFactory;
    private final Path xmlFilePath;

    private final ArrayList<ConfigsList> configsListArr;
    private final ArrayList<ConfigsData> configsDataArr;

    LoadConfigFile(Path filePath, ArrayList<ConfigsList> configsListArrayList, ArrayList<ConfigsData> configsDataArr) {
        this.xmlFilePath = filePath;
        this.configsListArr = configsListArrayList;
        this.configsDataArr = configsDataArr;

        inFactory = XMLInputFactory.newInstance();
        inFactory.setProperty(XMLInputFactory.IS_COALESCING, Boolean.FALSE);
    }

    boolean readConfiguration() {
        Duration.counterStart("Konfig lesen");
        boolean ret = false;

        if (!Files.exists(xmlFilePath)) {
            Duration.counterStop("Konfig lesen");
            return ret;
        }

        XMLStreamReader parser = null;
        try (InputStream is = Files.newInputStream(xmlFilePath);
             InputStreamReader in = new InputStreamReader(is, StandardCharsets.UTF_8)) {

            parser = inFactory.createXMLStreamReader(in);

            nextTag:
            while (parser.hasNext()) {
                final int event = parser.next();
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue nextTag;
                }

                String xmlElem = parser.getLocalName();
                for (ConfigsList list : configsListArr) {
                    if (list.getTagName().equals(xmlElem)) {
                        getConfigsList(parser, list);
                        continue nextTag;
                    }
                }

                for (ConfigsData configsData : configsDataArr) {
                    if (configsData.getTagName().equals(xmlElem)) {
                        getConfigData(parser, configsData);
                        continue nextTag;
                    }
                }

            }
            ret = true;

        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(732160795, ex);
        } finally {
            try {
                if (parser != null) {
                    parser.close();
                }
            } catch (final Exception ignored) {
            }
        }

        Duration.counterStop("Konfig lesen");
        return ret;
    }


    private boolean getConfigsList(XMLStreamReader parser, ConfigsList configsList) {
        boolean ret = false;
        final String configsListTagName = configsList.getTagName();

        try {
            ConfigsData configsData = configsList.getNewItem();
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(configsListTagName)) {
                    break;
                }
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                String s = parser.getLocalName();
                if (!configsData.getTagName().equals(s)) {
                    continue;
                }
                if (getConfigData(parser, configsData)) {
                    ret = true;
                    configsList.addNewItem(configsData);
                    configsData = configsList.getNewItem();
                }
            }
        } catch (final Exception ex) {
            ret = false;
            Log.errorLog(302104541, ex);
        }

        return ret;
    }

    private boolean getConfigData(XMLStreamReader parser, ConfigsData configsData) {
        boolean ret = false;
        String xmlElem = parser.getLocalName();

        try {
            while (parser.hasNext()) {
                final int event = parser.next();

                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
                    break;
                }
                if (event != XMLStreamConstants.START_ELEMENT) {
                    continue;
                }

                final String s = parser.getLocalName();
                for (Config config : configsData.getConfigsArr()) {
                    if (config.getKey().equals(s) && config.getClass().equals(ConfigConfigsList.class)) {
                        System.out.println(s + " - Config: " + config.getKey());

                        ConfigsList<? extends ConfigsData> ol = ((ConfigConfigsList) config).getActValue();
                        ConfigsData cd = ol.getNewItem();
                        ol.addNewItem(cd);
                        getConfigData(parser, cd);

                    } else if (config.getKey().equals(s)) {
                        final String n = parser.getElementText();
                        System.out.println(n + " - Config: " + config.getActValueString());
                        config.setActValue(n);
                        System.out.println(n + " - Config: " + config.getActValueString());

                    }
                }

            }
            ret = true;

        } catch (final Exception ex) {
            Log.errorLog(302104541, ex);
        }

        return ret;
    }

//    private boolean getConfigList(XMLStreamReader parser, ConfigConfigsList configList) {
//        boolean ret = false;
//        String xmlElem = parser.getLocalName();
//
//        ConfigsList<? extends ConfigsData> ol = configList.getActValue();
//        ConfigsData configsData = ol.getNewItem();
//        ol.addNewItem(configsData);
//
//        try {
//            while (parser.hasNext()) {
//                final int event = parser.next();
//
//                if (event == XMLStreamConstants.END_ELEMENT && parser.getLocalName().equals(xmlElem)) {
//                    break;
//                }
//                if (event != XMLStreamConstants.START_ELEMENT) {
//                    continue;
//                }
//
//                final String s = parser.getLocalName();
//                final String n = parser.getElementText();
//
//                for (Config config : configsData.getConfigsArr()) {
//                    if (config.getKey().equals(s) && config.getClass().equals(ConfigConfigsList.class)) {
//
////                        if (getConfigData(parser, configsData)) {
////                            ConfigsData cd = ((ConfigConfigsList) config).getNewItem();
////                            ((ConfigConfigsList) config).getActValue().add(cd);
////                        }
//
//                    } else if (config.getKey().equals(s)) {
//                        System.out.println(n + " - Config " + config.getActValueString());
//                        config.setActValue(n);
//                        System.out.println(n + " - Config " + config.getActValueString());
//                    }
//
//                }
//
//            }
//            ret = true;
//
//        } catch (
//                final Exception ex) {
//            Log.errorLog(302104541, ex);
//        }
//
//        return ret;
//    }

    @Override
    public void close() throws Exception {
    }

}