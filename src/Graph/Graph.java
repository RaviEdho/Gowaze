/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JLabel;

/**
 *
 * @author Edho
 */
class Graph {
    static int jumlahSimpul;
    static int terkunjung;
    static int indeksDikunjungiSekarang;
    static int jarakRootKeSekarang;
    static double jarakfinal;
    static int waktutempuh, jam, menit;
    static int titikawal = -1, titikakhir = -1;
    static boolean pinpoint = false;
    static ArrayList<ArrayList<Integer>> adjM;
    static ArrayList<ArrayList<Integer>> indeksEdge;
    static ArrayList<Simpul> simpul;
    static ArrayList<Path> path;
    static ArrayList<Integer> jalur;
    
    public Graph() {
        jumlahSimpul = 0; terkunjung = 0;
        simpul = new ArrayList<>();
        adjM = new ArrayList<>();
        indeksEdge = new ArrayList<>();
        path = new ArrayList<>();
        jalur = new ArrayList<>();
    }
    
    public static void flush() {
        jumlahSimpul = 0; terkunjung = 0;
        simpul.clear();
        adjM.clear();
        indeksEdge.clear();
        path.clear();
        jalur.clear();
        pinpoint = false;
        titikawal = -1; titikakhir = -1;
    }
    
    public static void preload() {
        tambahSimpul("Serpong"); tambahSimpul("Pluit"); tambahSimpul("Tanjung Priok");
        tambahSimpul("Meruya"); tambahSimpul("Tomang"); tambahSimpul("Pulomas");
        tambahSimpul("Cakung Timur"); tambahSimpul("Pejompongan"); tambahSimpul("Senayan");
        tambahSimpul("Veteran"); tambahSimpul("Cawang"); tambahSimpul("Halim Utama");
        tambahSimpul("Cikunir"); tambahSimpul("Ciputat"); tambahSimpul("Dukuh");
        
        tambahSisi(0, 1, 2, 13300, true);
        tambahSisi(1, 2, 6, 14400, true);
        tambahSisi(2, 1, 9, 22500, true);
        tambahSisi(3, 1, 3, 14900, true);
        tambahSisi(4, 1, 4, 7100, true);
        tambahSisi(5, 3, 4, 5800, true);
        tambahSisi(6, 0, 9, 11000, true);
        tambahSisi(7, 4, 7, 3200, true);
        tambahSisi(8, 7, 8, 1200, true);
        tambahSisi(9, 2, 5, 6200, true);
        tambahSisi(10, 6, 12, 9100, true);
        tambahSisi(11, 5, 10, 9500, true);
        tambahSisi(12, 5, 11, 8400, true);
        tambahSisi(13, 8, 10, 6900, true);
        tambahSisi(14, 11, 12, 7900, true);
        tambahSisi(15, 12, 14, 13000, true);
        tambahSisi(16, 5, 14, 14100, true);
        tambahSisi(17, 10, 14, 9200, true);
        tambahSisi(18, 11, 14, 8200, true);
        tambahSisi(19, 7, 14, 22900, true);
        tambahSisi(20, 7, 13, 11300, true);
        tambahSisi(21, 13, 14, 12800, true);
        tambahSisi(22, 9, 13, 2800, true);
        tambahSisi(23, 10, 11, 3400, true);
        tambahSisi(24, 3, 9, 9900, true);
    }
    
    public static void tambahSimpul(String inputnama) {
        simpul.add(jumlahSimpul++, new Simpul(inputnama));
        indeksEdge.add(new ArrayList<>());
        adjM.add(new ArrayList<>());
        for (int i = 0; i < jumlahSimpul; i++) {
            adjM.get(jumlahSimpul-1).add(i, 1000000000);
            adjM.get(i).add(jumlahSimpul-1, 1000000000);
            indeksEdge.get(jumlahSimpul-1).add(i, 100000000);
            indeksEdge.get(i).add(jumlahSimpul-1, 100000000);
        }
    }
    
