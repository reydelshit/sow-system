
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Date;
import javax.swing.ButtonGroup;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Reydel
 */
public class REBREEDINGMODAL extends javax.swing.JFrame {

    private String eartag;
    private String batch;

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
//    

    /**
     * Creates new form REBREEDINGMODAL
     */
    public REBREEDINGMODAL() {
        setUndecorated(true);
        conn = DBConnection.getConnection();
        initComponents();

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(LACTATE_YES);
        buttonGroup.add(LACTATE_NO);

        REBREEDING_BATCH_NUMBER.setVisible(false);
        REBREEDING_LACTATE.setVisible(false);

    }

    public void setEartag(String eartag, String batch) {
        this.eartag = eartag;
        this.batch = batch;
        REBREEDING_EARTAG.setText(eartag);
        REBREEDING_BATCH_NUMBER.setText(batch);
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
        REBREEDING_DATE = new com.toedter.calendar.JDateChooser();
        REBREEDING_BOAR_USED = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        rSButtonHover11 = new rojeru_san.complementos.RSButtonHover();
        jLabel16 = new javax.swing.JLabel();
        EXPECTED_FARROWING_LABEL = new javax.swing.JPanel();
        REBREEDING_EXPECTED_FARROWING = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        REBREEDING_EARTAG = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        REBREEDING_BREEDING_TYPE = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        rSButtonHover12 = new rojeru_san.complementos.RSButtonHover();
        jLabel20 = new javax.swing.JLabel();
        LACTATE_YES = new javax.swing.JRadioButton();
        LACTATE_NO = new javax.swing.JRadioButton();
        REBREEDING_LACTATE = new javax.swing.JLabel();
        REBREEDING_BATCH_NUMBER = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        LACTATE_SCHEDULE = new javax.swing.JComboBox<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        BREEDING_CONTAINER.setBackground(new java.awt.Color(26, 46, 53));
        BREEDING_CONTAINER.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        REBREEDING_DATE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                REBREEDING_DATEPropertyChange(evt);
            }
        });
        BREEDING_CONTAINER.add(REBREEDING_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 220, 40));

        REBREEDING_BOAR_USED.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        BREEDING_CONTAINER.add(REBREEDING_BOAR_USED, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 160, 220, 40));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 217, 90));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BOAR USED");
        BREEDING_CONTAINER.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 140, 220, 20));

        rSButtonHover11.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover11.setText("START REBREEDING");
        rSButtonHover11.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover11.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover11.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover11ActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(rSButtonHover11, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 380, 190, 40));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 217, 90));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("EXPECTED FARROWING");
        BREEDING_CONTAINER.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 280, 220, 20));

        EXPECTED_FARROWING_LABEL.setBackground(new java.awt.Color(153, 153, 153));
        EXPECTED_FARROWING_LABEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        REBREEDING_EXPECTED_FARROWING.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        REBREEDING_EXPECTED_FARROWING.setForeground(new java.awt.Color(255, 217, 90));
        REBREEDING_EXPECTED_FARROWING.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EXPECTED_FARROWING_LABEL.add(REBREEDING_EXPECTED_FARROWING, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 40));

        BREEDING_CONTAINER.add(EXPECTED_FARROWING_LABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 310, 220, 40));

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        REBREEDING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        REBREEDING_EARTAG.setForeground(new java.awt.Color(255, 217, 90));
        REBREEDING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(REBREEDING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 40));

        BREEDING_CONTAINER.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 70, 220, 40));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 217, 90));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("EAR TAG");
        BREEDING_CONTAINER.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 50, 220, 20));

        REBREEDING_BREEDING_TYPE.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Al", "Kasta" }));
        BREEDING_CONTAINER.add(REBREEDING_BREEDING_TYPE, new org.netbeans.lib.awtextra.AbsoluteConstraints(350, 160, 220, 40));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 217, 90));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("BREEDING DATE");
        BREEDING_CONTAINER.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 210, 220, 20));

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel19.setForeground(new java.awt.Color(255, 217, 90));
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("BREEDING TYPE");
        BREEDING_CONTAINER.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 140, 280, 20));

        rSButtonHover12.setBackground(new java.awt.Color(255, 51, 51));
        rSButtonHover12.setText("CANCEL");
        rSButtonHover12.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover12.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover12.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover12ActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(rSButtonHover12, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 380, 170, 40));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 217, 90));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("LACTATE");
        BREEDING_CONTAINER.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(320, 210, 270, 20));

        LACTATE_YES.setBackground(new java.awt.Color(26, 46, 53));
        LACTATE_YES.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LACTATE_YES.setForeground(new java.awt.Color(255, 217, 90));
        LACTATE_YES.setText("YES");
        LACTATE_YES.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LACTATE_YESActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(LACTATE_YES, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 240, -1, -1));

        LACTATE_NO.setBackground(new java.awt.Color(26, 46, 53));
        LACTATE_NO.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        LACTATE_NO.setForeground(new java.awt.Color(255, 217, 90));
        LACTATE_NO.setText("NO");
        LACTATE_NO.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LACTATE_NOActionPerformed(evt);
            }
        });
        BREEDING_CONTAINER.add(LACTATE_NO, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 240, -1, -1));

        REBREEDING_LACTATE.setText("yes");
        BREEDING_CONTAINER.add(REBREEDING_LACTATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(570, 230, 60, 30));

        REBREEDING_BATCH_NUMBER.setText("batch here");
        BREEDING_CONTAINER.add(REBREEDING_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 60, -1));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 217, 90));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("LACTATE SCHEDULE");
        BREEDING_CONTAINER.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(330, 280, 270, 20));

        LACTATE_SCHEDULE.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Once a week", "Twice a week", "Thrice a week" }));
        BREEDING_CONTAINER.add(LACTATE_SCHEDULE, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 300, 210, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(BREEDING_CONTAINER, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(BREEDING_CONTAINER, javax.swing.GroupLayout.PREFERRED_SIZE, 486, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void REBREEDING_DATEPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_REBREEDING_DATEPropertyChange
        // TODO add your handling code here:
        if (evt.getPropertyName().equals("date")) {
            Date selectedDate = null;
            if (REBREEDING_DATE.getCalendar() != null) {
                selectedDate = REBREEDING_DATE.getCalendar().getTime();
            }

            if (selectedDate != null) {
                Calendar cal = Calendar.getInstance();
                cal.setTime(selectedDate);
                cal.add(Calendar.DAY_OF_MONTH, 114);
                //                cal.add(Calendar.MINUTE, 5);
                Date expectedFarrowingDate = cal.getTime();
                String expectedFarrowing = new java.sql.Date(expectedFarrowingDate.getTime()).toString();

                REBREEDING_EXPECTED_FARROWING.setText(expectedFarrowing);
            }

            System.out.println("Reydel");

        }
    }//GEN-LAST:event_REBREEDING_DATEPropertyChange

    private void rSButtonHover11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover11ActionPerformed

        String boarUsed = REBREEDING_BOAR_USED.getText().trim();

        if (boarUsed.isEmpty() || !boarUsed.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid input for boarUsed. Please make sure it is a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (REBREEDING_DATE.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            REBREEDING();
        }
    }//GEN-LAST:event_rSButtonHover11ActionPerformed

    private void rSButtonHover12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover12ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_rSButtonHover12ActionPerformed

    private void LACTATE_YESActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LACTATE_YESActionPerformed
        REBREEDING_LACTATE.setText("yes");
    }//GEN-LAST:event_LACTATE_YESActionPerformed

    private void LACTATE_NOActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LACTATE_NOActionPerformed
        REBREEDING_LACTATE.setText("no");
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
            java.util.logging.Logger.getLogger(REBREEDINGMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(REBREEDINGMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(REBREEDINGMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(REBREEDINGMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new REBREEDINGMODAL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel BREEDING_CONTAINER;
    private javax.swing.JPanel EXPECTED_FARROWING_LABEL;
    private javax.swing.JRadioButton LACTATE_NO;
    private javax.swing.JComboBox<String> LACTATE_SCHEDULE;
    private javax.swing.JRadioButton LACTATE_YES;
    private javax.swing.JLabel REBREEDING_BATCH_NUMBER;
    private javax.swing.JTextField REBREEDING_BOAR_USED;
    private javax.swing.JComboBox<String> REBREEDING_BREEDING_TYPE;
    private com.toedter.calendar.JDateChooser REBREEDING_DATE;
    private javax.swing.JLabel REBREEDING_EARTAG;
    private javax.swing.JLabel REBREEDING_EXPECTED_FARROWING;
    private javax.swing.JLabel REBREEDING_LACTATE;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JPanel jPanel4;
    private rojeru_san.complementos.RSButtonHover rSButtonHover11;
    private rojeru_san.complementos.RSButtonHover rSButtonHover12;
    // End of variables declaration//GEN-END:variables

    private void REBREEDING() {
        try {

            Date selectedDate = REBREEDING_DATE.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();

            String checkSql = "SELECT eartag, MAX(parity) AS max_parity, sow_status FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, REBREEDING_EARTAG.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                int sow_status = rs.getInt("sow_status");
                int maxParity = rs.getInt("max_parity");
                int parity = maxParity + 1;

                if (sow_status != 3) {
                    if (sow_status != 1 && sow_status != 0) {
                        String sql = "INSERT INTO breeding (eartag, boar_used, breeding_date, expected_farrowing, rebreed, breeding_type, lactate, sow_status, parity, batch_number, weaning_status, farrowing_status, lactate_sched) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?, ?, ?)";
                        pst = conn.prepareStatement(sql);

                        pst.setString(1, REBREEDING_EARTAG.getText());
                        pst.setString(2, REBREEDING_BOAR_USED.getText());
                        pst.setString(3, dateString);
                        pst.setString(4, REBREEDING_EXPECTED_FARROWING.getText());

                        pst.setBoolean(5, true);
                        pst.setString(6, (String) REBREEDING_BREEDING_TYPE.getSelectedItem());
                        pst.setString(7, REBREEDING_LACTATE.getText());
                        pst.setInt(8, 0);

                        pst.setInt(9, parity);
                        pst.setString(10, REBREEDING_BATCH_NUMBER.getText());
                        pst.setBoolean(11, false);
                        pst.setBoolean(12, false);
                        pst.setString(13, (String) LACTATE_SCHEDULE.getSelectedItem());

                        pst.execute();

                        JOptionPane.showMessageDialog(null, REBREEDING_EARTAG.getText() + " is now rebreeding!");
                        REBREEDING_EARTAG.setText("");
                        REBREEDING_BOAR_USED.setText("");
                        REBREEDING_EXPECTED_FARROWING.setText("");
                        REBREEDING_BREEDING_TYPE.setSelectedIndex(0);
                        REBREEDING_LACTATE.setText("");

                        ButtonGroup buttonGroup = new ButtonGroup();
                        buttonGroup.add(LACTATE_YES);
                        buttonGroup.add(LACTATE_NO);

                        buttonGroup.clearSelection();
                    } else {
                        JOptionPane.showMessageDialog(null, REBREEDING_EARTAG.getText() + " is farrowed or currently breeding");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, REBREEDING_EARTAG.getText() + " already exists in the breeding table and is marked as culled.");
                }

                REBREEDING_EARTAG.setText("");
                REBREEDING_BOAR_USED.setText("");
                REBREEDING_EXPECTED_FARROWING.setText("");
                REBREEDING_BREEDING_TYPE.setSelectedIndex(0);
                REBREEDING_LACTATE.setText("");

                ButtonGroup buttonGroup = new ButtonGroup();
                buttonGroup.add(LACTATE_YES);
                buttonGroup.add(LACTATE_NO);

                buttonGroup.clearSelection();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}
