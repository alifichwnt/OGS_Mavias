/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

/**
 *
 * @author Rizky Alif - 2KS2
 */
public class ValidasiMahasiswa {

    public static boolean cekNim(String Nim) {
        if (!Nim.matches("[0-9]+")) {
            return false;
        }
        if (Nim.length() > 12) {
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
    
    public static boolean cekNoHP(String NoHp){
        if (!NoHp.matches("[0-9]+")) {
            return false;
        }
        if (NoHp.length() < 10) {
            return false;
        }
        if (NoHp.length() > 15) {
            return false;
        }
        return true;
    }
}
