
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Calendar;
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
public class REBREEDINGMODAL extends javax.swing.JFrame {

    private String eartag;

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

    }

    public void setEartag(String eartag) {
        this.eartag = eartag;
        REBREEDING_EARTAG.setText(eartag);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        REBREEDING_EXPECTED_DATE = new javax.swing.JLabel();
        REBREEDING_DATE = new com.toedter.calendar.JDateChooser();
        jLabel2 = new javax.swing.JLabel();
        REBREEDING_BOARD_USED = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        REBREEDING_EARTAG = new javax.swing.JLabel();
        REBREEDING_COMMENTS = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        rSButtonHover1 = new rojeru_san.complementos.RSButtonHover();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(26, 46, 53));
        jPanel1.setForeground(new java.awt.Color(255, 217, 90));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        REBREEDING_EXPECTED_DATE.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        REBREEDING_EXPECTED_DATE.setForeground(new java.awt.Color(255, 217, 90));
        REBREEDING_EXPECTED_DATE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        REBREEDING_EXPECTED_DATE.setText("EXPECTED FARROWING");
        jPanel1.add(REBREEDING_EXPECTED_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 190, 190, 50));

        REBREEDING_DATE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                REBREEDING_DATEPropertyChange(evt);
            }
        });
        jPanel1.add(REBREEDING_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 140, 200, 40));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 217, 90));
        jLabel2.setText("COMMENTS");
        jPanel1.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 320, 110, -1));
        jPanel1.add(REBREEDING_BOARD_USED, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 270, 200, 40));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 217, 90));
        jLabel3.setText("DATE");
        jPanel1.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 120, 110, -1));

        REBREEDING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        REBREEDING_EARTAG.setForeground(new java.awt.Color(255, 217, 90));
        REBREEDING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        REBREEDING_EARTAG.setText("EARTAG HERE");
        jPanel1.add(REBREEDING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 60, 190, 50));
        jPanel1.add(REBREEDING_COMMENTS, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 340, 200, 90));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 217, 90));
        jLabel5.setText("BOAR USED");
        jPanel1.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 250, 110, -1));

        jButton2.setText("CANCEL");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 530, -1, -1));

        rSButtonHover1.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover1.setText("REBREED");
        rSButtonHover1.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover1.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover1.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 460, 160, -1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 419, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 619, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void REBREEDING_DATEPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_REBREEDING_DATEPropertyChange
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

                REBREEDING_EXPECTED_DATE.setText(expectedFarrowing);
            }

            System.out.println("Reydel");

        }
    }//GEN-LAST:event_REBREEDING_DATEPropertyChange

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed
        REBREEDING();
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        setVisible(false);
    }//GEN-LAST:event_jButton2ActionPerformed

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
    private javax.swing.JTextField REBREEDING_BOARD_USED;
    private javax.swing.JTextField REBREEDING_COMMENTS;
    private com.toedter.calendar.JDateChooser REBREEDING_DATE;
    private javax.swing.JLabel REBREEDING_EARTAG;
    private javax.swing.JLabel REBREEDING_EXPECTED_DATE;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private rojeru_san.complementos.RSButtonHover rSButtonHover1;
    // End of variables declaration//GEN-END:variables

    private void REBREEDING() {
        try {
            boolean setFarrowedFalse = false;
            boolean isCulling = false;
            int parity = 0;

            Date selectedDate = REBREEDING_DATE.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();

            String checkSql = "SELECT eartag, culled, parity, breeding_status, farrowed FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, REBREEDING_EARTAG.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                boolean isCulled = rs.getBoolean("culled");
                boolean isFarrowed = rs.getBoolean("farrowed");
                boolean isCurrentlyBreeding = rs.getBoolean("breeding_status");
                parity = rs.getInt("parity");
                if (!isCulled) {
                    if (!isFarrowed && !isCurrentlyBreeding) {
                        String sql = "INSERT INTO breeding (eartag, boar_used, breeding_date, expected_farrowing, comments, farrowed, parity, culled, rebreed, breeding_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, REBREEDING_EARTAG.getText());
                        pst.setString(2, REBREEDING_BOARD_USED.getText());
                        pst.setString(3, dateString);
                        pst.setString(4, REBREEDING_EXPECTED_DATE.getText());
                        pst.setString(5, REBREEDING_COMMENTS.getText());
                        pst.setBoolean(6, setFarrowedFalse);
                        parity++;
                        pst.setInt(7, parity);
                        pst.setBoolean(8, isCulling);
                        pst.setBoolean(9, !isCurrentlyBreeding); // Set rebreed status based on breeding status
                        pst.setBoolean(10, !isCurrentlyBreeding); // Set breeding status

                        pst.execute();

                        JOptionPane.showMessageDialog(null, REBREEDING_EARTAG.getText() + " is now breeding!");

                        REBREEDING_EARTAG.setText("");
                        REBREEDING_BOARD_USED.setText("");
                        REBREEDING_DATE.setDate(null);
                        REBREEDING_EXPECTED_DATE.setText("");
                        REBREEDING_COMMENTS.setText("");
                    } else {
                        JOptionPane.showMessageDialog(null, REBREEDING_EARTAG.getText() + " is farrowed or currently breeding");
                    }
                } else {
                    JOptionPane.showMessageDialog(null, REBREEDING_EARTAG.getText() + " already exists in the breeding table and is marked as culled.");
                    REBREEDING_EARTAG.setText("");
                    REBREEDING_BOARD_USED.setText("");
                    REBREEDING_DATE.setDate(null);
                    REBREEDING_EXPECTED_DATE.setText("");
                    REBREEDING_COMMENTS.setText("");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }
}