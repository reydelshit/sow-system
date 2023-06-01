
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Date;
import javax.swing.JOptionPane;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Reydel
 */
public class WEANING_UPDATEMODAL extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;

    /**
     * Creates new form WEANING_UPDATEMODAL
     */
    public WEANING_UPDATEMODAL() {
        setUndecorated(true);
        conn = DBConnection.getConnection();
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel12 = new javax.swing.JPanel();
        rSButtonHover15 = new rojeru_san.complementos.RSButtonHover();
        WEANING_AW = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        WEANING_TOTAL = new javax.swing.JLabel();
        WEANING_MALE = new javax.swing.JTextField();
        WEANING_FEMALE = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        WEANING_CALENDAR = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        UPDATE_WEANING_EARTAG = new javax.swing.JLabel();
        rSButtonHover13 = new rojeru_san.complementos.RSButtonHover();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel12.setBackground(new java.awt.Color(26, 46, 53));
        jPanel12.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSButtonHover15.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover15.setText("SUBMIT");
        rSButtonHover15.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover15.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover15.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover15ActionPerformed(evt);
            }
        });
        jPanel12.add(rSButtonHover15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 390, 210, -1));

        WEANING_AW.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel12.add(WEANING_AW, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 210, 40));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel29.setForeground(new java.awt.Color(255, 217, 90));
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("AVERAGE WEIGHT");
        jPanel12.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 300, 210, 30));

        jPanel9.setBackground(new java.awt.Color(153, 153, 153));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        WEANING_TOTAL.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        WEANING_TOTAL.setForeground(new java.awt.Color(255, 217, 90));
        WEANING_TOTAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel9.add(WEANING_TOTAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 40));

        jPanel12.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 210, 40));

        WEANING_MALE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel12.add(WEANING_MALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 180, 70, 40));

        WEANING_FEMALE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel12.add(WEANING_FEMALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 180, 70, 40));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel33.setForeground(new java.awt.Color(255, 217, 90));
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("F");
        jPanel12.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 150, 50, 30));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel31.setForeground(new java.awt.Color(255, 217, 90));
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("M");
        jPanel12.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 150, 50, 30));

        jButton6.setText("=");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel12.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 190, 40, -1));
        jPanel12.add(WEANING_CALENDAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 210, 40));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel30.setForeground(new java.awt.Color(255, 217, 90));
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("TOTAL");
        jPanel12.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 220, 210, 30));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel28.setForeground(new java.awt.Color(255, 217, 90));
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("EARTAG");
        jPanel12.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 80, 30));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel34.setForeground(new java.awt.Color(255, 217, 90));
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("DATE");
        jPanel12.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 50, 30));

        UPDATE_WEANING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        UPDATE_WEANING_EARTAG.setForeground(new java.awt.Color(255, 217, 90));
        UPDATE_WEANING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        UPDATE_WEANING_EARTAG.setText("120312");
        jPanel12.add(UPDATE_WEANING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 150, 50));

        rSButtonHover13.setBackground(new java.awt.Color(204, 204, 204));
        rSButtonHover13.setText("CANCEL");
        rSButtonHover13.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover13.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover13.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover13ActionPerformed(evt);
            }
        });
        jPanel12.add(rSButtonHover13, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 450, 120, 40));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 280, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 509, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addComponent(jPanel12, javax.swing.GroupLayout.PREFERRED_SIZE, 509, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void rSButtonHover15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover15ActionPerformed
        UPDATE_WEANING_RECORD();
    }//GEN-LAST:event_rSButtonHover15ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed

        if (WEANING_MALE.getText().isEmpty() || !WEANING_MALE.getText().matches("\\d+")
                || WEANING_FEMALE.getText().isEmpty() || !WEANING_FEMALE.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty and have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int maleCount = WEANING_MALE.getText().isEmpty() ? 0 : Integer.parseInt(WEANING_MALE.getText());
            int femaleCount = WEANING_FEMALE.getText().isEmpty() ? 0 : Integer.parseInt(WEANING_FEMALE.getText());

            int total = maleCount + femaleCount;
            WEANING_TOTAL.setText(Integer.toString(total));
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void rSButtonHover13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover13ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_rSButtonHover13ActionPerformed

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
            java.util.logging.Logger.getLogger(WEANING_UPDATEMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WEANING_UPDATEMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WEANING_UPDATEMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WEANING_UPDATEMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new WEANING_UPDATEMODAL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel UPDATE_WEANING_EARTAG;
    private javax.swing.JTextField WEANING_AW;
    private com.toedter.calendar.JDateChooser WEANING_CALENDAR;
    private javax.swing.JTextField WEANING_FEMALE;
    private javax.swing.JTextField WEANING_MALE;
    private javax.swing.JLabel WEANING_TOTAL;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel9;
    private rojeru_san.complementos.RSButtonHover rSButtonHover13;
    private rojeru_san.complementos.RSButtonHover rSButtonHover15;
    // End of variables declaration//GEN-END:variables

    public void WEANING_RETRIEVE_RECORD(String eartag) {
        UPDATE_WEANING_EARTAG.setText(eartag);
        try {
            String query = "SELECT weaning_actualdate, male_piglets, female_piglets, total_piglets, aw FROM weaning_records WHERE eartag = ?";

            pst = conn.prepareStatement(query);
            pst.setString(1, eartag);
            rs = pst.executeQuery();

            while (rs.next()) {
                Date weaningDate = rs.getDate("weaning_actualdate");
                int malePiglets = rs.getInt("male_piglets");
                int femalePiglets = rs.getInt("female_piglets");
                int totalPiglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");

                // Assuming you have corresponding fields or components to display the retrieved data
                WEANING_CALENDAR.setDate(weaningDate);
                WEANING_MALE.setText(String.valueOf(malePiglets));
                WEANING_FEMALE.setText(String.valueOf(femalePiglets));
                WEANING_TOTAL.setText(String.valueOf(totalPiglets));
                WEANING_AW.setText(String.valueOf(aw));
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    public void UPDATE_WEANING_RECORD() {
        Date selectedDate = WEANING_CALENDAR.getDate();
        String dateString = new java.sql.Date(selectedDate.getTime()).toString();

        try {
            String query = "UPDATE weaning_records SET weaning_actualdate = ?, male_piglets = ?, female_piglets = ?, total_piglets = ?, aw = ? WHERE eartag = ?";

            pst = conn.prepareStatement(query);
            pst.setString(1, dateString);
            pst.setInt(2, Integer.parseInt(WEANING_MALE.getText()));
            pst.setInt(3, Integer.parseInt(WEANING_FEMALE.getText()));
            pst.setInt(4, Integer.parseInt(WEANING_TOTAL.getText()));
            pst.setDouble(5, Double.parseDouble(WEANING_AW.getText()));
            pst.setString(6, UPDATE_WEANING_EARTAG.getText());

            pst.executeUpdate();
            JOptionPane.showMessageDialog(null, "Weaning record updated successfully.");

            // Clear the fields after submission
            WEANING_CALENDAR.setDate(null);
            WEANING_MALE.setText("");
            WEANING_FEMALE.setText("");
            WEANING_TOTAL.setText("");
            WEANING_AW.setText("");
            UPDATE_WEANING_EARTAG.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