    public static void tambahSisi(int indeks, int awal, int akhir, int jarak, boolean duaarah) {
        adjM.get(awal).set(akhir, jarak);
        indeksEdge.get(awal).set(akhir, indeks);
        if(duaarah) {
            adjM.get(akhir).set(awal, jarak);
            indeksEdge.get(akhir).set(awal, indeks);
        }
    }
    
    public static void carijarak(int start) {  //menghitung jarak terdekat dari semua simpul ke simpul start
        path.clear();
        int awal = start;
        simpul.get(awal).terkunjung = true;     // kunjungi simpul start
        terkunjung = 1;
        
        for(int i = 0; i < jumlahSimpul; i++) { // buat list sementara jarak setiap simpul dari start
            path.add(new Path(awal, adjM.get(awal).get(i)));
        }
        
        while (terkunjung < jumlahSimpul) {     // ulangi hingga setiap simpul sudah dikunjungi
            int idxTerdekat = indexBelumDikunjungi();   // mencari indeks simpul terdekat yang belum dikunjungi
            indeksDikunjungiSekarang = idxTerdekat;
            jarakRootKeSekarang = path.get(idxTerdekat).jarak;  // panggil jarak dari start ke simpul terdekat untuk dibandingkan
            simpul.get(idxTerdekat).terkunjung = true;  // kunjungi indeks simpul terdekat yang belum dikunjungi
            terkunjung++;
            updatePath();   // analisis jarak tetangga-tetangga dari simpul yang dikunjungi
        }
        
        terkunjung = 0;     // setelah selesai dan semua simpul dikunjungi, reset ulang semua simpul
        for (int i = 0; i < jumlahSimpul; i++) {
            simpul.get(i).terkunjung = false;
        }
    }
    
    public static void carijalan(int start, int end) {     // melakukan jelajah jalur dari simpul tujuan menuju simpul awal (backtracking)
        jalur.clear();
        int pathTrace = path.get(end).indexroot;    // cari jalur melalui index root (indeks simpul yang dilalui sebelum simpul tsb)
        jalur.add(end);             // mulai dari simpul tujuan
        jalur.add(pathTrace);       // tambahkan simpul yang dilalui sebelum simpul tujuan
        while (pathTrace != start) {    
            int pathTraceTemp = path.get(pathTrace).indexroot;  // terus mencari simpul yang dilewati hingga menemui start
            jalur.add(pathTraceTemp);                           // tulis semua simpul yang dilewati
            pathTrace = pathTraceTemp;
        }
        Collections.reverse(jalur);     // ubah urutan dari awalnya tujuan ke awal, menjadi awal ke tujuan
        jarakfinal = path.get(end).jarak;
    }
    
    public static int indexBelumDikunjungi() {
        int jarakmula = 1000000000;     // anggap jarak setiap simpul dari simpul awal adalah INF
        int indeks = 0;
        
        for (int j = 1; j < jumlahSimpul; j++) {
            if(!simpul.get(j).terkunjung && path.get(j).jarak < jarakmula) {    // cari simpul terdekat yang belum dikunjungi
                jarakmula = path.get(j).jarak;
                indeks = j;
            }
        }
        return indeks;
    }
    
    public static void updatePath() {
        int branch = 0;
        while (branch < jumlahSimpul) {     // pengecekan dilakukan untuk setiap tetangga (branch) dari simpul yang sedang dikunjungi
            if (simpul.get(branch).terkunjung) {    // apabila tetangga sudah pernah dikunjungi, lewati penghitungan
                branch++;
                continue;
            }
            
            int jarakBranch = adjM.get(indeksDikunjungiSekarang).get(branch);   // ambil jarak dari simpul yang dikunjungi ke tetangganya (bila bukan tetangga, maka hasil INF)
            
            int jarakBranchViaDikunjungi = path.get(indeksDikunjungiSekarang).jarak + jarakBranch;    // jumlahkan jarak tetangga ke simpul yang dikunjungi, dengan jarak simpul awal ke simpul yang dikunjungi
            
            int jarakBranchLangsung = path.get(branch).jarak;       // ambil jarak langsung dari simpul awal ke tetangga bersangkutan
            
            if (jarakBranchViaDikunjungi < jarakBranchLangsung) {     // bila jarak ke tetangga tsb melewati simpul yg dikunjungi lebih dekat daripada jarak ke tetangga langsung dari simpul awal
                path.get(branch).indexroot = indeksDikunjungiSekarang;  // set jaraknya sebagai jarak terdekat,
                path.get(branch).jarak = jarakBranchViaDikunjungi;       // dan tandai bahwa tetangga tsb harus melewati simpul yang dikunjungi agar jaraknya lebih dekat.
            }
            branch++;
        }
    }
    
