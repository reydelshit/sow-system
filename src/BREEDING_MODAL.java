
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Reydel
 */
public class BREEDING_MODAL extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form BREEDING_MODAL
     */
    public BREEDING_MODAL() {
        conn = DBConnection.getConnection();
        setUndecorated(true);

        initComponents();
        BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER();

        BREEDING_LACTATE.setVisible(false);

        BREEDING_BATCH_NUMBER.setVisible(false);

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(LACTATE_YES);
        buttonGroup.add(LACTATE_NO);

        LIST_OF_SOW_BY_BATCH.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();
                int eartag = Integer.parseInt(table.getValueAt(row, 0).toString());
                BREEDING_BATCH_NUMBER.setText(table.getValueAt(row, 1).toString());
                BREEDING_EARTAG.setText(Integer.toString(eartag));

            }
        });

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        BREEDING_CONTAINER = new javax.swing.JPanel();
        BREEDING_DATE = new com.toedter.calendar.JDateChooser();
        BREEDING_BOAR_USED = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        LIST_OF_SOW_BY_BATCH = new rojeru_san.complementos.RSTableMetro();
        DROPDOWN_FOR_BATCH_NUMBER = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        rSButtonHover11 = new rojeru_san.complementos.RSButtonHover();
        jLabel16 = new javax.swing.JLabel();
        EXPECTED_FARROWING_LABEL = new javax.swing.JPanel();
        BREEDING_EXPECTED_FARROWING = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        BREEDING_EARTAG = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        BREEDING_BREEDING_TYPE = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        rSButtonHover12 = new rojeru_san.complementos.RSButtonHover();
        LACTATE_NO = new javax.swing.JRadioButton();
        LACTATE_YES = new javax.swing.JRadioButton();
        BREEDING_LACTATE = new javax.swing.JLabel();
        BREEDING_BATCH_NUMBER = new javax.swing.JLabel();
        LACTATE_SCHEDULE = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BREEDING_CONTAINER.setBackground(new java.awt.Color(26, 46, 53));
        BREEDING_CONTAINER.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BREEDING_DATE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                BREEDING_DATEPropertyChange(evt);
            }
        });
        BREEDING_CONTAINER.add(BREEDING_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 300, 220, 40));

        BREEDING_BOAR_USED.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        BREEDING_CONTAINER.add(BREEDING_BOAR_USED, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 220, 40));

        LIST_OF_SOW_BY_BATCH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Eartag", "Batch"
            }
        ));
        LIST_OF_SOW_BY_BATCH.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        LIST_OF_SOW_BY_BATCH.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        LIST_OF_SOW_BY_BATCH.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        LIST_OF_SOW_BY_BATCH.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        LIST_OF_SOW_BY_BATCH.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        LIST_OF_SOW_BY_BATCH.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        LIST_OF_SOW_BY_BATCH.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane3.setViewportView(LIST_OF_SOW_BY_BATCH);

        BREEDING_CONTAINER.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 60, 310, 140));

        DROPDOWN_FOR_BATCH_NUMBER.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT BATCH" }));
        BREEDING_CONTAINER.add(DROPDOWN_FOR_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 80, 150, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 217, 90));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("LIST OF SOW");
        BREEDING_CONTAINER.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 30, 230, 20));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 217, 90));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BOAR USED");
        BREEDING_CONTAINER.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 220, 20));

        rSButtonHover11.setBackground(new java.awt.Color(255, 217, 90));
        rSButtonHover11.setText("START BREEDING");
        rSButtonHover11.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover11.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover11.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover11ActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(rSButtonHover11, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 450, 170, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 217, 90));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("EXPECTED FARROWING");
        BREEDING_CONTAINER.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 350, 220, 20));

        EXPECTED_FARROWING_LABEL.setBackground(new java.awt.Color(153, 153, 153));
        EXPECTED_FARROWING_LABEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BREEDING_EXPECTED_FARROWING.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BREEDING_EXPECTED_FARROWING.setForeground(new java.awt.Color(255, 217, 90));
        BREEDING_EXPECTED_FARROWING.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EXPECTED_FARROWING_LABEL.add(BREEDING_EXPECTED_FARROWING, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 40));

        BREEDING_CONTAINER.add(EXPECTED_FARROWING_LABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 380, 220, 40));

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BREEDING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BREEDING_EARTAG.setForeground(new java.awt.Color(255, 217, 90));
        BREEDING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(BREEDING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 40));

        BREEDING_CONTAINER.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 220, 40));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 217, 90));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("EAR TAG");
        BREEDING_CONTAINER.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 220, 20));

        BREEDING_BREEDING_TYPE.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Al", "Kasta" }));
        BREEDING_CONTAINER.add(BREEDING_BREEDING_TYPE, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 230, 200, 40));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 217, 90));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("BREEDING DATE");
        BREEDING_CONTAINER.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 220, 20));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 217, 90));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("BREEDING TYPE");
        BREEDING_CONTAINER.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 210, 230, 20));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 217, 90));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("LACTATE SCHEDULE");
        BREEDING_CONTAINER.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 360, 270, 20));

        rSButtonHover12.setBackground(new java.awt.Color(204, 204, 204));
        rSButtonHover12.setText("CANCEL");
        rSButtonHover12.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover12.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover12.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover12ActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(rSButtonHover12, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 450, 170, 40));

        LACTATE_NO.setBackground(new java.awt.Color(26, 46, 53));
        LACTATE_NO.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LACTATE_NO.setForeground(new java.awt.Color(255, 217, 90));
        LACTATE_NO.setText("NO");
        LACTATE_NO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LACTATE_NOActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(LACTATE_NO, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 310, -1, -1));

        LACTATE_YES.setBackground(new java.awt.Color(26, 46, 53));
        LACTATE_YES.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LACTATE_YES.setForeground(new java.awt.Color(255, 217, 90));
        LACTATE_YES.setText("YES");
        LACTATE_YES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LACTATE_YESActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(LACTATE_YES, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 310, -1, -1));

        BREEDING_LACTATE.setText("yes");
        BREEDING_CONTAINER.add(BREEDING_LACTATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 300, 60, 30));

        BREEDING_BATCH_NUMBER.setText("batch_here");
        BREEDING_CONTAINER.add(BREEDING_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 40, 60, 20));

        LACTATE_SCHEDULE.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Once a week", "Twice a week", "Thrice a week" }));
        BREEDING_CONTAINER.add(LACTATE_SCHEDULE, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 380, 210, 40));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 217, 90));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("LACTATE");
        BREEDING_CONTAINER.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 280, 270, 20));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BREEDING_CONTAINER, javax.swing.GroupLayout.DEFAULT_SIZE, 683, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BREEDING_CONTAINER, javax.swing.GroupLayout.PREFERRED_SIZE, 552, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void BREEDING_DATEPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_BREEDING_DATEPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().equals("date")) {
            Date selectedDate = null;
            if (BREEDING_DATE.getCalendar() != null) {
                selectedDate = BREEDING_DATE.getCalendar().getTime();
            }

            if (selectedDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(selectedDate);
                cal.add(Calendar.DAY_OF_MONTH, 114);
                //                cal.add(Calendar.MINUTE, 5);
                Date expectedFarrowingDate = cal.getTime();
                String expectedFarrowing = new java.sql.Date(expectedFarrowingDate.getTime()).toString();

                BREEDING_EXPECTED_FARROWING.setText(expectedFarrowing);
            }

            System.out.println("Reydel");

        }
    }//GEN-LAST:event_BREEDING_DATEPropertyChange

    private void rSButtonHover11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover11ActionPerformed

        String boarUsed = BREEDING_BOAR_USED.getText().trim();

        if (boarUsed.isEmpty() || !boarUsed.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid input for boarUsed. Please make sure it is a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (BREEDING_DATE.getDate() == null || DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            BREEDING_START_BREEDING();
            BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER();
        }
    }//GEN-LAST:event_rSButtonHover11ActionPerformed

    private void rSButtonHover12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover12ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_rSButtonHover12ActionPerformed

    private void LACTATE_YESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LACTATE_YESActionPerformed
        BREEDING_LACTATE.setText("yes");
    }//GEN-LAST:event_LACTATE_YESActionPerformed

    private void LACTATE_NOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LACTATE_NOActionPerformed
        BREEDING_LACTATE.setText("no");
    }//GEN-LAST:event_LACTATE_NOActionPerformed

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
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(BREEDING_MODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(BREEDING_MODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(BREEDING_MODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(BREEDING_MODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new BREEDING_MODAL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel BREEDING_BATCH_NUMBER;
    private javax.swing.JTextField BREEDING_BOAR_USED;
    private javax.swing.JComboBox<String> BREEDING_BREEDING_TYPE;
    private javax.swing.JPanel BREEDING_CONTAINER;
    private com.toedter.calendar.JDateChooser BREEDING_DATE;
    private javax.swing.JLabel BREEDING_EARTAG;
    private javax.swing.JLabel BREEDING_EXPECTED_FARROWING;
    private javax.swing.JLabel BREEDING_LACTATE;
    private javax.swing.JComboBox<String> DROPDOWN_FOR_BATCH_NUMBER;
    private javax.swing.JPanel EXPECTED_FARROWING_LABEL;
    private javax.swing.JRadioButton LACTATE_NO;
    private javax.swing.JComboBox<String> LACTATE_SCHEDULE;
    private javax.swing.JRadioButton LACTATE_YES;
    private rojeru_san.complementos.RSTableMetro LIST_OF_SOW_BY_BATCH;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane3;
    private rojeru_san.complementos.RSButtonHover rSButtonHover11;
    private rojeru_san.complementos.RSButtonHover rSButtonHover12;
    // End of variables declaration//GEN-END:variables

    private void BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER() {
        try {
            String sql = "SELECT DISTINCT bnumber FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String batchNumber = rs.getString("bnumber");
                DROPDOWN_FOR_BATCH_NUMBER.addItem(batchNumber);
            }

            DROPDOWN_FOR_BATCH_NUMBER.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER();
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER() {
        try {
            DefaultTableModel model = new DefaultTableModel();

            String selectedBatchNumber = (String) DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();

            String query = "SELECT rs.eartag, rs.bnumber FROM register_sow rs LEFT JOIN breeding b ON rs.eartag = b.eartag WHERE rs.bnumber = ? AND b.eartag IS NULL";
            pst = conn.prepareStatement(query);
            pst.setString(1, selectedBatchNumber);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Batch");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                String bnumber = rs.getString("bnumber");

                model.addRow(new Object[]{eartag, bnumber});
            }

            if (LIST_OF_SOW_BY_BATCH != null) {
                LIST_OF_SOW_BY_BATCH.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void BREEDING_START_BREEDING() {
        try {
            Date selectedDate = BREEDING_DATE.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();

            String checkSql = "SELECT eartag, sow_status FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, BREEDING_EARTAG.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                int isCulled = rs.getInt("sow_status");
                if (isCulled == 4) {
                    JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " already exists in the breeding table and is marked as culled.");
                    BREEDING_EARTAG.setText("");
                    BREEDING_BOAR_USED.setText("");
                    BREEDING_DATE.setDate(null);
                    BREEDING_EXPECTED_FARROWING.setText("");
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " already exists in the breeding table.");
                    BREEDING_EARTAG.setText("");
                    BREEDING_BOAR_USED.setText("");
                    BREEDING_DATE.setDate(null);
                    BREEDING_EXPECTED_FARROWING.setText("");

                    return;
                }
            }

            String sql = "INSERT INTO breeding (eartag, batch_number, boar_used, breeding_date, expected_farrowing, rebreed, breeding_type, lactate, sow_status, parity, weaning_status, farrowing_status, lactate_sched ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?)";
            String farrowingUpdate = "SELECT sow_status FROM breeding WHERE eartag = ?";
            String parity = "UPDATE breeding SET parity = 1 WHERE eartag = ?";

            pst = conn.prepareStatement(farrowingUpdate);
            pst.setString(1, BREEDING_EARTAG.getText());
            rs = pst.executeQuery();

            if (rs.next() && rs.getInt("farrowed") == 1) {
                JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " has already been marked as farrowed.");
                BREEDING_EARTAG.setText("");
                BREEDING_BOAR_USED.setText("");
                BREEDING_DATE.setDate(null);
                BREEDING_EXPECTED_FARROWING.setText("");

                ButtonGroup buttonGroup = new ButtonGroup();
                buttonGroup.add(LACTATE_YES);
                buttonGroup.add(LACTATE_NO);

                buttonGroup.clearSelection();

                return;
            }

            pst = conn.prepareStatement(sql);
            pst.setString(1, BREEDING_EARTAG.getText());
            pst.setString(2, BREEDING_BATCH_NUMBER.getText());
            pst.setString(3, BREEDING_BOAR_USED.getText());
            pst.setString(4, dateString);
            pst.setString(5, BREEDING_EXPECTED_FARROWING.getText());
            pst.setBoolean(6, false);
            pst.setString(7, (String) BREEDING_BREEDING_TYPE.getSelectedItem());
            pst.setString(8, BREEDING_LACTATE.getText());
            pst.setInt(9, 0);
            pst.setInt(10, 1);
            pst.setBoolean(11, false);
            pst.setBoolean(12, false);
            pst.setString(13, (String) LACTATE_SCHEDULE.getSelectedItem());

            pst.execute();

            pst = conn.prepareStatement(parity);
            pst.setString(1, BREEDING_EARTAG.getText());
            int rowsAffected = pst.executeUpdate();

            if (rowsAffected > 0) {
                JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " started breeding.");
            } else {
                JOptionPane.showMessageDialog(null, "Failed to update parity for " + BREEDING_EARTAG.getText() + ".");
            }

            BREEDING_EARTAG.setText("");
            BREEDING_BOAR_USED.setText("");
            BREEDING_DATE.setDate(null);
            BREEDING_EXPECTED_FARROWING.setText("");

            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(LACTATE_YES);
            buttonGroup.add(LACTATE_NO);

            buttonGroup.clearSelection();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
