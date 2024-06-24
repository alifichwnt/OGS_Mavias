/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package View;

import Models.Session;
import javax.swing.JOptionPane;
import Controllers.ValidasiKegiatan;
import Database.Database;
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import java.util.Date;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import javax.swing.table.TableRowSorter;
import java.util.Comparator;
import javax.swing.filechooser.FileNameExtensionFilter;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPCell;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.Statement;
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
public class Kegiatan extends javax.swing.JFrame {

    private boolean validasi;

    /**
     * Creates new form Kegiatan
     */
    public Kegiatan() {
        initComponents();
        setTitle("Kegiatan Himada - Mavia's");
        errorNo.setVisible(false);
        updateKegiatanTable();
        sortTableDescending();
    }

    public Boolean ValidasiInput() {
        return ValidasiKegiatan.cekNo(noTextField.getText()) == false;
    }

    //method untuk sorting isi tabel
    private void sortTableDescending() {
        DefaultTableModel model = (DefaultTableModel) kegiatanTable.getModel();
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(model);
        kegiatanTable.setRowSorter(sorter);

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
    }

    //hide collumn
    private void hideColumn(JTable table, int columnIndex) {
        table.getColumnModel().getColumn(columnIndex).setMinWidth(0);
        table.getColumnModel().getColumn(columnIndex).setMaxWidth(0);
        table.getColumnModel().getColumn(columnIndex).setWidth(0);
        table.getColumnModel().getColumn(columnIndex).setPreferredWidth(0);
    }

    private void clearForm() {
        noTextField.setText(""); // Mengosongkan field No.
        tanggalDateChooser.setDate(null); // Mengosongkan pilihan tanggal
        namaKegiatanTextField.setText(""); // Mengosongkan field Nama Kegiatan
        lokasiTextField.setText(""); // Mengosongkan field Lokasi
        statusComboBox.setSelectedIndex(-1); // Mengosongkan pilihan status

        // Reset validasi dan error message
        errorNo.setVisible(false);
        validasi = false;
    }

