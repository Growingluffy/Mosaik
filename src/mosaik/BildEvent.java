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

package mosaik;

import java.util.EventObject;

public class BildEvent extends EventObject {
    // meldet eine Änderung
    private int progress;
    private int max;
    private int threads;
    private String text;

    public BildEvent(Object source, int prog, int m, String ttext, int tthreads) {
        super(source);
        progress = prog;
        max = m;
        text = ttext;
        threads = tthreads;
    }

    public String getText() {
        return text;
    }

    public int getProgress() {
        return progress;
    }

    public int getMax() {
        return max;
    }

    public int getThreads() {
        return threads;
    }

    public boolean nixLos() {
        return max == 0;
    }

}
