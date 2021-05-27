/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.Timer;

/**
 *
 * @author Edho
 */
public class GUI {
    static Thread t, l;
    static boolean confirmExit = false;
    
    static void DropButton(JButton butt, int locX) {
        t = new Thread() {
            public void run() {
                for (int i = butt.getX(); i < locX; i++) {
                    try {
                        t.sleep(20);
                        butt.setLocation(butt.getX() + 1, butt.getY() + 1);
                    } catch (InterruptedException ex) {}
                }
            }
        };
        t.start();
    }
    
    static void LiftButton(JButton butt, int locX) {
        if (t.isAlive()) {
            t.stop();
        }
        t = new Thread() {
            public void run() {
                for (int i = butt.getX(); i > locX; i--) {
                    try {
                        t.sleep(20);
                        butt.setLocation(butt.getX() - 1, butt.getY() - 1);
                    } catch (InterruptedException ex) {}
                }
            }
        };
        t.start();
    }
    
    static void LiftButtonAlt(JButton butt, int locX) {
        t = new Thread() {
            public void run() {
                for (int i = butt.getX(); i > locX; i--) {
                    try {
                        t.sleep(20);
                        butt.setLocation(butt.getX() - 1, butt.getY() - 1);
                    } catch (InterruptedException ex) {}
                }
            }
        };
        t.start();
    }
    
    static void ResetOverlayJalur(boolean hover) {
        for (JLabel i : Maps.Jalur) {
            if (!hover) {
                i.setEnabled(false);
                i.setVisible(false);
            } else if (hover & !i.isEnabled()) {
                i.setVisible(false);
            }
        }
        
    }
    
    static void ResetTransparencyPin() {
        for (JLabel pin: Maps.Pin) {
            pin.setIcon(Maps.PinTransparent);
        }
    }
    
    static void SetTransparencyPin(JLabel l, boolean opaque) {
        if(opaque){
            l.setIcon(Maps.PinOpaque);
        } else {
            l.setIcon(Maps.PinTransparent);
        }
    }
    
    static Timer exitmaps = new Timer(3000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            Maps.btnExit.setBackground(GUI.ButtonBgUnclicked());
            Maps.btnExit.setForeground(new Color(0,0,0));
            Maps.shBtnExit.setBackground(new Color(151,136,61));
            confirmExit = false;
            Maps.btnExit.setText("KELUAR");
        }
    });
    
    static Timer exitbonus = new Timer(3000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            confirmExit = false;
            GrafBonus.btnExit.setIcon(GrafBonus.Cross);
        }
    });
    
    static Timer tambahsimpul = new Timer(1500, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            GrafBonus.btnTambahSimpul.setIcon(GrafBonus.Plus);
        }
    });
    
    static Boolean ceknamasimpul(String input) {
        boolean valid = true;
        for (int i = 0; i < Graph.simpul.size(); i++) {
            if (Graph.simpul.get(i).nama == null ? input == null : Graph.simpul.get(i).nama.equals(input)) {
                valid = false;
            }
        }
        return valid;
    }
    
    static Timer tambahsisi = new Timer(1500, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            GrafBonus.btnTambahSisi.setIcon(GrafBonus.Plus);
        }
    });
    
    public static void UpdateCombobox() {
        DefaultComboBoxModel list = new DefaultComboBoxModel();
        for (int i = 0; i < Graph.simpul.size(); i++) {
            list.addElement(Graph.simpul.get(i).nama);
        }
        
        DefaultComboBoxModel awal = new DefaultComboBoxModel();
        for (int i = 0; i < Graph.simpul.size(); i++) {
            awal.addElement(Graph.simpul.get(i).nama);
        }
        
        DefaultComboBoxModel akhir = new DefaultComboBoxModel();
        for (int i = 0; i < Graph.simpul.size(); i++) {
            akhir.addElement(Graph.simpul.get(i).nama);
        }
        
        GrafBonus.cmbxLihatSimpul.setModel(list);
        GrafBonus.cmbxTitikAwal.setModel(awal);
        GrafBonus.cmbxTitikAkhir.setModel(akhir);
        GrafBonus.cmbxStart.setModel(awal);
        GrafBonus.cmbxEnd.setModel(akhir);
    }
    
    public static Color ButtonBgClicked() {
        return new Color(215,192,86);
    }
    
    public static Color ButtonBgUnclicked() {
        return new Color(240,215,97);
    }
    
    public static Color ButtonBgBonusMenuClicked() {
        return new Color(138,183,135);
    }
    
    public static Color ButtonBgBonusMenuUnclicked() {
        return new Color(158,206,154);
    }
    
    public static String FormatDouble(double i) {
        DecimalFormat format = new DecimalFormat("#.#");
        return format.format(i);
    }
}