    // Metode untuk memperbarui tampilan tabel kegiatan
    private void updateKegiatanTable() {
        DefaultTableModel model = (DefaultTableModel) kegiatanTable.getModel();
        model.setRowCount(0); // Kosongkan tabel sebelum menambahkan data baru

        try {
            Connection conn = Database.getConnection();
            String query = "SELECT * FROM kegiatan";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int id = rs.getInt("id");
                int no = rs.getInt("No");
                String tanggal = rs.getString("date");
                String namaKegiatan = rs.getString("namaKegiatan");
                String lokasi = rs.getString("lokasi");
                String status = rs.getString("status");

                // Tambahkan baris baru ke tabel
                model.addRow(new Object[]{id, no, tanggal, namaKegiatan, lokasi, status});
            }

            // Sembunyikan kolom id setelah tabel terisi
            hideColumn(kegiatanTable, 0); // Ubah angka 0 sesuai dengan indeks kolom id (dimulai dari 0)

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Database.closeConnection();
        }
    }

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
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        namaKegiatanTextField = new javax.swing.JTextField();
        noTextField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        clearButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        kegiatanTable = new javax.swing.JTable();
        jLabel10 = new javax.swing.JLabel();
        exportPDFButton = new javax.swing.JButton();
        menuButton = new javax.swing.JButton();
        lokasiTextField = new javax.swing.JTextField();
        statusComboBox = new javax.swing.JComboBox<>();
        tanggalDateChooser = new com.toedter.calendar.JDateChooser();
        errorNo = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(900, 698));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/image/header1.png"))); // NOI18N
        jPanel1.add(logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 0, 80, 70));

        Header.setFont(new java.awt.Font("Arial Black", 1, 24)); // NOI18N
        Header.setForeground(new java.awt.Color(255, 255, 255));
        Header.setText("LIST KEGIATAN HIMADA MAVIA'S");
        jPanel1.add(Header, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 20, -1, -1));

        jLabel2.setBackground(new java.awt.Color(255, 255, 255));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("No.");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(240, 80, -1, 30));

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Tanggal");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 80, -1, 30));

        jLabel4.setBackground(new java.awt.Color(255, 255, 255));
        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Nama Kegiatan");
        jPanel1.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 140, -1, 30));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Lokasi");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 190, -1, 30));

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Status");
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 240, -1, 30));

        namaKegiatanTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                namaKegiatanTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(namaKegiatanTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 140, 240, 30));

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
        jPanel1.add(noTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 80, 40, 30));

        searchButton.setBackground(new java.awt.Color(51, 51, 255));
        searchButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        searchButton.setForeground(new java.awt.Color(255, 255, 255));
        searchButton.setText("SEARCH");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });
        jPanel1.add(searchButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(630, 300, -1, 40));

        saveButton.setBackground(new java.awt.Color(51, 153, 0));
        saveButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        saveButton.setForeground(new java.awt.Color(255, 255, 255));
        saveButton.setText("INPUT");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        jPanel1.add(saveButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 300, -1, 40));

        editButton.setBackground(new java.awt.Color(255, 204, 51));
        editButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        editButton.setForeground(new java.awt.Color(255, 255, 255));
        editButton.setText("EDIT");
        editButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editButtonActionPerformed(evt);
            }
        });
        jPanel1.add(editButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 300, -1, 40));

        clearButton.setBackground(new java.awt.Color(51, 51, 51));
        clearButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        clearButton.setForeground(new java.awt.Color(255, 255, 255));
        clearButton.setText("CLEAR");
        clearButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearButtonActionPerformed(evt);
            }
        });
        jPanel1.add(clearButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 300, -1, 40));

        deleteButton.setBackground(new java.awt.Color(153, 0, 0));
        deleteButton.setFont(new java.awt.Font("Dubai", 1, 12)); // NOI18N
        deleteButton.setForeground(new java.awt.Color(255, 255, 255));
        deleteButton.setText("DELETE");
        deleteButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                deleteButtonActionPerformed(evt);
            }
        });
        jPanel1.add(deleteButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 300, -1, 40));

        kegiatanTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "id", "No", "Tanggal", "Nama Kegiatan", "Lokasi", "Status"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        kegiatanTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kegiatanTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(kegiatanTable);

        jPanel1.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(82, 360, 750, 290));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 3, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(204, 204, 204));
        jLabel10.setText("Export to:");
        jPanel1.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 660, -1, 30));

        exportPDFButton.setBackground(new java.awt.Color(255, 25, 22));
        exportPDFButton.setFont(new java.awt.Font("Cascadia Mono", 1, 12)); // NOI18N
        exportPDFButton.setForeground(new java.awt.Color(255, 255, 255));
        exportPDFButton.setText(".pdf");
        exportPDFButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exportPDFButtonActionPerformed(evt);
            }
        });
        jPanel1.add(exportPDFButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 660, 70, 30));

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

        lokasiTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lokasiTextFieldActionPerformed(evt);
            }
        });
        jPanel1.add(lokasiTextField, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 190, 240, 30));

        statusComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Belum Terlaksana", "Terlaksana" }));
        statusComboBox.setSelectedIndex(-1);
        statusComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                statusComboBoxActionPerformed(evt);
            }
        });
        jPanel1.add(statusComboBox, new org.netbeans.lib.awtextra.AbsoluteConstraints(432, 240, 150, 30));
        jPanel1.add(tanggalDateChooser, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 80, 140, 30));

        errorNo.setFont(new java.awt.Font("Cooper Black", 2, 10)); // NOI18N
        errorNo.setForeground(new java.awt.Color(255, 51, 51));
        errorNo.setText("Harus Angka!");
        jPanel1.add(errorNo, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 90, 80, -1));

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
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 715, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void namaKegiatanTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_namaKegiatanTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_namaKegiatanTextFieldActionPerformed

    private void lokasiTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lokasiTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_lokasiTextFieldActionPerformed

    private void statusComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_statusComboBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_statusComboBoxActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        // TODO add your handling code here:
        // Mendapatkan nilai dari input pengguna
        int no = Integer.parseInt(noTextField.getText());
        Date selectedDate = tanggalDateChooser.getDate();
        String Tanggal = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate); // Format tanggal sesuai dengan format SQL
        String namaKegiatan = namaKegiatanTextField.getText();
        String lokasi = lokasiTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();

        // Menyimpan data ke database
        try {
            Connection conn = Database.getConnection();
            String query = "INSERT INTO kegiatan (No, date, namaKegiatan, lokasi, status) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, no);
            pstmt.setString(2, Tanggal);
            pstmt.setString(3, namaKegiatan);
            pstmt.setString(4, lokasi);
            pstmt.setString(5, status);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(null, "Data kegiatan berhasil disimpan", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                // Perbarui tabel setelah menyimpan data
                updateKegiatanTable(); // Panggil method ini untuk memperbarui tabel
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Database.closeConnection();
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void editButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editButtonActionPerformed
        // TODO add your handling code here:
        // Ambil data dari field/combobox
        int no = Integer.parseInt(noTextField.getText());
        Date selectedDate = tanggalDateChooser.getDate();
        String tanggal = new SimpleDateFormat("yyyy-MM-dd").format(selectedDate); // Format tanggal sesuai dengan format SQL
        String namaKegiatan = namaKegiatanTextField.getText();
        String lokasi = lokasiTextField.getText();
        String status = statusComboBox.getSelectedItem().toString();

        // Mendapatkan baris yang dipilih dari tabel
        int row = kegiatanTable.getSelectedRow();
        // Memastikan baris yang dipilih valid
        if (row == -1) {
            JOptionPane.showMessageDialog(null, "Pilih baris yang ingin diubah", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Mendapatkan ID dari baris yang dipilih
        DefaultTableModel model = (DefaultTableModel) kegiatanTable.getModel();
        int id = (int) model.getValueAt(row, 0); // ID disimpan di kolom pertama

        // Menampilkan dialog konfirmasi
        int option = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin mengupdate data ini?", "Konfirmasi Update", JOptionPane.YES_NO_OPTION);
        if (option != JOptionPane.YES_OPTION) {
            return; // Jika pengguna memilih tidak, keluar dari method
        }

        // Perbarui data di database
        try {
            Connection conn = Database.getConnection();
            String query = "UPDATE kegiatan SET No=?, date=?, namaKegiatan=?, lokasi=?, status=? WHERE id=?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, no);
            pstmt.setString(2, tanggal);
            pstmt.setString(3, namaKegiatan);
            pstmt.setString(4, lokasi);
            pstmt.setString(5, status);
            pstmt.setInt(6, id);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(null, "Data kegiatan berhasil diperbarui", "Sukses", JOptionPane.INFORMATION_MESSAGE);

                // Perbarui tabel setelah mengedit data
                updateKegiatanTable();

                // Kosongkan form setelah berhasil mengedit
                clearForm();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        } finally {
            Database.closeConnection();
        }
    }//GEN-LAST:event_editButtonActionPerformed

    private void clearButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearButtonActionPerformed
        // TODO add your handling code here:
        clearForm();
    }//GEN-LAST:event_clearButtonActionPerformed

    private void deleteButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_deleteButtonActionPerformed
        // TODO add your handling code here:
        // Ambil NIM dari nimTextField
        String No = noTextField.getText();

        // Validasi NIM tidak boleh kosong
        if (No.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Silakan pilih data yang akan dihapus terlebih dahulu.");
            return;
        }

        // Tampilkan dialog konfirmasi penghapusan
        int confirm = JOptionPane.showConfirmDialog(this, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus Data", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                // Buat koneksi ke database
                Connection conn = Database.getConnection();

                // Buat query untuk menghapus data berdasarkan NIM
                String sql = "DELETE FROM kegiatan WHERE No = ?";
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, No);
                    // Eksekusi query
                    int rowsDeleted = stmt.executeUpdate();
                    // Tampilkan pesan berhasil jika data berhasil dihapus
                    if (rowsDeleted > 0) {
                        JOptionPane.showMessageDialog(this, "Data berhasil dihapus.");
                        clearForm(); // Bersihkan form setelah penghapusan berhasil
                    } else {
                        JOptionPane.showMessageDialog(this, "Gagal menghapus data. NIM tidak ditemukan.");
                    }
                }
                updateKegiatanTable();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Terjadi kesalahan saat menghapus data.");
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
        String searchSQL = "SELECT * FROM kegiatan WHERE No = ?";

        try (Connection connection = Database.getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(searchSQL)) {
            preparedStatement.setString(1, nomor);

            try (ResultSet rs = preparedStatement.executeQuery()) {
                if (rs.next()) {
                    // Jika data ditemukan, ambil nilai dari hasil query
                    String Date = rs.getString("date");
                    String nama = rs.getString("namaKegiatan");
                    String lokasi = rs.getString("lokasi");
                    String status = rs.getString("status");

                    // Set nilai ke dalam field masing-masing
                    noTextField.setText(nomor);
                    namaKegiatanTextField.setText(nama);
                    lokasiTextField.setText(lokasi);
                    statusComboBox.setSelectedItem(status);
                    // Parse tanggal ke dalam format yang sesuai dengan JDateChooser
                    try {
                        Date date = new SimpleDateFormat("yyyy-MM-dd").parse(Date);
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

    private void kegiatanTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_kegiatanTableMouseClicked
        // TODO add your handling code here:
        // Dapatkan baris yang dipilih
        int row = kegiatanTable.getSelectedRow();

        // Pastikan klik valid (misalnya, tidak ada yang dipilih)
        if (row == -1) {
            return;
        }

        // Ambil nilai dari setiap kolom yang sesuai
        DefaultTableModel model = (DefaultTableModel) kegiatanTable.getModel();
        int id = (int) model.getValueAt(row, 0); // Ubah sesuai dengan indeks kolom id
        int no = (int) model.getValueAt(row, 1); // Ubah sesuai dengan indeks kolom No
        String tanggal = (String) model.getValueAt(row, 2); // Ubah sesuai dengan indeks kolom Tanggal
        String namaKegiatan = (String) model.getValueAt(row, 3); // Ubah sesuai dengan indeks kolom Nama Kegiatan
        String lokasi = (String) model.getValueAt(row, 4); // Ubah sesuai dengan indeks kolom Lokasi
        String status = (String) model.getValueAt(row, 5); // Ubah sesuai dengan indeks kolom Status

        // Tampilkan nilai di field atau combobox yang sesuai
        noTextField.setText(String.valueOf(no));

        // Konversi tanggal dari String ke Date
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
            tanggalDateChooser.setDate(date);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }

        namaKegiatanTextField.setText(namaKegiatan);
        lokasiTextField.setText(lokasi);

        // Pilih item combobox status
        if (status.equals("Belum Terlaksana")) {
            statusComboBox.setSelectedIndex(0);
        } else if (status.equals("Terlaksana")) {
            statusComboBox.setSelectedIndex(1);
        }
    }//GEN-LAST:event_kegiatanTableMouseClicked

    private void noTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_noTextFieldKeyReleased
        // TODO add your handling code here:
        if (ValidasiKegiatan.cekNo(noTextField.getText()) == false) {
            errorNo.setVisible(true);
            validasi = false;
        } else {
            errorNo.setVisible(false);
            validasi = true;
        }
    }//GEN-LAST:event_noTextFieldKeyReleased

    private void exportPDFButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exportPDFButtonActionPerformed
        // TODO add your handling code here:
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Specify a file save");
        fileChooser.setFileFilter(new FileNameExtensionFilter("PDF Document (*.pdf)", "pdf"));
        int userSelection = fileChooser.showSaveDialog(this);
        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File saveFile = fileChooser.getSelectedFile();
            String filePath = saveFile.getAbsolutePath();

            // Tambahkan ekstensi .pdf jika belum ada
            if (!filePath.toLowerCase().endsWith(".pdf")) {
                saveFile = new File(filePath + ".pdf");
            }

            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(saveFile));
                document.open();

                // Header
                Font fontHeader = new Font(Font.FontFamily.COURIER, 20, Font.BOLD);
                Paragraph header = new Paragraph();
                header.setAlignment(Element.ALIGN_CENTER);
                // Tambahkan teks header
                header.add(new Chunk(" KEGIATAN MAVIA'S", fontHeader));
                document.add(header);

                // Spasi antara header dan tabel
                document.add(new Paragraph(" "));

                // Tabel
                PdfPTable table = new PdfPTable(kegiatanTable.getColumnCount());
                for (int i = 0; i < kegiatanTable.getColumnCount(); i++) {
                    PdfPCell cell = new PdfPCell(new Paragraph(kegiatanTable.getColumnName(i)));
                    table.addCell(cell);
                }
                for (int row = 0; row < kegiatanTable.getRowCount(); row++) {
                    for (int col = 0; col < kegiatanTable.getColumnCount(); col++) {
                        Object value = kegiatanTable.getValueAt(row, col);
                        if (value != null) {
                            PdfPCell cell = new PdfPCell(new Paragraph(value.toString()));
                            table.addCell(cell);
                        } else {
                            PdfPCell cell = new PdfPCell(new Paragraph("NULL"));
                            table.addCell(cell);
                        }
                    }
                }
                document.add(table);

                // Footer
                Font fontFooter = new Font(Font.FontFamily.HELVETICA, 20, Font.ITALIC);
                Paragraph footer = new Paragraph("KEPENGERUSAN MAVIA'S 2024", fontFooter);
                footer.setAlignment(Element.ALIGN_CENTER);
                footer.setSpacingBefore(20); // Tambahkan jarak antara tabel dan footer
                document.add(footer);

                document.close();
                JOptionPane.showMessageDialog(this, "PDF successfully exported!", "Export Success", JOptionPane.INFORMATION_MESSAGE);

            } catch (FileNotFoundException | DocumentException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error exporting PDF", "Export Error", JOptionPane.ERROR_MESSAGE);
            } catch (IOException ex) {
                Logger.getLogger(TabelMahasiswa.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }//GEN-LAST:event_exportPDFButtonActionPerformed

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
            java.util.logging.Logger.getLogger(Kegiatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Kegiatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Kegiatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Kegiatan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
    private javax.swing.JLabel errorNo;
    private javax.swing.JButton exportPDFButton;
    private javax.swing.JFileChooser jFileChooser;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable kegiatanTable;
    private javax.swing.JLabel logo;
    private javax.swing.JTextField lokasiTextField;
    private javax.swing.JButton menuButton;
    private javax.swing.JTextField namaKegiatanTextField;
    private javax.swing.JTextField noTextField;
    private javax.swing.JButton saveButton;
    private javax.swing.JButton searchButton;
    private javax.swing.JComboBox<String> statusComboBox;
    private com.toedter.calendar.JDateChooser tanggalDateChooser;
    // End of variables declaration//GEN-END:variables
}
