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

import javafx.beans.property.*;

public class FotoProps extends FotoXml {

    private final IntegerProperty nr = new SimpleIntegerProperty(0);
    private final IntegerProperty red = new SimpleIntegerProperty(0);
    private final IntegerProperty green = new SimpleIntegerProperty(0);
    private final IntegerProperty blue = new SimpleIntegerProperty(0);
    private final IntegerProperty sum = new SimpleIntegerProperty(0);

    public final Property[] properties = {nr, red, green, blue, sum};


    public int getNr() {
        return nr.get();
    }

    public void setNr(int nr) {
        this.nr.set(nr);
    }

    public IntegerProperty nrProperty() {
        return nr;
    }

    public int getRed() {
        return red.get();
    }

    public IntegerProperty redProperty() {
        return red;
    }

    public void setRed(int red) {
        this.red.set(red);
    }

    public int getGreen() {
        return green.get();
    }

    public IntegerProperty greenProperty() {
        return green;
    }

    public void setGreen(int green) {
        this.green.set(green);
    }

    public int getBlue() {
        return blue.get();
    }

    public IntegerProperty blueProperty() {
        return blue;
    }

    public void setBlue(int blue) {
        this.blue.set(blue);
    }

    public int getSum() {
        return sum.get();
    }

    public IntegerProperty sumProperty() {
        return sum;
    }

    public void setSum(int sum) {
        this.sum.set(sum);
    }

    public void setPropsFromXml() {
        setRed(Integer.parseInt(arr[COLOR_RED]));
        setGreen(Integer.parseInt(arr[COLOR_GREEN]));
        setBlue(Integer.parseInt(arr[COLOR_BLUE]));
    setSum(getRed()+getGreen()+getBlue());
    }


    public void setXmlFromProps() {
        arr[COLOR_RED] =String.valueOf(getRed());
        arr[COLOR_GREEN] =String.valueOf(getGreen());
        arr[COLOR_BLUE] =String.valueOf(getBlue());
        arr[COLOR_SUM] =String.valueOf(getSum());
    }


    @Override
    public int compareTo(Foto arg0) {
        if (arg0.getSum()==getSum()){
            return 0;
        }else if (arg0.getSum()<getSum()){
            return -1;
        }else{
            return 1;
        }
    }

}