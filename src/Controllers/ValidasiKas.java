/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

/**
 *
 * @author Rizky Alif - 2KS2
 */
public class ValidasiKas {
    
    public static boolean cekNo(String No) {
        if (!No.matches("[0-9]+")) {
            return false;
        }
        return true;
    }
    
    public static boolean cekNama(String Nama){
        if (!Nama.matches("[a-zA-Z\\s]+")) {
            return false;
        }
        return true;
    }
    
    public static boolean cekPemasukan(String pemasukan){
        if (!pemasukan.matches("[0-9]+")) {
            return false;
        }
        return true;
    }
    
    public static boolean cekPengeluaran(String pengeluaran){
        if (!pengeluaran.matches("[0-9]+")) {
            return false;
        }
        return true;
    }
}
