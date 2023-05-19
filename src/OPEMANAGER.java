
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
        CULLED_FETCH_EARTAG();

        cardLayout = (CardLayout) (PAGES.getLayout());

        CategoryChart chartForRegSOw = new CategoryChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).build();

        chartForRegSOw.setTitle("Registered Sows");
        chartForRegSOw.setXAxisTitle("Date");
        chartForRegSOw.setYAxisTitle("Number of Sows");

        try {
            String query = "SELECT date, COUNT(eartag) AS count FROM register_sow GROUP BY date";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            List<String> xValues = new ArrayList<>();
            List<Integer> yValues = new ArrayList<>();

            while (rs.next()) {
                Date date = rs.getDate("date");
                int count = rs.getInt("count");

                xValues.add(date.toString()); // Convert date to string for category chart
                yValues.add(count);
            }

            chartForRegSOw.addSeries("Number of Sows", xValues, yValues);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        chartPanel = new XChartPanel<>(chartForRegSOw);
        CHART_PANEL_REG_SOW.setLayout(new BorderLayout());
        CHART_PANEL_REG_SOW.add(chartPanel, BorderLayout.CENTER);

        pack();
        setVisible(true);

        PieChart chartForPie = new PieChartBuilder().width(800).height(600).theme(ChartTheme.Matlab).build();

        chartForPie.setTitle("Farrowing Records");

        try {
            String culledQuery = "SELECT COUNT(*) AS culledCount FROM farrowing_records WHERE culled = 1";
            pst = conn.prepareStatement(culledQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                int culledCount = rs.getInt("culledCount");
                chartForPie.addSeries("Culled (" + culledCount + ")", culledCount);
            }

            String farrowedQuery = "SELECT COUNT(*) AS farrowedCount FROM farrowing_records WHERE culled = 0";
            pst = conn.prepareStatement(farrowedQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                int farrowedCount = rs.getInt("farrowedCount");
                chartForPie.addSeries("Farrowed (" + farrowedCount + ")", farrowedCount);
            }

            String warningQuery = "SELECT COUNT(DISTINCT eartag) AS warningCount "
                    + "FROM farrowing_records "
                    + "WHERE ((female_piglets + male_piglets) < 7 "
                    + "OR mortality > 0 "
                    + "OR remarks IS NOT NULL "
                    + "OR (farrowing_actualdate BETWEEN farrowing_duedate - INTERVAL 3 DAY AND farrowing_duedate + INTERVAL 3 DAY)) "
                    + "AND culled = 0";
            pst = conn.prepareStatement(warningQuery);
            rs = pst.executeQuery();
            if (rs.next()) {
                int warningCount = rs.getInt("warningCount");
                chartForPie.addSeries("Warning (" + warningCount + ")", warningCount);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        pieChartPanel = new XChartPanel<>(chartForPie);

        PANEL_PIE_CHART.setLayout(new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;

        PANEL_PIE_CHART.add(pieChartPanel, gbc);

        pack();
        setVisible(true);

        WARNING_SOW_LIST_WARNING_SOW.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();
                int eartag = Integer.parseInt(table.getValueAt(row, 0).toString());
                try {
                    DefaultTableModel model = new DefaultTableModel();
                    String warningSowsDetailsQuery = "SELECT * FROM farrowing_records WHERE eartag = ?";
                    pst = conn.prepareStatement(warningSowsDetailsQuery);
                    pst.setInt(1, eartag);
                    rs = pst.executeQuery();
                    model.addColumn("Female");
                    model.addColumn("Male");
                    model.addColumn("Total");
                    model.addColumn("Mortality");
                    model.addColumn("Remarks");
                    model.addColumn("Due");
                    model.addColumn("Actual");

                    while (rs.next()) {

                        int femalePiglets = rs.getInt("female_piglets");
                        int malePiglets = rs.getInt("male_piglets");
                        int totalPiglets = rs.getInt("total_piglets");
                        int mortality = rs.getInt("mortality");
                        String remarks = rs.getString("remarks");
                        Date farrowingDueDate = rs.getDate("farrowing_duedate");
                        Date farrowingActualDate = rs.getDate("farrowing_actualdate");
                        model.addRow(new Object[]{femalePiglets, malePiglets, totalPiglets, mortality, remarks, farrowingDueDate, farrowingActualDate});
                    }
                    if (WARNING_SOW_DETAILS != null) {
                        WARNING_SOW_DETAILS.setModel(model);
                        WARNING_CULL_BUTTON.setVisible(true);
                        WARNING_FORCULLED_LABEL.setText(String.valueOf(eartag));
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, ex);
                }
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
        LIST_OF_SOW = new javax.swing.JPanel();
        PERFORMANCE_SEARCHFIELD = new javax.swing.JTextField();
        jButton15 = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        PERFORMANCE_FARROWING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jScrollPane7 = new javax.swing.JScrollPane();
        PERFORMANCE_WEANING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jScrollPane14 = new javax.swing.JScrollPane();
        PERFORMANCE_BREEDING_TABLE = new rojeru_san.complementos.RSTableMetro();
        WARNING_SOW = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        WARNING_CULL_BUTTON = new javax.swing.JButton();
        WARNING_FORCULLED_LABEL = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        WARNING_SOW_DETAILS = new rojeru_san.complementos.RSTableMetro();
        jScrollPane10 = new javax.swing.JScrollPane();
        WARNING_SOW_LIST_WARNING_SOW = new rojeru_san.complementos.RSTableMetro();
        CULLED_SOW = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        CULLED_TOTAL_CULLED = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        CULLED_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();

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

        LIST_OF_SOW.setBackground(new java.awt.Color(204, 204, 204));
        LIST_OF_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        LIST_OF_SOW.add(PERFORMANCE_SEARCHFIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 260, 30));

        jButton15.setText("SEARCH");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        LIST_OF_SOW.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, -1, 30));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("FARROWING");
        LIST_OF_SOW.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 540, 40));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("BREEDING");
        LIST_OF_SOW.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 510, 40));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("WEANING");
        LIST_OF_SOW.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 540, 40));

        PERFORMANCE_FARROWING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Actual", "Due", "Female", "Male", "Total", "ABW", "Mortality", "Remarks"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PERFORMANCE_FARROWING_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        PERFORMANCE_FARROWING_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        PERFORMANCE_FARROWING_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jScrollPane6.setViewportView(PERFORMANCE_FARROWING_TABLE);

        LIST_OF_SOW.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 1050, 220));

        PERFORMANCE_WEANING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Eartag", "Actual", "Male", "Female", "Total", "AW"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PERFORMANCE_WEANING_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        PERFORMANCE_WEANING_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        PERFORMANCE_WEANING_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane7.setViewportView(PERFORMANCE_WEANING_TABLE);

        LIST_OF_SOW.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, 520, 300));

        PERFORMANCE_BREEDING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Boar Used", "Breeding Date", "Expected", "Comment", "Status"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        PERFORMANCE_BREEDING_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        PERFORMANCE_BREEDING_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        PERFORMANCE_BREEDING_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane14.setViewportView(PERFORMANCE_BREEDING_TABLE);

        LIST_OF_SOW.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 510, 300));

        PAGES.add(LIST_OF_SOW, "PAGE_5");

        WARNING_SOW.setBackground(new java.awt.Color(0, 153, 153));
        WARNING_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("WARNING SOW");
        WARNING_SOW.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 120, 40));

        WARNING_CULL_BUTTON.setText("CULL");
        WARNING_CULL_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WARNING_CULL_BUTTONActionPerformed(evt);
            }
        });
        WARNING_SOW.add(WARNING_CULL_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 550, 170, 50));
        WARNING_SOW.add(WARNING_FORCULLED_LABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 50, 80, 20));

        WARNING_SOW_DETAILS.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Female", "Male", "Total", "Mortality", "Remarks", "Due", "Actual"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, true, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        WARNING_SOW_DETAILS.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        WARNING_SOW_DETAILS.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        WARNING_SOW_DETAILS.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane9.setViewportView(WARNING_SOW_DETAILS);

        WARNING_SOW.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 70, 850, 620));

        WARNING_SOW_LIST_WARNING_SOW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Eartag"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        jScrollPane10.setViewportView(WARNING_SOW_LIST_WARNING_SOW);

        WARNING_SOW.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 140, 620));

        PAGES.add(WARNING_SOW, "PAGE_6");

        CULLED_SOW.setBackground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setForeground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("LIST OF CULLED SOW");
        CULLED_SOW.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 300, 40));

        CULLED_TOTAL_CULLED.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        CULLED_TOTAL_CULLED.setForeground(new java.awt.Color(255, 255, 0));
        CULLED_TOTAL_CULLED.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CULLED_TOTAL_CULLED.setText("10000");
        CULLED_SOW.add(CULLED_TOTAL_CULLED, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 200, 70));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("TOTAL CULLED SOW");
        CULLED_SOW.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 170, 200, 40));

        CULLED_MAIN_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null},
                {null},
                {null},
                {null}
            },
            new String [] {
                "Culled"
            }
        ));
        jScrollPane11.setViewportView(CULLED_MAIN_TABLE);

        CULLED_SOW.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 180, 580));

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

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        cardLayout.show(PAGES, "PAGE_6");
    }//GEN-LAST:event_jButton13ActionPerformed

    private void jButton14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton14ActionPerformed
        cardLayout.show(PAGES, "PAGE_7");
    }//GEN-LAST:event_jButton14ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        // TODO add your handling code here:
        PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS();
        PERFORMCE_FARROWING_RETRIEVE_DETAILS();
        PERFORMANCE_WEANING_RETRIEVE_DETAILS();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void WARNING_CULL_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WARNING_CULL_BUTTONActionPerformed

        String culledValue = WARNING_FORCULLED_LABEL.getText();
        int culledInt = Integer.parseInt(culledValue);
        try {

            String checkCulledQuery = "SELECT culled FROM farrowing_records WHERE eartag = ?";
            pst = conn.prepareStatement(checkCulledQuery);
            pst.setInt(1, culledInt);
            rs = pst.executeQuery();

            if (rs.next() && rs.getBoolean("culled")) {
                JOptionPane.showMessageDialog(null, "Sow with eartag number " + culledValue + " is already culled.");
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

            JOptionPane.showMessageDialog(null, "Culling status for sow with eartag number " + culledValue + " has been updated to culled.");

            WARNING_FETCH_EARTAG();
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
    private rojeru_san.complementos.RSTableMetro CULLED_MAIN_TABLE;
    private javax.swing.JPanel CULLED_SOW;
    private javax.swing.JLabel CULLED_TOTAL_CULLED;
    private javax.swing.JPanel LIST_OF_SOW;
    private javax.swing.JPanel MAIN_PANEL;
    private javax.swing.JPanel PAGES;
    private javax.swing.JPanel PANEL_PIE_CHART;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_BREEDING_TABLE;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_FARROWING_TABLE;
    private javax.swing.JTextField PERFORMANCE_SEARCHFIELD;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_WEANING_TABLE;
    private javax.swing.JButton WARNING_CULL_BUTTON;
    private javax.swing.JLabel WARNING_FORCULLED_LABEL;
    private javax.swing.JPanel WARNING_SOW;
    private rojeru_san.complementos.RSTableMetro WARNING_SOW_DETAILS;
    private rojeru_san.complementos.RSTableMetro WARNING_SOW_LIST_WARNING_SOW;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane2;
    // End of variables declaration//GEN-END:variables

    private void PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT boar_used, breeding_date, expected_farrowing, comments, farrowed FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();

            model.addColumn("Boar");
            model.addColumn("Date");
            model.addColumn("Expected");
            model.addColumn("Comments");
            model.addColumn("Status");

            while (rs.next()) {
                Date breeding_date = rs.getDate("breeding_date");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                String comments = rs.getString("comments");
                int isFarrowed = rs.getInt("farrowed");
                String status = isFarrowed == 1 ? "farrowed" : "not farrowed";

                model.addRow(new Object[]{boar_used, breeding_date, expected_farrowing, comments, status});
            }

            if (PERFORMANCE_BREEDING_TABLE != null) {
                PERFORMANCE_BREEDING_TABLE.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void PERFORMCE_FARROWING_RETRIEVE_DETAILS() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT farrowing_actualdate, farrowing_duedate, female_piglets, male_piglets, total_piglets, abw, mortality, remarks FROM farrowing_records WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();

            model.addColumn("Actual");
            model.addColumn("Due");
            model.addColumn("Female");
            model.addColumn("Male");
            model.addColumn("Total");
            model.addColumn("ABW");
            model.addColumn("Mortality");
            model.addColumn("Remarks");

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

            if (PERFORMANCE_FARROWING_TABLE != null) {
                PERFORMANCE_FARROWING_TABLE.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void PERFORMANCE_WEANING_RETRIEVE_DETAILS() {

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
                model.addColumn("Actual");
                model.addColumn("Male");
                model.addColumn("Female");
                model.addColumn("Total");
                model.addColumn("AW");

                model.addRow(new Object[]{weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
            }

            PERFORMANCE_WEANING_TABLE.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void WARNING_FETCH_EARTAG() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String warningSowsQuery = "SELECT DISTINCT eartag "
                    + "FROM farrowing_records "
                    + "WHERE (female_piglets + male_piglets) < 7 "
                    + "OR mortality > 0 "
                    + "OR remarks IS NOT NULL "
                    + "OR (farrowing_actualdate BETWEEN farrowing_duedate - INTERVAL 3 DAY AND farrowing_duedate + INTERVAL 3 DAY)";

            pst = conn.prepareStatement(warningSowsQuery);
            rs = pst.executeQuery();

            model.addColumn("Eartag");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");

                model.addRow(new Object[]{eartag});
            }

            if (WARNING_SOW_LIST_WARNING_SOW != null) {
                WARNING_SOW_LIST_WARNING_SOW.setModel(model);
            }
            WARNING_CULL_BUTTON.setVisible(false);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void CULLED_FETCH_EARTAG() {

        try {
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

            if (CULLED_MAIN_TABLE != null) {
                CULLED_MAIN_TABLE.setModel(model);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

}
