/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Graph;

/**
 *
 * @author Edho
 */
class Path {
    int jarak;      // menyimpan jarak total dari simpul awal
    int indexroot;  // menyimpan simpul yang dikunjungi sebelum simpul ini agar mendapat jarak terdekat
    
    public Path(int idx, int j) {
        indexroot = idx;
        jarak = j;
    }
}
