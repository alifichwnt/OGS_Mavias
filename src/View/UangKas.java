/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
import Models.Session;
import Models.Mahasiswa;
import Database.Database;
import javax.swing.JOptionPane;
import Controllers.ValidasiKas;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author Rizky Alif - 2KS2
 */
public class UangKas extends javax.swing.JFrame {

    private boolean validasi;
    private String keterangan;
    private String idMahasiswa; // Variabel untuk menyimpan ID mahasiswa

    /**
     * Creates new form UangKas
     */
    public UangKas() {
        initComponents();
        setTitle("Data Kas Mahasiswa - Mavia's");
        updateKasTable();
        errorNo.setVisible(false);
        errorNama.setVisible(false);
        errorPemasukan.setVisible(false);
        errorPengeluaran.setVisible(false);
        uangKasTextField.setEditable(false);
        uangKasTextField.setText("10000");
        keteranganTextField.setEditable(false);
        saldoTerakhirTextField.setEditable(false);
        id_MahasiswaTextField.setEditable(false);
        id_MahasiswaTextField.setFocusable(false);
        getLastSaldoAkhir();
        sortTableDescending();
    }

    public Boolean ValidasiInput() {
        if (ValidasiKas.cekNo(noTextField.getText()) == false) {
            return false;
        }
        if (ValidasiKas.cekNama(namaTextField.getText()) == false) {
            return false;
        }
        if (ValidasiKas.cekPengeluaran(pengeluaranTextField.getText()) == false) {
            return false;
        }
        return ValidasiKas.cekPemasukan(pemasukanTextField.getText()) != false;
    }

    public void setNamaField(String nama) {
        namaTextField.setText(nama);
    }

    // Metode untuk mengatur idMahasiswa
    public void setIdMahasiswaField(String id) {
        this.idMahasiswa = id;
    }

    // Metode untuk mengatur id_MahasiswaTextField
    public void setIdMahasiswaTextField(String id) {
        id_MahasiswaTextField.setText(id);
    }

    //method untuk sorting isi tabel
    private void sortTableDescending() {
        DefaultTableModel model = (DefaultTableModel) kasTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        kasTable.setRowSorter(sorter);

        // Menerapkan Comparator untuk Nama (kolom 1) descending
        sorter.setComparator(1, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        // Menerapkan Comparator untuk Tingkat (kolom 4) descending
        sorter.setComparator(4, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });

        kasTable.setRowSorter(sorter);
    }

    private void clearForm() {
        // Kosongkan semua field input
        noTextField.setText("");
        tanggalDateChooser.setDate(null);
        namaTextField.setText("");
        keteranganTextField.setText("");
        pemasukanTextField.setText("");
        pengeluaranTextField.setText("");
        saldoTerakhirTextField.setText("");

        // Sembunyikan error messages jika ada
        errorNo.setVisible(false);
        errorNama.setVisible(false);
        errorPemasukan.setVisible(false);
        errorPengeluaran.setVisible(false);
    }

    // Metode untuk mengatur state (enabled/disabled) dan nilai dari field input
    private void setFieldState() {
        if (pemasukanRadioButton.isSelected()) {
            pemasukanTextField.setEditable(true);
            pengeluaranTextField.setEditable(false);
            pengeluaranTextField.setText("0");
        } else if (pengeluaranRadioButton.isSelected()) {
            pemasukanTextField.setEditable(false);
            pengeluaranTextField.setEditable(true);
            pemasukanTextField.setText("0");
        }
    }

