
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

import javax.swing.*;
import org.knowm.xchart.*;
import org.knowm.xchart.style.MatlabTheme;
import org.knowm.xchart.style.Styler.ChartTheme;
import org.knowm.xchart.style.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Reydel
 */
public class OPEMANAGER extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    CardLayout cardLayout;
    
    private JPanel chartPanel;
    private JPanel pieChartPanel;
    
    public OPEMANAGER() {
        conn = DBConnection.getConnection();
        initComponents();
        
        WARNING_FETCH_EARTAG();

        cardLayout = (CardLayout)(PAGES.getLayout());
        
       

        // Create a chart object and set the MATLAB theme
        CategoryChart chartForRegSOw = new CategoryChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).build();

        // Customize the chart
        chartForRegSOw.setTitle("Registered Sows");
        chartForRegSOw.setXAxisTitle("Date");
        chartForRegSOw.setYAxisTitle("Number of Sows");

        // Retrieve data from MySQL database table
        try {
            String query = "SELECT date, COUNT(eartag) AS count FROM register_sow GROUP BY date";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            // Create lists to store x and y values
            List<String> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();

            // Add data to the lists
            while (rs.next()) {
                Date date = rs.getDate("date");
                int count = rs.getInt("count");

                xValues.add(date.toString()); // Convert date to string for category chart
                yValues.add(count);
            }

            // Add series data to the chart
            chartForRegSOw.addSeries("Number of Sows", xValues, yValues);
        } catch (SQLException e) {
            e.printStackTrace();
        }



        // Create a chart panel
        chartPanel = new XChartPanel<>(chartForRegSOw);

        // Set the layout manager for the CHART_PANEL
        CHART_PANEL_REG_SOW.setLayout(new BorderLayout());

        // Add the chart panel to the CHART_PANEL
        CHART_PANEL_REG_SOW.add(chartPanel, BorderLayout.CENTER);

        // Set the JFrame size and visibility
        pack();
        setVisible(true);
        
        
        

         // Create a chart object and set the MATLAB theme
        PieChart chartForPie = new PieChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).build();

        // Customize the chart
        chartForPie.setTitle("Farrowing Records");

        // Retrieve data from the database
        try {
            // Count culled records
            String culledQuery = "SELECT COUNT(*) AS culledCount FROM farrowing_records WHERE culled = 1";
            pst = conn.prepareStatement(culledQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                int culledCount = rs.getInt("culledCount");
                chartForPie.addSeries("Culled (" + culledCount + ")", culledCount);
            }

            // Count farrowed records
            String farrowedQuery = "SELECT COUNT(*) AS farrowedCount FROM farrowing_records WHERE culled = 0";
            pst = conn.prepareStatement(farrowedQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                int farrowedCount = rs.getInt("farrowedCount");
                chartForPie.addSeries("Farrowed (" + farrowedCount + ")", farrowedCount);
            }

            // Count warning records
            String warningQuery = "SELECT COUNT(DISTINCT eartag) AS warningCount " +
                    "FROM farrowing_records " +
                    "WHERE ((female_piglets + male_piglets) < 7 " +
                    "OR mortality > 0 " +
                    "OR remarks IS NOT NULL " +
                    "OR (farrowing_actualdate BETWEEN farrowing_duedate - INTERVAL 3 DAY AND farrowing_duedate + INTERVAL 3 DAY)) " +
                    "AND culled = 0";
            pst = conn.prepareStatement(warningQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                int warningCount = rs.getInt("warningCount");
                 chartForPie.addSeries("Warning (" + warningCount + ")", warningCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        // Create a chart panel
        pieChartPanel = new XChartPanel<>(chartForPie);

        // Set the layout manager for the PANEL_PIE_CHART
        PANEL_PIE_CHART.setLayout(new GridBagLayout());

        // Create constraints for the chart panel
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        // Add the chart panel to the PANEL_PIE_CHART with constraints
        PANEL_PIE_CHART.add(pieChartPanel, gbc);

        // Set the JFrame size and visibility
        pack();
        setVisible(true);

    
    }
    

    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        PAGES = new javax.swing.JPanel();
        MAIN_PANEL = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        CHART_PANEL_REG_SOW = new javax.swing.JPanel();
        PANEL_PIE_CHART = new javax.swing.JPanel();
        PERFORMANCE = new javax.swing.JPanel();
        jScrollPane8 = new javax.swing.JScrollPane();
        PERFORMANCE_WEANING_TABLE = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        PERFORMANCE_FARROWING_TABLE = new javax.swing.JTable();
        PERFORMANCE_SEARCHFIELD = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jScrollPane10 = new javax.swing.JScrollPane();
        PERFORMANCE_BREEDING_TABLE = new javax.swing.JTable();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        WARNING_SOW = new javax.swing.JPanel();
        jScrollPane11 = new javax.swing.JScrollPane();
        WARNING_SOW_LIST_WARNING_SOW = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        WARNING_CULL_BUTTON = new javax.swing.JButton();
        jScrollPane12 = new javax.swing.JScrollPane();
        WARNING_SOW_DETAILS = new javax.swing.JTable();
        WARNING_FORCULLED_LABEL = new javax.swing.JLabel();
        CULLED_SOW = new javax.swing.JPanel();
        jScrollPane13 = new javax.swing.JScrollPane();
        CULLED_MAIN_TABLE = new javax.swing.JTable();
        jLabel7 = new javax.swing.JLabel();
        CULLED_TOTAL_CULLED = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(0, 153, 153));
        jPanel1.setMinimumSize(new java.awt.Dimension(250, 330));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("LIST OF SOW");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 150, 40));

        jButton8.setText("HOME");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 150, 40));

        jButton13.setText("WARNING SOW");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 150, 40));

        jButton14.setText("CULLED SOW");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 150, 40));

        jSplitPane2.setLeftComponent(jPanel1);

        PAGES.setLayout(new java.awt.CardLayout());

        MAIN_PANEL.setBackground(new java.awt.Color(153, 255, 153));
        MAIN_PANEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("RDJ FARM");
        MAIN_PANEL.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(370, 10, 350, 60));

        javax.swing.GroupLayout CHART_PANEL_REG_SOWLayout = new javax.swing.GroupLayout(CHART_PANEL_REG_SOW);
        CHART_PANEL_REG_SOW.setLayout(CHART_PANEL_REG_SOWLayout);
        CHART_PANEL_REG_SOWLayout.setHorizontalGroup(
            CHART_PANEL_REG_SOWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 640, Short.MAX_VALUE)
        );
        CHART_PANEL_REG_SOWLayout.setVerticalGroup(
            CHART_PANEL_REG_SOWLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 440, Short.MAX_VALUE)
        );

        MAIN_PANEL.add(CHART_PANEL_REG_SOW, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 220, 640, 440));

        javax.swing.GroupLayout PANEL_PIE_CHARTLayout = new javax.swing.GroupLayout(PANEL_PIE_CHART);
        PANEL_PIE_CHART.setLayout(PANEL_PIE_CHARTLayout);
        PANEL_PIE_CHARTLayout.setHorizontalGroup(
            PANEL_PIE_CHARTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 360, Short.MAX_VALUE)
        );
        PANEL_PIE_CHARTLayout.setVerticalGroup(
            PANEL_PIE_CHARTLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        MAIN_PANEL.add(PANEL_PIE_CHART, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 60, 360, 300));

        PAGES.add(MAIN_PANEL, "MAIN_PANEL");

        PERFORMANCE.setBackground(new java.awt.Color(204, 204, 204));
        PERFORMANCE.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        PERFORMANCE_WEANING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane8.setViewportView(PERFORMANCE_WEANING_TABLE);

        PERFORMANCE.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 400, 540, 300));

        PERFORMANCE_FARROWING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane9.setViewportView(PERFORMANCE_FARROWING_TABLE);

        PERFORMANCE.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 100, 860, 250));
        PERFORMANCE.add(PERFORMANCE_SEARCHFIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 260, 30));

        jButton12.setText("SEARCH");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        PERFORMANCE.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, -1, 30));

        PERFORMANCE_BREEDING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane10.setViewportView(PERFORMANCE_BREEDING_TABLE);

        PERFORMANCE.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 510, 300));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("FARROWING");
        PERFORMANCE.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 540, 40));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("BREEDING");
        PERFORMANCE.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 510, 40));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("WEANING");
        PERFORMANCE.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 360, 540, 40));

        PAGES.add(PERFORMANCE, "PAGE_5");

        WARNING_SOW.setBackground(new java.awt.Color(0, 153, 153));
        WARNING_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        WARNING_SOW_LIST_WARNING_SOW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        WARNING_SOW_LIST_WARNING_SOW.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                WARNING_SOW_LIST_WARNING_SOWMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(WARNING_SOW_LIST_WARNING_SOW);

        WARNING_SOW.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 80, 130, 620));

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("WARNING SOW");
        WARNING_SOW.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 120, 40));

        WARNING_CULL_BUTTON.setText("CULL");
        WARNING_CULL_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WARNING_CULL_BUTTONActionPerformed(evt);
            }
        });
        WARNING_SOW.add(WARNING_CULL_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 550, 170, 50));

        WARNING_SOW_DETAILS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jScrollPane12.setViewportView(WARNING_SOW_DETAILS);

        WARNING_SOW.add(jScrollPane12, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 80, 840, 620));
        WARNING_SOW.add(WARNING_FORCULLED_LABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 50, 80, 20));

        PAGES.add(WARNING_SOW, "PAGE_6");

        CULLED_SOW.setBackground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setForeground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        CULLED_MAIN_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Title 1", "Title 2"
            }
        ));
        jScrollPane13.setViewportView(CULLED_MAIN_TABLE);

        CULLED_SOW.add(jScrollPane13, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 100, 300, 600));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("LIST OF WARNING SOW");
        CULLED_SOW.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 300, 40));

        CULLED_TOTAL_CULLED.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        CULLED_TOTAL_CULLED.setForeground(new java.awt.Color(255, 255, 0));
        CULLED_TOTAL_CULLED.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CULLED_TOTAL_CULLED.setText("10000");
        CULLED_SOW.add(CULLED_TOTAL_CULLED, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 200, 70));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("TOTAL CULLED SOW");
        CULLED_SOW.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 200, 40));

        PAGES.add(CULLED_SOW, "PAGE_7");

        jSplitPane2.setRightComponent(PAGES);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1365, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane2)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "PAGE_5");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "MAIN_PANEL");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS();
        PERFORMCE_FARROWING_RETRIEVE_DETAILS();
        PERFORMANCE_WEANING_RETRIEVE_DETAILS();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        cardLayout.show(PAGES, "PAGE_6");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void WARNING_SOW_LIST_WARNING_SOWMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_WARNING_SOW_LIST_WARNING_SOWMouseClicked
        // TODO add your handling code here:
        
    }//GEN-LAST:event_WARNING_SOW_LIST_WARNING_SOWMouseClicked

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        cardLayout.show(PAGES, "PAGE_7");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void WARNING_CULL_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WARNING_CULL_BUTTONActionPerformed
        
        String culledValue = WARNING_FORCULLED_LABEL.getText();
        int culledInt = Integer.parseInt(culledValue);
        try {
            
            String checkCulledQuery = "SELECT culled FROM farrowing_records WHERE eartag = ?";
            pst = conn.prepareStatement(checkCulledQuery);
            pst.setInt(1, culledInt);
            rs = pst.executeQuery();

            if (rs.next() && rs.getBoolean("culled")) {
                JOptionPane.showMessageDialog(null, "Sow with eartag number " + culledValue +  " is already culled.");
                CULLED_FETCH_EARTAG();
                return;
            }
            
            String breedingUpdateQuery = "UPDATE breeding SET culled = ? WHERE eartag = ?";
            PreparedStatement breedingUpdateStmt = conn.prepareStatement(breedingUpdateQuery);
            breedingUpdateStmt.setBoolean(1, true);
            breedingUpdateStmt.setInt(2, culledInt); 
            breedingUpdateStmt.executeUpdate();



            String farrowingUpdateQuery = "UPDATE farrowing_records SET culled = ? WHERE eartag = ?";
            PreparedStatement farrowingUpdateStmt = conn.prepareStatement(farrowingUpdateQuery);
            farrowingUpdateStmt.setBoolean(1, true);
            farrowingUpdateStmt.setInt(2, culledInt); 
            farrowingUpdateStmt.executeUpdate();
            
            
            JOptionPane.showMessageDialog(null, "Culling status for sow with eartag number " + culledValue +  " has been updated to culled.");

            CULLED_FETCH_EARTAG();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        
    }//GEN-LAST:event_WARNING_CULL_BUTTONActionPerformed

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
            java.util.logging.Logger.getLogger(SECRETARY.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SECRETARY.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SECRETARY.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SECRETARY.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SECRETARY().setVisible(true);
            }
        });
        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel CHART_PANEL_REG_SOW;
    private javax.swing.JTable CULLED_MAIN_TABLE;
    private javax.swing.JPanel CULLED_SOW;
    private javax.swing.JLabel CULLED_TOTAL_CULLED;
    private javax.swing.JPanel MAIN_PANEL;
    private javax.swing.JPanel PAGES;
    private javax.swing.JPanel PANEL_PIE_CHART;
    private javax.swing.JPanel PERFORMANCE;
    private javax.swing.JTable PERFORMANCE_BREEDING_TABLE;
    private javax.swing.JTable PERFORMANCE_FARROWING_TABLE;
    private javax.swing.JTextField PERFORMANCE_SEARCHFIELD;
    private javax.swing.JTable PERFORMANCE_WEANING_TABLE;
    private javax.swing.JButton WARNING_CULL_BUTTON;
    private javax.swing.JLabel WARNING_FORCULLED_LABEL;
    private javax.swing.JPanel WARNING_SOW;
    private javax.swing.JTable WARNING_SOW_DETAILS;
    private javax.swing.JTable WARNING_SOW_LIST_WARNING_SOW;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane12;
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane2;
    // End of variables declaration//GEN-END:variables


    
    private void PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS(){
        
        try{
            DefaultTableModel model = new DefaultTableModel();

            
            String query = "SELECT boar_used, breeding_date, expected_farrowing, comments, farrowed FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();
            
            model.addColumn("boar_used");
            model.addColumn("breeding_date");
            model.addColumn("expected_farrowing");
            model.addColumn("comments");
            model.addColumn("status");


            
            while (rs.next()) {
                Date breeding_date = rs.getDate("breeding_date");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                String comments = rs.getString("comments");
                int isFarrowed = rs.getInt("farrowed");
                String status = isFarrowed == 1 ? "farrowed" : "not farrowed";

            model.addRow(new Object[]{boar_used, breeding_date, expected_farrowing, comments, status});
        }

             if(PERFORMANCE_BREEDING_TABLE != null){
                PERFORMANCE_BREEDING_TABLE.setModel(model);
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    private void PERFORMCE_FARROWING_RETRIEVE_DETAILS(){

        try{
            DefaultTableModel model = new DefaultTableModel();


            String query = "SELECT farrowing_actualdate, farrowing_duedate, female_piglets, male_piglets, total_piglets, abw, mortality, remarks FROM farrowing_records WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();


            model.addColumn("farrowing_actualdate");
            model.addColumn("farrowing_duedate");
            model.addColumn("female_piglets");
            model.addColumn("male_piglets");
            model.addColumn("total_piglets");
            model.addColumn("abw");
            model.addColumn("mortality");
            model.addColumn("remarks");



            while (rs.next()) {
                Date farrowing_actualdate = rs.getDate("farrowing_actualdate");
                Date farrowing_duedate = rs.getDate("farrowing_duedate");
                int female_piglets = rs.getInt("female_piglets");
                int male_piglets = rs.getInt("male_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double abw = rs.getDouble("abw");
                int mortality = rs.getInt("mortality");
                String remarks = rs.getString("remarks");
    



                model.addRow(new Object[]{farrowing_actualdate, farrowing_duedate, female_piglets, male_piglets, total_piglets, abw, mortality, remarks});
            }

             if(PERFORMANCE_FARROWING_TABLE != null){
                PERFORMANCE_FARROWING_TABLE.setModel(model);
            }
        } catch (Exception e){
            JOptionPane.showMessageDialog(null,e);
        }
    }
    
    private void PERFORMANCE_WEANING_RETRIEVE_DETAILS(){

        try {
            DefaultTableModel model = new DefaultTableModel();
            String query = "SELECT weaning_actualdate, male_piglets, female_piglets, total_piglets, aw FROM weaning_records  WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();
            
            
            if (rs.next()) {
//                int eartag = rs.getInt("eartag");
                Date weaning_actualdate = rs.getDate("weaning_actualdate");
                int male_piglets = rs.getInt("male_piglets");
                int female_piglets = rs.getInt("female_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");
                
//                model.addColumn("eartag");
                model.addColumn("weaning_actualdate");
                model.addColumn("male_piglets");
                model.addColumn("female_piglets");
                model.addColumn("total_piglets");
                model.addColumn("aw");
                
                model.addRow(new Object[]{weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
            }
            
            PERFORMANCE_WEANING_TABLE.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }
    

     private void WARNING_FETCH_EARTAG(){
        
        try{
            DefaultTableModel model = new DefaultTableModel();
    
            String warningSowsQuery = "SELECT DISTINCT eartag " +
                "FROM farrowing_records " +
                "WHERE (female_piglets + male_piglets) < 7 " +
                "OR mortality > 0 " +
                "OR remarks IS NOT NULL " +
                "OR (farrowing_actualdate BETWEEN farrowing_duedate - INTERVAL 3 DAY AND farrowing_duedate + INTERVAL 3 DAY)";
     
            pst = conn.prepareStatement(warningSowsQuery);
            rs = pst.executeQuery();
            
            model.addColumn("Eartag");


            
            while (rs.next()) {
                int eartag = rs.getInt("eartag");

            model.addRow(new Object[]{eartag});
        }

             if(WARNING_SOW_LIST_WARNING_SOW != null){
                WARNING_SOW_LIST_WARNING_SOW.setModel(model);
            }
             WARNING_CULL_BUTTON.setVisible(false);
            
        }  catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
    }
     
    private void CULLED_FETCH_EARTAG(){
         
        try{
            DefaultTableModel model = new DefaultTableModel();
            
            String cullQuery = "SELECT DISTINCT eartag FROM farrowing_records WHERE culled = true";


            pst = conn.prepareStatement(cullQuery);
            rs = pst.executeQuery();
            
            int counterForCull = 0;
            
            model.addColumn("Eartag");

            while (rs.next()) {
                counterForCull++;
                int eartag = rs.getInt("eartag");

            model.addRow(new Object[]{eartag});
        }
            
            CULLED_TOTAL_CULLED.setText(String.valueOf(counterForCull));

             if(CULLED_MAIN_TABLE != null){
                CULLED_MAIN_TABLE.setModel(model);
            }
            
        }  catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }
    }
      

}