    public static void cariwaktu() {
        waktutempuh = (int) (jarakfinal / (Maps.slSpeed.getValue() * 1000 / 60));
        jam = waktutempuh / 60;
        menit = waktutempuh % 60;
        Maps.lblWaktuTempuh.setText("");
        if (jam != 0) {
            Maps.lblWaktuTempuh.setText(" " + Integer.toString(jam) + " jam");
        }
        if (menit != 0) {
            Maps.lblWaktuTempuh.setText(Maps.lblWaktuTempuh.getText() + " " + Integer.toString(menit) + " menit");
        }
    }
    
    // --------------------------------------------------------------------------------------//
    
    public static void pinpointClick(int input) {
        if (input != titikawal) {
            if (pinpoint) {
                titikakhir = input;
                carijalan(titikawal, titikakhir);
                Maps.lblPintuTolAkhir.setText(simpul.get(input).nama);
                Maps.lblJarak.setText(GUI.FormatDouble(jarakfinal / 1000) + " km");
                cariwaktu();
                GUI.ResetTransparencyPin();
                for (int i: jalur) {
                    GUI.SetTransparencyPin(Maps.Pin[i], true);
                }
                int temp = -1;
                GUI.ResetOverlayJalur(false);
                for (int i: jalur) {
                    if (temp != -1) {
                        Maps.Jalur[indeksEdge.get(temp).get(i)].setEnabled(true);
                        Maps.Jalur[indeksEdge.get(temp).get(i)].setVisible(true);
                    }
                    temp = i;
                }
                
                Maps.btnCancel.setVisible(true);
                Maps.shBtnCancel.setVisible(true);
            } else {
                pinpoint = true;
                titikawal = input;
                carijarak(titikawal);
                Maps.lblPintuTolAwal.setText(simpul.get(input).nama);
            }
        }
    }
    
    public static void pinpointHover(int input) {
        if (pinpoint && input != titikawal) {
            carijalan(titikawal, input);
            int temp = -1;
            GUI.ResetOverlayJalur(true);
            for (int i: jalur) {
                if (temp != -1) {
                    Maps.Jalur[indeksEdge.get(temp).get(i)].setVisible(true);
                }
                temp = i;
            }
            
        }
        GUI.SetTransparencyPin(Maps.Pin[input], true);
    }
    
    public static void pinpointUnhover(int input) {
        GUI.ResetTransparencyPin();
        if (titikawal != -1) {
            GUI.SetTransparencyPin(Maps.Pin[titikawal], true);
            GUI.ResetOverlayJalur(true);
        }
        if (titikakhir != -1) {
            jalur.clear();
            carijalan(titikawal, titikakhir);
            
            for (int i: jalur) {
                GUI.SetTransparencyPin(Maps.Pin[i], true);
            }
        }
    }
    
    public static void clearpinpoint() {
        path.clear();
        pinpoint = false;
        GUI.ResetOverlayJalur(false);
        GUI.ResetTransparencyPin();
        titikawal = -1; titikakhir = -1;
        Maps.lblPintuTolAwal.setText("???");
        Maps.lblPintuTolAkhir.setText("???");
        Maps.lblJarak.setText("");
        Maps.lblWaktuTempuh.setText("");
    }
    
    
}