    //hide collumn
    private void hideColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    private Integer calculateSaldoAkhir(Integer pemasukan, Integer pengeluaran, int selectedRow) {
        DefaultTableModel model = (DefaultTableModel) kasTable.getModel();
        int rowCount = model.getRowCount();

        // Jika tidak ada baris pada tabel, kembalikan saldo 0
        if (rowCount == 0) {
            saldoTerakhirTextField.setText("0");
            return 0;
        }

        // Hitung saldo akhir baru untuk baris yang dipilih
        int previousSaldo = selectedRow > 0 ? Integer.parseInt(model.getValueAt(selectedRow - 1, 6).toString()) : 0;
        int newSaldoAkhir = previousSaldo + pemasukan - pengeluaran;

        // Perbarui nilai saldo akhir pada baris yang dipilih
        model.setValueAt(newSaldoAkhir, selectedRow, 6);

        // Perbarui nilai saldo akhir pada semua baris setelah baris yang dipilih
        for (int i = selectedRow + 1; i < rowCount; i++) {
            int pemasukanNext = Integer.parseInt(model.getValueAt(i, 4).toString());
            int pengeluaranNext = Integer.parseInt(model.getValueAt(i, 5).toString());
            newSaldoAkhir = newSaldoAkhir + pemasukanNext - pengeluaranNext;
            model.setValueAt(newSaldoAkhir, i, 6);
        }

        // Perbarui nilai pada field saldoAkhirTextField
        saldoTerakhirTextField.setText(Integer.toString(newSaldoAkhir));

        return newSaldoAkhir;
    }

