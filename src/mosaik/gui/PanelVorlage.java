/*
 * PanelVorlage.java
 *
 * Created on 6. Oktober 2007, 21:55
 */

package mosaik.gui;

import java.util.EventObject;
import mosaik.AenderungsListener;
import mosaik.daten.Daten;

/**
 *
 * @author  emil
 */
public class PanelVorlage extends javax.swing.JPanel {

    private Daten daten;
    public boolean geaendert = false;
    boolean stopBeobachter = false;
    PanelVorlage panel;

    /** Creates new form PanelVorlage
     * @param d 
     */
    public PanelVorlage(Daten d) {
        daten = d;
        panel = this;
        daten.beobAenderung.addAdListener(new BeobachterStart());
        initComponents();
        addComponentListener(new java.awt.event.ComponentAdapter() {

                             @Override
                             public void componentShown(java.awt.event.ComponentEvent evt) {
                                 isShown();
                             }

                         });
    }

    void isShown() {
        if (geaendert) {
            geaendert = false;
            neuLaden();
        }
    }

    void neuLaden() {
    }

    void neuLadenInTime() {
    }
    ////////////////////////
    // private
    ////////////////////////
    private void setGeaendert() {
        if (this.isShowing()) {
            neuLadenInTime();
        }
        geaendert = true;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    private class BeobachterStart implements AenderungsListener {

        public void tus(EventObject e) {
            setGeaendert();
        }

    }

}
