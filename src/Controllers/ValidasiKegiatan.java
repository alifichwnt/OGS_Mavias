/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controllers;

/**
 *
 * @author Rizky Alif - 2KS2
 */
public class ValidasiKegiatan {

    public static boolean cekNo(String No) {
        if (!No.matches("[0-9]+")) {
            return false;
        }
        return true;
    }
}
