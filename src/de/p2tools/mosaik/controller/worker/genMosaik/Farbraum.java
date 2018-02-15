/*
 *    Copyright (C) 2008
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.p2tools.mosaik.controller.worker.genMosaik;

import de.p2tools.mosaik.controller.config.ProgData;
import de.p2tools.mosaik.controller.data.thumb.Thumb;
import de.p2tools.mosaik.controller.data.thumb.ThumbCollection;
import de.p2tools.p2Lib.tools.Log;

import java.awt.*;
import java.util.Iterator;

public class Farbraum {

    public final int FARBEN = 256;
    public boolean[][][] suchraum = new boolean[FARBEN][FARBEN][FARBEN];
    private final ProgData progData;
    private final ThumbCollection thumbCollection;

    public Farbraum(ThumbCollection thumbCollection) {
        progData = ProgData.getInstance();
        this.thumbCollection = thumbCollection;

        for (int i = 0; i < FARBEN - 1; ++i) {
            for (int k = 0; k < FARBEN - 1; ++k) {
                for (int l = 0; l < FARBEN - 1; ++l) {
                    suchraum[i][k][l] = false;
                }
            }
        }
        Iterator<Thumb> it = thumbCollection.getThumbList().iterator();
        while (it.hasNext()) {
            addFarbe(it.next());
        }
    }

    /**
     * @param c
     * @param anz
     * @return
     */
    public Thumb getThumb(Color c, int anz) {
        Thumb thumb;
        int sprung = 0;
        int max = 10;
        int r = c.getRed();
        int g = c.getGreen();
        int b = c.getBlue();
        int rMin = r, rMax = r, gMin = g, gMax = g, bMin = b, bMax = b;
        while (rMin > 0 || gMin > 0 || bMin > 0 || rMax < FARBEN - 1 || gMax < FARBEN - 1 || bMax < FARBEN - 1) {
            rMin -= sprung;
            gMin -= sprung;
            bMin -= sprung;
            rMax += sprung;
            gMax += sprung;
            bMax += sprung;
            if (rMin < 0) {
                rMin = 0;
            }
            if (gMin < 0) {
                gMin = 0;
            }
            if (bMin < 0) {
                bMin = 0;
            }
            if (rMax >= FARBEN) {
                rMax = FARBEN - 1;
            }
            if (gMax >= FARBEN) {
                gMax = FARBEN - 1;
            }
            if (bMax >= FARBEN) {
                bMax = FARBEN - 1;
            }
            for (int i = rMin; i <= rMax; ++i) {
                for (int k = gMin; k <= gMax; ++k) {
                    for (int l = bMin; l <= bMax; ++l) {
                        if (suchraum[i][k][l] == true) {
                            thumb = thumbCollection.getThumbList().getThumb(i, k, l, anz);
                            if (thumb != null) {
                                return thumb;
                            } else {
                                suchraum[i][k][l] = false;
                            }
                        }
                    }
                }
            }
            sprung += 2;
            if (sprung > max) {
                sprung = max;
            }
        }
        Log.errorLog(987120365, "Farbraum.getThumb - keine Farbe!!");
        return null;
    }

    private void addFarbe(Thumb thumb) {
        int r, g, b;
        r = thumb.getRed();
        g = thumb.getGreen();
        b = thumb.getBlue();
        suchraum[r][g][b] = true;
    }

}