    private void updateSaldoAkhirInDatabase(Connection connection) throws SQLException {
        DefaultTableModel model = (DefaultTableModel) kasTable.getModel();
        int rowCount = model.getRowCount();

        String updateSaldoSQL = "UPDATE uangkas SET SaldoAkhir=? WHERE No=?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSaldoSQL)) {
            // Mulai saldo akhir dari 0
            int previousSaldo = 0;

            // Perbarui nilai saldo akhir pada semua baris di database
            for (int i = 0; i < rowCount; i++) {
                int pemasukanNext = Integer.parseInt(model.getValueAt(i, 4).toString());
                int pengeluaranNext = Integer.parseInt(model.getValueAt(i, 5).toString());
                int newSaldoAkhir = previousSaldo + pemasukanNext - pengeluaranNext;

                // Set nilai saldo akhir baru ke PreparedStatement
                preparedStatement.setInt(1, newSaldoAkhir);
                preparedStatement.setString(2, model.getValueAt(i, 0).toString()); // No adalah kolom ke-0
                preparedStatement.executeUpdate();

                // Set previousSaldo ke saldoAkhir yang baru dihitung untuk iterasi berikutnya
                previousSaldo = newSaldoAkhir;
            }
        }
    }

    private int getLastSaldoAkhir() {
        DefaultTableModel model = (DefaultTableModel) kasTable.getModel();
        int rowCount = model.getRowCount();
        if (rowCount > 0) {
            return Integer.parseInt(model.getValueAt(rowCount - 1, 6).toString()); // Kolom ke-6 adalah kolom "Saldo Akhir"
        } else {
            return 0; // Jika tabel kosong, kembalikan nilai 0
        }
    }

    public final void updateKasTable() {
        DefaultTableModel model = (DefaultTableModel) kasTable.getModel();
        model.setRowCount(0); // Kosongkan tabel sebelum menambahkan data baru

        try {
            Connection conn = Database.getConnection();
            try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM uangkas")) {

                while (rs.next()) {
                    String No = rs.getString("No");
                    String Tanggal = rs.getString("Tanggal");
                    String Nama = rs.getString("Nama");
                    String Keterangan = rs.getString("Keterangan");
                    String Pemasukan = rs.getString("Pemasukan");
                    String Pengeluaran = rs.getString("Pengeluaran");
                    String SaldoAkhir = rs.getString("SaldoAkhir");
                    String Id_Mahasiswa = rs.getString("Id_Mahasiswa");

                    // Tambahkan baris baru ke tabel
                    model.addRow(new Object[]{No, Tanggal, Nama, Keterangan, Pemasukan, Pengeluaran, SaldoAkhir, Id_Mahasiswa});
                }

                // Sembunyikan kolom Id_Mahasiswa setelah tabel terisi
                hideColumn(kasTable, 7); // Ubah angka 7 sesuai dengan indeks kolom Id_Mahasiswa (dimulai dari 0)

                //ambil saldo akhir dari database
                updateSaldoAkhirInDatabase(conn);

                // Set nilai Saldo Terakhir TextField setelah tabel diperbarui
                saldoTerakhirTextField.setText(Integer.toString(getLastSaldoAkhir()));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        } finally {
            Database.closeConnection();
        }
    }

    // Metode lain di kelas UangKas.java
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jFileChooser = new javax.swing.JFileChooser();
        jPanel1 = new javax.swing.JPanel();
        logo = new javax.swing.JLabel();
        Header = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        viewNamaButton = new javax.swing.JButton();
        namaTextField = new javax.swing.JTextField();
        keteranganTextField = new javax.swing.JTextField();
        noTextField = new javax.swing.JTextField();
        pengeluaranTextField = new javax.swing.JTextField();
        pemasukanTextField = new javax.swing.JTextField();
        saldoTerakhirTextField = new javax.swing.JTextField();
        uangKasTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        kasTable = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        exportCSVButton = new javax.swing.JButton();
        menuButton = new javax.swing.JButton();
        tanggalDateChooser = new com.toedter.calendar.JDateChooser();
        pengeluaranRadioButton = new javax.swing.JRadioButton();
        pemasukanRadioButton = new javax.swing.JRadioButton();
        errorPengeluaran = new javax.swing.JLabel();
        errorNama = new javax.swing.JLabel();
        errorNo = new javax.swing.JLabel();
        errorPemasukan = new javax.swing.JLabel();
        id_MahasiswaTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(153, 153, 153));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 698));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/header1.png"))); // NOI18N
        jPanel1.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 80, 70));

        Header.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        Header.setForeground(new java.awt.Color(255, 255, 255));
        Header.setText("MANAJEMEN KAS HIMADA MAVIA'S");
        jPanel1.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, -1));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setText("Rp");
        jPanel1.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 250, -1, -1));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("No.");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 100, -1, -1));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Tanggal");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 140, -1, -1));

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setText("ID ");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 220, -1, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setText("Keterangan");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, -1, -1));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel6.setText("Rp");
        jPanel1.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 100, -1, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel7.setText("Rp");
        jPanel1.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 140, -1, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setText("Saldo Terakhir");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 190, -1, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 102, 0));
        jLabel9.setText("Uang Kas/minggu");
        jPanel1.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 250, -1, -1));

        viewNamaButton.setBackground(new java.awt.Color(204, 204, 255));
        viewNamaButton.setForeground(new java.awt.Color(0, 51, 153));
        viewNamaButton.setText("View");
        viewNamaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewNamaButtonActionPerformed(evt);
            }
        });
        jPanel1.add(viewNamaButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 180, 60, 30));

        namaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaTextFieldActionPerformed(evt);
            }
        });
        namaTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                namaTextFieldKeyReleased(evt);
            }
        });
        jPanel1.add(namaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 160, 30));

        keteranganTextField.setBackground(new java.awt.Color(204, 204, 204));
        keteranganTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keteranganTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(keteranganTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 260, 160, 30));

        noTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noTextFieldActionPerformed(evt);
            }
        });
        noTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                noTextFieldKeyReleased(evt);
            }
        });
        jPanel1.add(noTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(300, 100, 60, 30));

        pengeluaranTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pengeluaranTextFieldActionPerformed(evt);
            }
        });
        pengeluaranTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pengeluaranTextFieldKeyReleased(evt);
            }
        });
        jPanel1.add(pengeluaranTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 140, 150, 30));

        pemasukanTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pemasukanTextFieldActionPerformed(evt);
            }
        });
        pemasukanTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                pemasukanTextFieldKeyReleased(evt);
            }
        });
        jPanel1.add(pemasukanTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 102, 150, 30));

        saldoTerakhirTextField.setBackground(new java.awt.Color(204, 204, 204));
        saldoTerakhirTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saldoTerakhirTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(saldoTerakhirTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 190, 150, 30));

        uangKasTextField.setBackground(new java.awt.Color(204, 204, 204));
        uangKasTextField.setText("10000");
        uangKasTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uangKasTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(uangKasTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 250, 50, -1));

        searchButton.setBackground(new java.awt.Color(51, 51, 255));
        searchButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setText("SEARCH");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel1.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 310, -1, 40));

        saveButton.setBackground(new java.awt.Color(51, 153, 0));
        saveButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("INPUT");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel1.add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 310, -1, 40));

        editButton.setBackground(new java.awt.Color(255, 204, 51));
        editButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        editButton.setForeground(new java.awt.Color(255, 255, 255));
        editButton.setText("EDIT");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jPanel1.add(editButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, -1, 40));

        clearButton.setBackground(new java.awt.Color(51, 51, 51));
        clearButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        clearButton.setForeground(new java.awt.Color(255, 255, 255));
        clearButton.setText("CLEAR");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        jPanel1.add(clearButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 310, -1, 40));

        deleteButton.setBackground(new java.awt.Color(153, 0, 0));
        deleteButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("DELETE");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel1.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 310, -1, 40));

        kasTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "No", "Tanggal", "Nama", "Keterangan", "Pemasukan", "Pengeluaran", "Saldo Akhir", "id_mahasiswa"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.Integer.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        kasTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kasTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(kasTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 360, 750, 290));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel10.setText("Export to:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 660, -1, 30));

        exportCSVButton.setBackground(new java.awt.Color(0, 153, 0));
        exportCSVButton.setFont(new java.awt.Font("Cascadia Mono", 1, 12)); // NOI18N
        exportCSVButton.setForeground(new java.awt.Color(255, 255, 255));
        exportCSVButton.setText(".csv");
        exportCSVButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportCSVButtonActionPerformed(evt);
            }
        });
        jPanel1.add(exportCSVButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 660, 70, 30));

        menuButton.setBackground(new java.awt.Color(204, 204, 255));
        menuButton.setFont(new java.awt.Font("Cascadia Mono", 1, 12)); // NOI18N
        menuButton.setForeground(new java.awt.Color(153, 0, 0));
        menuButton.setText("MENU");
        menuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuButtonActionPerformed(evt);
            }
        });
        jPanel1.add(menuButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, -1, -1));
        jPanel1.add(tanggalDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(198, 140, 160, 30));

        pengeluaranRadioButton.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        pengeluaranRadioButton.setText("Pengeluaran");
        pengeluaranRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pengeluaranRadioButtonActionPerformed(evt);
            }
        });
        jPanel1.add(pengeluaranRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 140, 110, 30));

        pemasukanRadioButton.setFont(new java.awt.Font("Bell MT", 1, 14)); // NOI18N
        pemasukanRadioButton.setText("Pemasukan");
        pemasukanRadioButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pemasukanRadioButtonActionPerformed(evt);
            }
        });
        jPanel1.add(pemasukanRadioButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 100, -1, 30));

        errorPengeluaran.setFont(new java.awt.Font("Franklin Gothic Demi", 2, 10)); // NOI18N
        errorPengeluaran.setForeground(new java.awt.Color(204, 0, 0));
        errorPengeluaran.setText("Harus berupa angka!");
        jPanel1.add(errorPengeluaran, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 150, -1, -1));

        errorNama.setFont(new java.awt.Font("Franklin Gothic Demi", 2, 10)); // NOI18N
        errorNama.setForeground(new java.awt.Color(204, 0, 0));
        errorNama.setText("Nama harus berupa huruf!");
        jPanel1.add(errorNama, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 210, -1, -1));

        errorNo.setFont(new java.awt.Font("Franklin Gothic Demi", 2, 10)); // NOI18N
        errorNo.setForeground(new java.awt.Color(204, 0, 0));
        errorNo.setText("Harus angka!");
        jPanel1.add(errorNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 110, -1, -1));

        errorPemasukan.setFont(new java.awt.Font("Franklin Gothic Demi", 2, 10)); // NOI18N
        errorPemasukan.setForeground(new java.awt.Color(204, 0, 0));
        errorPemasukan.setText("Harus berupa angka!");
        jPanel1.add(errorPemasukan, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 110, -1, -1));

        id_MahasiswaTextField.setBackground(new java.awt.Color(204, 204, 204));
        id_MahasiswaTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                id_MahasiswaTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(id_MahasiswaTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(371, 220, 50, 30));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel11.setText("Nama");
        jPanel1.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 180, -1, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 708, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void noTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_noTextFieldActionPerformed

    private void menuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();

        // Buka MainMenu
        MainMenu mainMenu = new MainMenu();
        mainMenu.setVisible(true);
    }//GEN-LAST:event_menuButtonActionPerformed

    private void namaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaTextFieldActionPerformed

    private void viewNamaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewNamaButtonActionPerformed
        // TODO add your handling code here:
        this.dispose();

        // Buka Tabel Mahasiwa
        TabelMahasiswa mahasiswa = new TabelMahasiswa();
        mahasiswa.setVisible(true);
    }//GEN-LAST:event_viewNamaButtonActionPerformed

    private void keteranganTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keteranganTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_keteranganTextFieldActionPerformed

    private void pemasukanTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pemasukanTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pemasukanTextFieldActionPerformed

    private void pengeluaranTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengeluaranTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pengeluaranTextFieldActionPerformed

    private void saldoTerakhirTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saldoTerakhirTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saldoTerakhirTextFieldActionPerformed

    private void uangKasTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uangKasTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_uangKasTextFieldActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        // Validasi input sebelum menyimpan
        if (!ValidasiInput()) {
            JOptionPane.showMessageDialog(this, "Harap isi semua kolom dengan benar.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        // Ambil nilai dari setiap field
        String No = noTextField.getText();
        Date selectedDate = tanggalDateChooser.getDate();
        String Tanggal = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate); // Format tanggal sesuai dengan format SQL
        String Nama = namaTextField.getText();
        String Keterangan = keteranganTextField.getText();
        int Pemasukan = Integer.parseInt(pemasukanTextField.getText());
        int Pengeluaran = Integer.parseInt(pengeluaranTextField.getText());
        int SaldoTerakhir = Integer.parseInt(saldoTerakhirTextField.getText());
        String ID_Mahasiswa = id_MahasiswaTextField.getText();

        // Hitung nilai Saldo Akhir baru
        int SaldoAkhir;
        if (pemasukanRadioButton.isSelected()) {
            SaldoAkhir = SaldoTerakhir + Pemasukan;
        } else if (pengeluaranRadioButton.isSelected()) {
            SaldoAkhir = SaldoTerakhir - Pengeluaran;
        } else {
            JOptionPane.showMessageDialog(this, "Pilih jenis transaksi (Pemasukan atau Pengeluaran).", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Konversi SaldoAkhir ke String untuk penyimpanan
        String SaldoAkhirStr = Integer.toString(SaldoAkhir);

        // Query SQL untuk menyimpan data
        String insertSQL = "INSERT INTO uangkas (No, Tanggal, Nama, Keterangan, Pemasukan, Pengeluaran, SaldoAkhir, id_mahasiswa) VALUES (?,?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, No);
            preparedStatement.setString(2, Tanggal);
            preparedStatement.setString(3, Nama);
            preparedStatement.setString(4, Keterangan);
            preparedStatement.setString(5, Integer.toString(Pemasukan));
            preparedStatement.setString(6, Integer.toString(Pengeluaran));
            preparedStatement.setString(7, SaldoAkhirStr);
            preparedStatement.setString(8, ID_Mahasiswa);

            int rowsInserted = preparedStatement.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Data berhasil disimpan", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                // Perbarui tabel GUI (kasTable) setelah menyimpan data
                updateKasTable();

                // Kosongkan form input setelah berhasil disimpan
                clearForm();
            }

        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menyimpan data", "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Database.closeConnection();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        // Ambil nilai dari field-field yang sudah diperbarui
        String No = noTextField.getText();
        String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(tanggalDateChooser.getDate());
        String Nama = namaTextField.getText();
        String keterangan = keteranganTextField.getText();
        String pemasukanStr = pemasukanTextField.getText();
        String pengeluaranStr = pengeluaranTextField.getText();
        String id_mahasiswa = id_MahasiswaTextField.getText();

        // Menampilkan dialog konfirmasi
        int option = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin mengupdate data ini?", "Konfirmasi Update", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return; // Jika pengguna memilih tidak, keluar dari method
        }

        try {
            // Konversi nilai String ke Integer untuk pemasukan dan pengeluaran
            Integer pemasukan = Integer.valueOf(pemasukanStr);
            Integer pengeluaran = Integer.valueOf(pengeluaranStr);
            // Dapatkan baris yang dipilih
            int selectedRow = kasTable.getSelectedRow();
            if (selectedRow == -1) {
                JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            // Hitung saldo akhir berdasarkan pemasukan dan pengeluaran
            Integer saldoAkhir = calculateSaldoAkhir(pemasukan, pengeluaran, selectedRow);

            // Query SQL untuk update data berdasarkan No
            String updateSQL = "UPDATE uangkas SET Tanggal=?, Nama=?, Keterangan=?, Pemasukan=?, Pengeluaran=?, SaldoAkhir=?, Id_Mahasiswa=? WHERE No=?";

            try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
                preparedStatement.setString(1, tanggal);
                preparedStatement.setString(2, Nama);
                preparedStatement.setString(3, keterangan);
                preparedStatement.setInt(4, pemasukan); // Menggunakan setInt karena pemasukan adalah integer
                preparedStatement.setInt(5, pengeluaran); // Menggunakan setInt karena pengeluaran adalah integer
                preparedStatement.setInt(6, saldoAkhir); // Menggunakan setInt karena saldoAkhir adalah integer
                preparedStatement.setString(7, id_mahasiswa);
                preparedStatement.setString(8, No);

                int rowsUpdated = preparedStatement.executeUpdate();
                if (rowsUpdated > 0) {
                    // Perbarui tabel GUI (kasTable) setelah mengupdate data
                    updateKasTable();

                    JOptionPane.showMessageDialog(this, "Data berhasil diperbarui", "Sukses", JOptionPane.INFORMATION_MESSAGE);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat mengupdate data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                Database.closeConnection();
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Format pemasukan atau pengeluaran tidak valid", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        // Mengosongkan setiap field
        noTextField.setText("");
        tanggalDateChooser.setDate(null); // Menghapus nilai tanggal dari JDateChooser
        namaTextField.setText("");
        keteranganTextField.setText("");
        pemasukanTextField.setText("");
        pengeluaranTextField.setText("");
        id_MahasiswaTextField.setText("");
    }//GEN-LAST:event_clearButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        int selectedRow = kasTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Pilih baris yang akan dihapus", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Ambil nilai No dari baris yang dipilih
        String No = kasTable.getValueAt(selectedRow, 0).toString();

        // Tampilkan dialog konfirmasi
        int option = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Penghapusan", JOptionPane.YES_NO_OPTION);

        // Jika pengguna mengklik tombol Yes (YES_OPTION), lanjutkan dengan penghapusan
        if (option == JOptionPane.YES_OPTION) {
            // Query SQL untuk menghapus data berdasarkan No
            String deleteSQL = "DELETE FROM uangkas WHERE No = ?";

            try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(deleteSQL)) {

                preparedStatement.setString(1, No);

                int rowsDeleted = preparedStatement.executeUpdate();
                if (rowsDeleted > 0) {
                    JOptionPane.showMessageDialog(this, "Data berhasil dihapus", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                    // Perbarui tabel GUI (kasTable) setelah menghapus data
                    updateKasTable(); // Pastikan updateKasTable() adalah metode yang memperbarui data di tabel
                }

            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data", "Error", JOptionPane.ERROR_MESSAGE);
            } finally {
                Database.closeConnection();
            }
        }
    }//GEN-LAST:event_deleteButtonActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        // TODO add your handling code here:
        // Ambil nomor dari noTextField
        String nomor = noTextField.getText().trim();

        // Query SQL untuk mencari data berdasarkan nomor
        String searchSQL = "SELECT * FROM uangkas WHERE No = ?";

        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(searchSQL)) {
            preparedStatement.setString(1, nomor);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Jika data ditemukan, ambil nilai dari hasil query
                    String tanggal = rs.getString("Tanggal");
                    String nama = rs.getString("Nama");
                    String keterangan = rs.getString("Keterangan");
                    String pemasukan = rs.getString("Pemasukan");
                    String pengeluaran = rs.getString("Pengeluaran");
                    String saldoAkhir = rs.getString("SaldoAkhir");
                    String idMahasiswa = rs.getString("Id_Mahasiswa");

                    // Set nilai ke dalam field masing-masing
                    noTextField.setText(nomor);
                    namaTextField.setText(nama);
                    keteranganTextField.setText(keterangan);
                    pemasukanTextField.setText(pemasukan);
                    pengeluaranTextField.setText(pengeluaran);
                    saldoTerakhirTextField.setText(saldoAkhir);
                    id_MahasiswaTextField.setText(idMahasiswa);

                    // Parse tanggal ke dalam format yang sesuai dengan JDateChooser
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
                        tanggalDateChooser.setDate(date);
                    } catch (ParseException ex) {
                        ex.printStackTrace();
                        JOptionPane.showMessageDialog(this, "Error parsing date: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    // Jika tidak ada data yang ditemukan
                    JOptionPane.showMessageDialog(this, "Data dengan nomor " + nomor + " tidak ditemukan", "Not Found", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat melakukan pencarian data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Database.closeConnection();
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    private void kasTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kasTableMouseClicked
        // TODO add your handling code here:
        int selectedRow = kasTable.getSelectedRow();
        if (selectedRow == -1) {
            return; // Jika tidak ada baris yang dipilih, keluar dari metode
        }

        // Ambil nilai dari setiap kolom pada baris yang dipilih
        String No = kasTable.getValueAt(selectedRow, 0).toString();
        String tanggal = kasTable.getValueAt(selectedRow, 1).toString();
        String Nama = kasTable.getValueAt(selectedRow, 2).toString();
        String keterangan = kasTable.getValueAt(selectedRow, 3).toString();
        String pemasukan = kasTable.getValueAt(selectedRow, 4).toString();
        String pengeluaran = kasTable.getValueAt(selectedRow, 5).toString();
        String saldoAkhir = kasTable.getValueAt(selectedRow, 6).toString();
        String id_mahasiswa = kasTable.getValueAt(selectedRow, 7).toString();

        // Set nilai ke dalam field masing-masing
        noTextField.setText(No);
        namaTextField.setText(Nama);
        keteranganTextField.setText(keterangan);
        pemasukanTextField.setText(pemasukan);
        pengeluaranTextField.setText(pengeluaran);
        saldoTerakhirTextField.setText(saldoAkhir);
        id_MahasiswaTextField.setText(id_mahasiswa);
        // Parse tanggal ke dalam format yang sesuai dengan JDateChooser
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal); // Sesuaikan format dengan yang digunakan di database
            tanggalDateChooser.setDate(date); // Set nilai tanggal ke JDateChooser
        } catch (ParseException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error parsing date: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_kasTableMouseClicked

    private void pengeluaranRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pengeluaranRadioButtonActionPerformed
        // TODO add your handling code here:
        keterangan = "Uang Keluar";
        pemasukanRadioButton.setSelected(false);
        keteranganTextField.setText("" + keterangan);
        setFieldState();
    }//GEN-LAST:event_pengeluaranRadioButtonActionPerformed

    private void pemasukanRadioButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pemasukanRadioButtonActionPerformed
        // TODO add your handling code here:
        keterangan = "Uang Masuk";
        pengeluaranRadioButton.setSelected(false);
        keteranganTextField.setText("" + keterangan);
        setFieldState();
    }//GEN-LAST:event_pemasukanRadioButtonActionPerformed

    private void noTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noTextFieldKeyReleased
        // TODO add your handling code here:
        if (ValidasiKas.cekNo(noTextField.getText()) == false) {
            errorNo.setVisible(true);
            validasi = false;
        } else {
            errorNo.setVisible(false);
            validasi = true;
        }
    }//GEN-LAST:event_noTextFieldKeyReleased

    private void namaTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_namaTextFieldKeyReleased
        // TODO add your handling code here:
        if (ValidasiKas.cekNama(namaTextField.getText()) == false) {
            errorNama.setVisible(true);
            validasi = false;
        } else {
            errorNama.setVisible(false);
            validasi = true;
        }
    }//GEN-LAST:event_namaTextFieldKeyReleased

    private void pemasukanTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pemasukanTextFieldKeyReleased
        // TODO add your handling code here:
        if (ValidasiKas.cekPemasukan(pemasukanTextField.getText()) == false) {
            errorPemasukan.setVisible(true);
            validasi = false;
        } else {
            errorPemasukan.setVisible(false);
            validasi = true;
        }
    }//GEN-LAST:event_pemasukanTextFieldKeyReleased

    private void pengeluaranTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_pengeluaranTextFieldKeyReleased
        // TODO add your handling code here:
        if (ValidasiKas.cekPengeluaran(pengeluaranTextField.getText()) == false) {
            errorPengeluaran.setVisible(true);
            validasi = false;
        } else {
            errorPengeluaran.setVisible(false);
            validasi = true;
        }
    }//GEN-LAST:event_pengeluaranTextFieldKeyReleased

    private void exportCSVButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportCSVButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Save as CSV File");
        fileChooser.setFileFilter(new FileNameExtensionFilter("CSV Files (*.csv)", "csv")); // Filter hanya file CSV
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            String filePath = fileToSave.getAbsolutePath();

            // Menambahkan ekstensi .csv jika belum ada
            if (!filePath.toLowerCase().endsWith(".csv")) {
                fileToSave = new File(filePath + ".csv");
            }

            try (FileWriter fw = new FileWriter(fileToSave); BufferedWriter bw = new BufferedWriter(fw)) {

                // Menulis judul kolom
                for (int i = 0; i < kasTable.getColumnCount(); i++) {
                    bw.write(kasTable.getColumnName(i) + ",");
                }
                bw.newLine();

                // Menulis data ke file
                for (int i = 0; i < kasTable.getRowCount(); i++) {
                    for (int j = 0; j < kasTable.getColumnCount(); j++) {
                        Object value = kasTable.getValueAt(i, j);
                        if (value != null) {
                            bw.write(value.toString() + ",");
                        } else {
                            bw.write("NULL,");
                        }
                    }
                    bw.newLine();
                }

                JOptionPane.showMessageDialog(this, "File successfully saved as CSV!", "Information", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Error saving CSV file: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_exportCSVButtonActionPerformed

    private void id_MahasiswaTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_id_MahasiswaTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_id_MahasiswaTextFieldActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(UangKas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UangKas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UangKas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UangKas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        if (!Session.isAuthenticated()) {
            JOptionPane.showMessageDialog(null, "Harap Login Terlebih Dahulu.");
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new Login().setVisible(true);
                }
            });
        } else {
            // Jika sudah terautentikasi, lanjutkan ke main menu atau halaman lain
            java.awt.EventQueue.invokeLater(new Runnable() {
                @Override
                public void run() {
                    new MainMenu().setVisible(true);
                }
            });
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Header;
    private javax.swing.JButton clearButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JButton editButton;
    private javax.swing.JLabel errorNama;
    private javax.swing.JLabel errorNo;
    private javax.swing.JLabel errorPemasukan;
    private javax.swing.JLabel errorPengeluaran;
    private javax.swing.JButton exportCSVButton;
    private javax.swing.JTextField id_MahasiswaTextField;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable kasTable;
    private javax.swing.JTextField keteranganTextField;
    private javax.swing.JLabel logo;
    private javax.swing.JButton menuButton;
    public javax.swing.JTextField namaTextField;
    private javax.swing.JTextField noTextField;
    private javax.swing.JRadioButton pemasukanRadioButton;
    private javax.swing.JTextField pemasukanTextField;
    private javax.swing.JRadioButton pengeluaranRadioButton;
    private javax.swing.JTextField pengeluaranTextField;
    private javax.swing.JTextField saldoTerakhirTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private com.toedter.calendar.JDateChooser tanggalDateChooser;
    private javax.swing.JTextField uangKasTextField;
    private javax.swing.JButton viewNamaButton;
    // End of variables declaration//GEN-END:variables
}
