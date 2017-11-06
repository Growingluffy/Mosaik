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

package de.p2tools.controller.data.fotos;

public final class FotoCollection extends FotoCollectionProps {


    public FotoCollection() {
    }

    public FotoCollection(String name) {
        setName(name);
    }

//    @Override
//    public String toString() {
//        return getName();
//    }


    public FotoCollection getCopy() {
        final FotoCollection ret = new FotoCollection();
        for (int i = 0; i < properties.length; ++i) {
            ret.properties[i].setValue(this.properties[i].getValue());
        }
        ret.setXmlFromProps();

        return ret;
    }

    public void copyToMe(FotoCollection foto) {
        for (int i = 0; i < properties.length; ++i) {
            properties[i].setValue(foto.properties[i].getValue());
        }
        setXmlFromProps();
    }
}