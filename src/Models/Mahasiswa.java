/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

/**
 *
 * @author Rizky Alif - 2KS2
 */
public class Mahasiswa {
    private String id;
    private String nim;
    private String nama;
    private String jenisKelamin;
    private String noHp;
    private String tingkat;

    public Mahasiswa(String id, String nim, String nama, String jenisKelamin, String noHp, String tingkat) {
        this.id = id;
        this.nim = nim;
        this.nama = nama;
        this.jenisKelamin = jenisKelamin;
        this.noHp = noHp;
        this.tingkat = tingkat;
    }

    // Getter methods
    public String getId() {
        return id;
    }

    public String getNim() {
        return nim;
    }

    public String getNama() {
        return nama;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getNoHp() {
        return noHp;
    }

    public String getTingkat() {
        return tingkat;
    }
}

