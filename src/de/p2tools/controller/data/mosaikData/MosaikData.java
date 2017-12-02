/*
 * MTPlayer Copyright (C) 2017 W. Xaver W.Xaver[at]googlemail.com
 * http://zdfmediathk.sourceforge.net/
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


package de.p2tools.controller.data.mosaikData;

import de.p2tools.mLib.configFile.ConfigFile;
import de.p2tools.mLib.configFile.Configs;
import de.p2tools.mLib.configFile.ConfigsData;
import de.p2tools.mLib.configFile.ConfigsString;

public class MosaikData extends MosaikDataProps implements ConfigsData {

    public String getTagName() {
        return "TAG";
    }

    public Configs[] getConfigsArr() {
        Configs[] arr = new Configs[]{new ConfigsString("key1", "init1", "act1"),
                new ConfigsString("key2", "init2", "act2")};
        return arr;
    }

    public MosaikData() {
        ConfigFile configFile = new ConfigFile("/tmp/usb/test");
        configFile.addConfigs(this);
        configFile.writeConfigFile();
    }
}
