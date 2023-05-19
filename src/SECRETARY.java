
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Reydel
 */
public class SECRETARY extends javax.swing.JFrame {

    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    CardLayout cardLayout;

    private final NOTIFICATIONMODAL notificationModal;
    private final REBREEDINGMODAL rebreedingModal;

    int newNotificationCount = 0;

    public SECRETARY() {
        conn = DBConnection.getConnection();
        initComponents();

        notificationModal = new NOTIFICATIONMODAL();
        notificationModal.setVisible(false);

        rebreedingModal = new REBREEDINGMODAL();
        rebreedingModal.setVisible(false);

        FETCH_CURRENT_EARTAG();
        REGISTER_RETRIEVE_REGISTERED_SOW();
//
        BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER();
//        BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER();

        BREEDING_RETRIEVE_BREEDING_DETAILS();
//
//        FARROWING_RETRIEVE_DETAILS();
//        FARROWING_REBREEDING();

        FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();
//
//        WEANING_RETRIEVE_DETAILS();

        WEANING_REBREEDING_BTN.setVisible(false);
//
//        PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS();
//        PERFORMCE_FARROWING_RETRIEVE_DETAILS();
//        PERFORMANCE_WEANING_RETRIEVE_DETAILS();

        UPLOAD_NOTIFICATION();
        NUMBER_OF_NOTIFICATION.setText(String.valueOf(newNotificationCount));

        WARNING_FETCH_EARTAG();

        CULLED_FETCH_EARTAG();

        LIST_OF_SOW_BY_BATCH.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();
                int eartag = Integer.parseInt(table.getValueAt(row, 1).toString());

                BREEDING_EARTAG.setText(Integer.toString(eartag));

            }
        });

//        VISIBILITY
//        BREEDING_EARTAG.setVisible(false);
        FARROWING_DETAILS_CONTAINER.setVisible(false);

        FARROWING_ONGOING_BREEDING.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();

                int eartag = Integer.parseInt(table.getValueAt(row, 0).toString());
                String farrowingDue = table.getValueAt(row, 2).toString();

                FARROWING_EARTAG.setText(Integer.toString(eartag));
                FARROWING_DUE.setText(farrowingDue);
                FARROWING_RETRIEVE_DETAILS();

            }
        });

        LIST_OF_NOT_FARROWED.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();

                int eartag = Integer.parseInt(table.getValueAt(row, 0).toString());
                String farrowingDue = table.getValueAt(row, 1).toString();

                FARROWING_EARTAG.setText(Integer.toString(eartag));
                FARROWING_DUE.setText(farrowingDue);
                FARROWING_SEARCH_FIELD.setText(Integer.toString(eartag));

            }
        });

//          WEANING
//        WEANING_RETRIEVE_DETAILS();
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

//        CULLED 
        CULLED_FETCH_EARTAG();

        cardLayout = (CardLayout) (PAGES.getLayout());
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
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        NUMBER_OF_NOTIFICATION = new javax.swing.JLabel();
        jButton13 = new javax.swing.JButton();
        jButton14 = new javax.swing.JButton();
        jToggleButton1 = new javax.swing.JToggleButton();
        jButton15 = new javax.swing.JButton();
        PAGES = new javax.swing.JPanel();
        MAIN_PANEL = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        REGISTER_OF_SOW = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        REGSOW_TABLE = new rojeru_san.complementos.RSTableMetro();
        jPanel2 = new javax.swing.JPanel();
        REGSOW_DATE = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        REGSOW_BUILDING = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        REGSOW_BNUMBER = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        REGSOW_BUTTON = new javax.swing.JButton();
        ADD_EARTAG = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        EARTAG_CONTAINER = new javax.swing.JPanel();
        LATEST_REGSOW_EARTAG = new javax.swing.JLabel();
        CURRENT_REGSOW_EARTAG = new javax.swing.JLabel();
        REGSOW_ASSIGNED_EMPLOYEE = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        REGSOW_PEN = new javax.swing.JTextField();
        BREEDING = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        EXPECTED_FARROWING_LABEL = new javax.swing.JPanel();
        BREEDING_EXPECTED_FARROWING = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        BREEDING_EARTAG = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BREEDING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jPanel3 = new javax.swing.JPanel();
        BREEDING_DATE = new com.toedter.calendar.JDateChooser();
        BREEDING_BOAR_USED = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        LIST_OF_SOW_BY_BATCH = new rojeru_san.complementos.RSTableMetro();
        DROPDOWN_FOR_BATCH_NUMBER = new javax.swing.JComboBox<>();
        jLabel13 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        BREEDING_COMMENTS = new javax.swing.JTextArea();
        START_BREEDING_BUTTON = new javax.swing.JButton();
        FARROWING = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        FARROWING_ONGOING_BREEDING = new rojeru_san.complementos.RSTableMetro();
        FARROWING_SEARCH_FIELD = new javax.swing.JTextField();
        FARROWING_BUTTON = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        FARROWING_DUE = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        FARROWING_FEMALE = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        FARROWING_MALE = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        FARROWING_TOTAL_PIGLETS = new javax.swing.JLabel();
        jLabel25 = new javax.swing.JLabel();
        FARROWING_ABW = new javax.swing.JTextField();
        FARROWING_MORT = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        FARROWING_REMARKS = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        FARROWING_SUBMIT_BUTTON = new javax.swing.JButton();
        FARROWING_ACTUAL = new com.toedter.calendar.JDateChooser();
        jButton9 = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        FARROWING_EARTAG = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        FARROWING_DETAILS_CONTAINER = new javax.swing.JPanel();
        jScrollPane14 = new javax.swing.JScrollPane();
        FARROWING_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        LIST_OF_NOT_FARROWED = new rojeru_san.complementos.RSTableMetro();
        WEANING = new javax.swing.JPanel();
        WEANING_CALENDAR = new com.toedter.calendar.JDateChooser();
        WEANING_MALE = new javax.swing.JTextField();
        WEANING_FEMALE = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        WEANING_AW = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        jButton7 = new javax.swing.JButton();
        WEANING_SEARCH_FIELD = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jButton11 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        WEANING_EARTAG = new javax.swing.JLabel();
        WEANING_TOTAL = new javax.swing.JLabel();
        WEANING_REBREEDING_BTN = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        WEANING_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();
        PERFORMANCE = new javax.swing.JPanel();
        PERFORMANCE_SEARCHFIELD = new javax.swing.JTextField();
        jButton12 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        PERFORMANCE_FARROWING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jScrollPane7 = new javax.swing.JScrollPane();
        PERFORMANCE_WEANING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jScrollPane8 = new javax.swing.JScrollPane();
        PERFORMANCE_BREEDING_TABLE = new rojeru_san.complementos.RSTableMetro();
        WARNING_SOW = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        WARNING_CULL_BUTTON = new javax.swing.JButton();
        WARNING_FORCULLED_LABEL = new javax.swing.JLabel();
        jScrollPane9 = new javax.swing.JScrollPane();
        WARNING_SOW_DETAILS = new rojeru_san.complementos.RSTableMetro();
        jScrollPane10 = new javax.swing.JScrollPane();
        WARNING_SOW_LIST_WARNING_SOW = new rojeru_san.complementos.RSTableMetro();
        CULLED_SOW = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        CULLED_TOTAL_CULLED = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        CULLED_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(26, 46, 53));
        jPanel1.setMinimumSize(new java.awt.Dimension(250, 330));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton1.setText("PERFORMANCE");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 150, 40));

        jButton2.setText("REGISTER SOW");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 150, 40));

        jButton3.setText("BREEDING");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 150, 40));

        jButton4.setText("FARROWING");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 150, 40));

        jButton5.setText("WEANING");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 150, 40));

        jButton8.setText("HOME");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 150, 40));

        NUMBER_OF_NOTIFICATION.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NUMBER_OF_NOTIFICATION.setForeground(new java.awt.Color(255, 255, 255));
        NUMBER_OF_NOTIFICATION.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        NUMBER_OF_NOTIFICATION.setText("0");
        jPanel1.add(NUMBER_OF_NOTIFICATION, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 650, 30, 40));

        jButton13.setText("WARNING SOW");
        jButton13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton13ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 150, 40));

        jButton14.setText("CULLED SOW");
        jButton14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton14ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton14, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 150, 40));

        jToggleButton1.setText("NOTIFICATION");
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jToggleButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 650, -1, 40));

        jButton15.setText("LOGOUT");
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton15, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 650, -1, 40));

        jSplitPane2.setLeftComponent(jPanel1);

        PAGES.setLayout(new java.awt.CardLayout());

        MAIN_PANEL.setBackground(new java.awt.Color(255, 217, 90));
        MAIN_PANEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("RDJ FARM");
        MAIN_PANEL.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(380, 60, 350, 60));

        PAGES.add(MAIN_PANEL, "MAIN_PANEL");

        REGISTER_OF_SOW.setBackground(new java.awt.Color(255, 217, 90));
        REGISTER_OF_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        REGSOW_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Eartag", "Date", "Batch", "Building", "Employee"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, true, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        REGSOW_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        REGSOW_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        REGSOW_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        REGSOW_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        REGSOW_TABLE.setColorSelBackgound(new java.awt.Color(255, 217, 90));
        REGSOW_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        REGSOW_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jScrollPane16.setViewportView(REGSOW_TABLE);
        if (REGSOW_TABLE.getColumnModel().getColumnCount() > 0) {
            REGSOW_TABLE.getColumnModel().getColumn(0).setResizable(false);
            REGSOW_TABLE.getColumnModel().getColumn(0).setPreferredWidth(20);
            REGSOW_TABLE.getColumnModel().getColumn(1).setResizable(false);
            REGSOW_TABLE.getColumnModel().getColumn(1).setPreferredWidth(30);
            REGSOW_TABLE.getColumnModel().getColumn(2).setResizable(false);
            REGSOW_TABLE.getColumnModel().getColumn(2).setPreferredWidth(20);
            REGSOW_TABLE.getColumnModel().getColumn(3).setResizable(false);
            REGSOW_TABLE.getColumnModel().getColumn(3).setPreferredWidth(20);
            REGSOW_TABLE.getColumnModel().getColumn(4).setResizable(false);
        }

        REGISTER_OF_SOW.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 40, 680, 650));

        jPanel2.setBackground(new java.awt.Color(26, 46, 53));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(REGSOW_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 240, 40));

        jLabel1.setForeground(new java.awt.Color(255, 217, 90));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATE");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 100, 50, -1));

        jLabel4.setForeground(new java.awt.Color(255, 217, 90));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("BUILDING");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 190, 70, -1));

        REGSOW_BUILDING.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel2.add(REGSOW_BUILDING, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, 110, 30));

        jLabel5.setForeground(new java.awt.Color(255, 217, 90));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("PEN");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 190, 80, -1));

        REGSOW_BNUMBER.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(REGSOW_BNUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 360, 240, 40));

        jLabel2.setForeground(new java.awt.Color(255, 217, 90));
        jLabel2.setText("ASSIGNED EMPLOYEE");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 260, -1, -1));

        REGSOW_BUTTON.setText("REGISTER SOW");
        REGSOW_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REGSOW_BUTTONActionPerformed(evt);
            }
        });
        jPanel2.add(REGSOW_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 570, 130, 30));

        ADD_EARTAG.setText("ADD EARTAG");
        ADD_EARTAG.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ADD_EARTAGActionPerformed(evt);
            }
        });
        jPanel2.add(ADD_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 460, -1, 30));

        jLabel12.setForeground(new java.awt.Color(255, 217, 90));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("SUGGESTED EARTAG");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 410, 150, 40));

        EARTAG_CONTAINER.setBackground(new java.awt.Color(153, 153, 153));
        EARTAG_CONTAINER.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LATEST_REGSOW_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LATEST_REGSOW_EARTAG.setForeground(new java.awt.Color(255, 255, 0));
        LATEST_REGSOW_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EARTAG_CONTAINER.add(LATEST_REGSOW_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 180, 40));

        jPanel2.add(EARTAG_CONTAINER, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 500, 180, 40));

        CURRENT_REGSOW_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CURRENT_REGSOW_EARTAG.setForeground(new java.awt.Color(255, 255, 0));
        CURRENT_REGSOW_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CURRENT_REGSOW_EARTAG.setText("5000");
        jPanel2.add(CURRENT_REGSOW_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 410, 60, 40));

        REGSOW_ASSIGNED_EMPLOYEE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel2.add(REGSOW_ASSIGNED_EMPLOYEE, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 280, 240, 40));

        jLabel6.setForeground(new java.awt.Color(255, 217, 90));
        jLabel6.setText("BATCH NUMBER");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 340, -1, -1));
        jPanel2.add(REGSOW_PEN, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 210, 120, 30));

        REGISTER_OF_SOW.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 350, 680));

        PAGES.add(REGISTER_OF_SOW, "PAGE_1");

        BREEDING.setBackground(new java.awt.Color(255, 217, 90));
        BREEDING.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 217, 90));
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("COMMENTS");
        BREEDING.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 550, 220, 20));

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 217, 90));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("EAR TAG");
        BREEDING.add(jLabel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 220, 20));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(255, 217, 90));
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("EXPECTED FARROWING");
        BREEDING.add(jLabel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 480, 220, 20));

        EXPECTED_FARROWING_LABEL.setBackground(new java.awt.Color(153, 153, 153));
        EXPECTED_FARROWING_LABEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BREEDING_EXPECTED_FARROWING.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BREEDING_EXPECTED_FARROWING.setForeground(new java.awt.Color(255, 255, 255));
        BREEDING_EXPECTED_FARROWING.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EXPECTED_FARROWING_LABEL.add(BREEDING_EXPECTED_FARROWING, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 220, 40));

        BREEDING.add(EXPECTED_FARROWING_LABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 500, 220, 40));

        jPanel4.setBackground(new java.awt.Color(153, 153, 153));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BREEDING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        BREEDING_EARTAG.setForeground(new java.awt.Color(255, 255, 255));
        BREEDING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel4.add(BREEDING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 210, 40));

        BREEDING.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 280, 220, 40));

        BREEDING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "Eartag", "Boar Used", "Breeding Date", "Expected", "Comments", "Status", "Culled"
            }
        ));
        BREEDING_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        BREEDING_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        BREEDING_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        BREEDING_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        BREEDING_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        BREEDING_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        BREEDING_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane1.setViewportView(BREEDING_TABLE);

        BREEDING.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 110, 830, 580));

        jPanel3.setBackground(new java.awt.Color(26, 46, 53));
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        BREEDING_DATE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                BREEDING_DATEPropertyChange(evt);
            }
        });
        jPanel3.add(BREEDING_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 420, 220, 40));
        jPanel3.add(BREEDING_BOAR_USED, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 340, 220, 40));

        LIST_OF_SOW_BY_BATCH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "ID", "Eartag", "Batch"
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

        jPanel3.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 100, 230, 130));

        DROPDOWN_FOR_BATCH_NUMBER.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "SELECT BATCH" }));
        jPanel3.add(DROPDOWN_FOR_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 30, 150, 30));

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 217, 90));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("LIST OF SOW");
        jPanel3.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, 230, 20));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(255, 217, 90));
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("BOAR USED");
        jPanel3.add(jLabel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 320, 220, 20));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(255, 217, 90));
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("BREEDING DATE");
        jPanel3.add(jLabel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 400, 220, 20));

        BREEDING_COMMENTS.setColumns(20);
        BREEDING_COMMENTS.setRows(5);
        jScrollPane4.setViewportView(BREEDING_COMMENTS);

        jPanel3.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 560, -1, -1));

        START_BREEDING_BUTTON.setText("START BREEDING");
        START_BREEDING_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                START_BREEDING_BUTTONActionPerformed(evt);
            }
        });
        jPanel3.add(START_BREEDING_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 660, 170, 30));

        BREEDING.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 250, 710));

        PAGES.add(BREEDING, "PAGE_2");

        FARROWING.setBackground(new java.awt.Color(0, 153, 255));
        FARROWING.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_ONGOING_BREEDING.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Eartag", "Boar Used", "Expected"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        FARROWING_ONGOING_BREEDING.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        FARROWING_ONGOING_BREEDING.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        FARROWING_ONGOING_BREEDING.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane2.setViewportView(FARROWING_ONGOING_BREEDING);
        if (FARROWING_ONGOING_BREEDING.getColumnModel().getColumnCount() > 0) {
            FARROWING_ONGOING_BREEDING.getColumnModel().getColumn(0).setResizable(false);
            FARROWING_ONGOING_BREEDING.getColumnModel().getColumn(1).setResizable(false);
            FARROWING_ONGOING_BREEDING.getColumnModel().getColumn(1).setHeaderValue("Boar Used");
            FARROWING_ONGOING_BREEDING.getColumnModel().getColumn(2).setResizable(false);
        }

        FARROWING.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 400, 120));

        FARROWING_SEARCH_FIELD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FARROWING_SEARCH_FIELDKeyTyped(evt);
            }
        });
        FARROWING.add(FARROWING_SEARCH_FIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 70, 150, 30));

        FARROWING_BUTTON.setText("SEARCH");
        FARROWING_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FARROWING_BUTTONActionPerformed(evt);
            }
        });
        FARROWING.add(FARROWING_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(420, 70, -1, 30));

        jLabel19.setText("LIST OF EARTAG THAT ARE CURRENTLY NOT FARROWED");
        FARROWING.add(jLabel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 90, 360, 30));

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_DUE.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FARROWING_DUE.setForeground(new java.awt.Color(255, 255, 255));
        FARROWING_DUE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(FARROWING_DUE, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 40));

        FARROWING.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 120, 200, 40));

        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("F");
        FARROWING.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 240, 60, -1));

        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("EAR TAG");
        FARROWING.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 30, 200, -1));

        FARROWING_FEMALE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FARROWING_FEMALE.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                FARROWING_FEMALEInputMethodTextChanged(evt);
            }
        });
        FARROWING_FEMALE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                FARROWING_FEMALEPropertyChange(evt);
            }
        });
        FARROWING.add(FARROWING_FEMALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(100, 260, 60, 30));

        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("ACTUAL DATE");
        FARROWING.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 170, 200, -1));

        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("M");
        FARROWING.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 240, 60, -1));

        FARROWING_MALE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FARROWING_MALE.addInputMethodListener(new java.awt.event.InputMethodListener() {
            public void caretPositionChanged(java.awt.event.InputMethodEvent evt) {
            }
            public void inputMethodTextChanged(java.awt.event.InputMethodEvent evt) {
                FARROWING_MALEInputMethodTextChanged(evt);
            }
        });
        FARROWING_MALE.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FARROWING_MALEActionPerformed(evt);
            }
        });
        FARROWING_MALE.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                FARROWING_MALEPropertyChange(evt);
            }
        });
        FARROWING.add(FARROWING_MALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 260, 60, 30));

        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("TOTAL BORN");
        FARROWING.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 120, -1));

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_TOTAL_PIGLETS.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FARROWING_TOTAL_PIGLETS.setForeground(new java.awt.Color(255, 255, 255));
        FARROWING_TOTAL_PIGLETS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(FARROWING_TOTAL_PIGLETS, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 40));

        FARROWING.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 190, 40));

        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("ABW");
        FARROWING.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 190, -1));

        FARROWING_ABW.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FARROWING.add(FARROWING_ABW, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 400, 190, 40));

        FARROWING_MORT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FARROWING.add(FARROWING_MORT, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 470, 190, 40));

        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("MORT");
        FARROWING.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 450, 190, -1));

        FARROWING_REMARKS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FARROWING.add(FARROWING_REMARKS, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 540, 190, 40));

        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("REMARKS");
        FARROWING.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 520, 190, -1));

        FARROWING_SUBMIT_BUTTON.setText("SUBMIT");
        FARROWING_SUBMIT_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FARROWING_SUBMIT_BUTTONActionPerformed(evt);
            }
        });
        FARROWING.add(FARROWING_SUBMIT_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 600, 190, 40));
        FARROWING.add(FARROWING_ACTUAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 190, 200, 40));

        jButton9.setText("=");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        FARROWING.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 260, 50, 30));

        jPanel7.setBackground(new java.awt.Color(153, 153, 153));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FARROWING_EARTAG.setForeground(new java.awt.Color(255, 255, 255));
        FARROWING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel7.add(FARROWING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 40));

        FARROWING.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 50, 200, 40));

        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("DUE DATE");
        FARROWING.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 200, -1));

        FARROWING_DETAILS_CONTAINER.setBackground(new java.awt.Color(153, 153, 153));
        FARROWING_DETAILS_CONTAINER.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_MAIN_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "test", "test", "test", "test"
            }
        ));
        FARROWING_MAIN_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        FARROWING_MAIN_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        FARROWING_MAIN_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane14.setViewportView(FARROWING_MAIN_TABLE);

        FARROWING_DETAILS_CONTAINER.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 820, 420));

        FARROWING.add(FARROWING_DETAILS_CONTAINER, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 260, 820, 420));

        jLabel38.setText("SEARCH EARTAG IF FARROWED");
        FARROWING.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 40, 270, 30));

        LIST_OF_NOT_FARROWED.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Eartag", "Expected"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        LIST_OF_NOT_FARROWED.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        LIST_OF_NOT_FARROWED.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        LIST_OF_NOT_FARROWED.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane15.setViewportView(LIST_OF_NOT_FARROWED);
        if (LIST_OF_NOT_FARROWED.getColumnModel().getColumnCount() > 0) {
            LIST_OF_NOT_FARROWED.getColumnModel().getColumn(0).setResizable(false);
            LIST_OF_NOT_FARROWED.getColumnModel().getColumn(1).setResizable(false);
        }

        FARROWING.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 120, 400, 120));

        PAGES.add(FARROWING, "PAGE_3");

        WEANING.setBackground(new java.awt.Color(204, 51, 255));
        WEANING.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        WEANING.add(WEANING_CALENDAR, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 210, 40));

        WEANING_MALE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        WEANING.add(WEANING_MALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, 70, 40));

        WEANING_FEMALE.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        WEANING.add(WEANING_FEMALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 270, 70, 40));

        jButton6.setText("=");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        WEANING.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(220, 280, 40, -1));

        WEANING_AW.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        WEANING.add(WEANING_AW, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 470, 210, 50));

        jLabel28.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel28.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel28.setText("EARTAG");
        WEANING.add(jLabel28, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 80, 80, 30));

        jLabel29.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel29.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel29.setText("AVERAGE WEIGHT");
        WEANING.add(jLabel29, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 210, 30));

        jLabel30.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel30.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel30.setText("TOTAL");
        WEANING.add(jLabel30, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 330, 210, 30));

        jLabel31.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel31.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel31.setText("M");
        WEANING.add(jLabel31, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 240, 50, 30));

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("CHECK EARTAG IF IT IS ON WEANING PERIOD");
        WEANING.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 40, 270, 30));

        jButton7.setText("SUBMIT");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });
        WEANING.add(jButton7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 550, 160, 40));
        WEANING.add(WEANING_SEARCH_FIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 70, 250, 40));

        jLabel33.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel33.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel33.setText("F");
        WEANING.add(jLabel33, new org.netbeans.lib.awtextra.AbsoluteConstraints(150, 240, 50, 30));

        jButton11.setText("CHECK");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });
        WEANING.add(jButton11, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 73, -1, 30));

        jLabel34.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel34.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel34.setText("DATE");
        WEANING.add(jLabel34, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 160, 50, 30));

        WEANING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        WEANING_EARTAG.setForeground(new java.awt.Color(255, 255, 0));
        WEANING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        WEANING_EARTAG.setText("EARTAG HERE");
        WEANING.add(WEANING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 210, 50));

        WEANING_TOTAL.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        WEANING_TOTAL.setForeground(new java.awt.Color(255, 255, 0));
        WEANING_TOTAL.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        WEANING.add(WEANING_TOTAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 360, 210, 50));

        WEANING_REBREEDING_BTN.setText("START REBREEDING");
        WEANING_REBREEDING_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WEANING_REBREEDING_BTNActionPerformed(evt);
            }
        });
        WEANING.add(WEANING_REBREEDING_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 560, 160, 40));

        WEANING_MAIN_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Eartag", "Actual", "Male", "Female", "Total", "AW"
            }
        ));
        WEANING_MAIN_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        WEANING_MAIN_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        WEANING_MAIN_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jScrollPane5.setViewportView(WEANING_MAIN_TABLE);

        WEANING.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 140, 760, 540));

        PAGES.add(WEANING, "PAGE_4");

        PERFORMANCE.setBackground(new java.awt.Color(204, 204, 204));
        PERFORMANCE.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        PERFORMANCE.add(PERFORMANCE_SEARCHFIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 20, 260, 30));

        jButton12.setText("SEARCH");
        jButton12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton12ActionPerformed(evt);
            }
        });
        PERFORMANCE.add(jButton12, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 20, -1, 30));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel35.setText("FARROWING");
        PERFORMANCE.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(280, 60, 540, 40));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("BREEDING");
        PERFORMANCE.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 510, 40));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("WEANING");
        PERFORMANCE.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 540, 40));

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
        if (PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumnCount() > 0) {
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(0).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(1).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(2).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(3).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(4).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(5).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(6).setResizable(false);
            PERFORMANCE_FARROWING_TABLE.getColumnModel().getColumn(7).setResizable(false);
        }

        PERFORMANCE.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 1050, 220));

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
        if (PERFORMANCE_WEANING_TABLE.getColumnModel().getColumnCount() > 0) {
            PERFORMANCE_WEANING_TABLE.getColumnModel().getColumn(0).setResizable(false);
            PERFORMANCE_WEANING_TABLE.getColumnModel().getColumn(1).setResizable(false);
            PERFORMANCE_WEANING_TABLE.getColumnModel().getColumn(2).setResizable(false);
            PERFORMANCE_WEANING_TABLE.getColumnModel().getColumn(3).setResizable(false);
            PERFORMANCE_WEANING_TABLE.getColumnModel().getColumn(5).setResizable(false);
        }

        PERFORMANCE.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, 520, 300));

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
        jScrollPane8.setViewportView(PERFORMANCE_BREEDING_TABLE);
        if (PERFORMANCE_BREEDING_TABLE.getColumnModel().getColumnCount() > 0) {
            PERFORMANCE_BREEDING_TABLE.getColumnModel().getColumn(0).setResizable(false);
            PERFORMANCE_BREEDING_TABLE.getColumnModel().getColumn(1).setResizable(false);
            PERFORMANCE_BREEDING_TABLE.getColumnModel().getColumn(2).setResizable(false);
            PERFORMANCE_BREEDING_TABLE.getColumnModel().getColumn(3).setResizable(false);
            PERFORMANCE_BREEDING_TABLE.getColumnModel().getColumn(4).setResizable(false);
        }

        PERFORMANCE.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 510, 300));

        PAGES.add(PERFORMANCE, "PAGE_5");

        WARNING_SOW.setBackground(new java.awt.Color(0, 153, 153));
        WARNING_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        if (WARNING_SOW_DETAILS.getColumnModel().getColumnCount() > 0) {
            WARNING_SOW_DETAILS.getColumnModel().getColumn(0).setResizable(false);
            WARNING_SOW_DETAILS.getColumnModel().getColumn(1).setResizable(false);
            WARNING_SOW_DETAILS.getColumnModel().getColumn(2).setResizable(false);
            WARNING_SOW_DETAILS.getColumnModel().getColumn(3).setResizable(false);
            WARNING_SOW_DETAILS.getColumnModel().getColumn(4).setResizable(false);
            WARNING_SOW_DETAILS.getColumnModel().getColumn(5).setResizable(false);
            WARNING_SOW_DETAILS.getColumnModel().getColumn(6).setResizable(false);
        }

        WARNING_SOW.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 80, 850, 620));

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
        if (WARNING_SOW_LIST_WARNING_SOW.getColumnModel().getColumnCount() > 0) {
            WARNING_SOW_LIST_WARNING_SOW.getColumnModel().getColumn(0).setResizable(false);
        }

        WARNING_SOW.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 140, 620));

        PAGES.add(WARNING_SOW, "PAGE_6");

        CULLED_SOW.setBackground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setForeground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("LIST OF CULLED SOW");
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
                .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 729, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed

        // TODO add your handling code here:
        cardLayout.show(PAGES, "PAGE_1");
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "PAGE_2");

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "PAGE_3");

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "PAGE_4");

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "PAGE_5");

    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        cardLayout.show(PAGES, "MAIN_PANEL");
    }//GEN-LAST:event_jButton8ActionPerformed

    private void REGSOW_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REGSOW_BUTTONActionPerformed
        // TODO add your handling code here:

        String pen = REGSOW_PEN.getText();
        String employee = REGSOW_ASSIGNED_EMPLOYEE.getText();
        String bNumber = REGSOW_BNUMBER.getText();
        if (pen.isEmpty() || !pen.matches("\\d+")
                || employee.isEmpty() || !employee.matches("[a-zA-Z]+")
                || bNumber.isEmpty() || !bNumber.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty and have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            SOW_REGISTRATION();
            REGISTER_RETRIEVE_REGISTERED_SOW();

            REGSOW_DATE.setDate(null);
            REGSOW_BNUMBER.setText("");
            LATEST_REGSOW_EARTAG.setText("");
            REGSOW_BUILDING.setSelectedIndex(0);
            REGSOW_ASSIGNED_EMPLOYEE.setText("");
            REGSOW_PEN.setText("");

            FETCH_CURRENT_EARTAG();
        }


    }//GEN-LAST:event_REGSOW_BUTTONActionPerformed

    private void ADD_EARTAGActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ADD_EARTAGActionPerformed
        // TODO add your handling code here:
        String addEartag = CURRENT_REGSOW_EARTAG.getText();
        LATEST_REGSOW_EARTAG.setText(addEartag);
    }//GEN-LAST:event_ADD_EARTAGActionPerformed

    private void START_BREEDING_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_START_BREEDING_BUTTONActionPerformed

        String boarUsed = BREEDING_BOAR_USED.getText();
        String comments = BREEDING_COMMENTS.getText();

        if (boarUsed.isEmpty() || !boarUsed.matches("\\d+")
                || !comments.matches("[a-zA-Z0-9]*") || comments.isEmpty()
                || BREEDING_DATE.getDate() == null || DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty and have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            BREEDING_START_BREEDING();
            BREEDING_RETRIEVE_BREEDING_DETAILS();
            UPLOAD_NOTIFICATION();
            FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();
        }


    }//GEN-LAST:event_START_BREEDING_BUTTONActionPerformed

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
                cal.add(Calendar.DAY_OF_MONTH, 1);
//                cal.add(Calendar.MINUTE, 5);
                Date expectedFarrowingDate = cal.getTime();
                String expectedFarrowing = new java.sql.Date(expectedFarrowingDate.getTime()).toString();

                BREEDING_EXPECTED_FARROWING.setText(expectedFarrowing);
            }

            System.out.println("Reydel");

        }
    }//GEN-LAST:event_BREEDING_DATEPropertyChange

    private void FARROWING_MALEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FARROWING_MALEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FARROWING_MALEActionPerformed

    private void FARROWING_SUBMIT_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FARROWING_SUBMIT_BUTTONActionPerformed

        if (FARROWING_ACTUAL.getDate() == null || FARROWING_ABW.getText().isEmpty()
                || FARROWING_MORT.getText().isEmpty() || !FARROWING_MORT.getText().matches("\\d+")
                || Double.parseDouble(FARROWING_ABW.getText()) <= 0 || !FARROWING_ABW.getText().matches("[0-9]+\\.?[0-9]*")) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty and have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            FARROWING_SUBMIT();
            FARROWING_RETRIEVE_DETAILS();
            BREEDING_RETRIEVE_BREEDING_DETAILS();
            FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();

            PERFORMCE_FARROWING_RETRIEVE_DETAILS();

            WARNING_FETCH_EARTAG();
            CULLED_FETCH_EARTAG();

            UPLOAD_NOTIFICATION();
        }


    }//GEN-LAST:event_FARROWING_SUBMIT_BUTTONActionPerformed

    private void FARROWING_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FARROWING_BUTTONActionPerformed
        // TODO add your handling code here:
        FARROWING_SEARCH_EARTAG();

        FARROWING_RETRIEVE_DETAILS();
        FARROWING_DETAILS_CONTAINER.setVisible(true);
    }//GEN-LAST:event_FARROWING_BUTTONActionPerformed

    private void FARROWING_MALEPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_FARROWING_MALEPropertyChange

    }//GEN-LAST:event_FARROWING_MALEPropertyChange

    private void FARROWING_FEMALEPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_FARROWING_FEMALEPropertyChange

    }//GEN-LAST:event_FARROWING_FEMALEPropertyChange

    private void FARROWING_MALEInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_FARROWING_MALEInputMethodTextChanged

    }//GEN-LAST:event_FARROWING_MALEInputMethodTextChanged

    private void FARROWING_FEMALEInputMethodTextChanged(java.awt.event.InputMethodEvent evt) {//GEN-FIRST:event_FARROWING_FEMALEInputMethodTextChanged

    }//GEN-LAST:event_FARROWING_FEMALEInputMethodTextChanged

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed

        if (FARROWING_MALE.getText().isEmpty() || !FARROWING_MALE.getText().matches("\\d+")
                || FARROWING_FEMALE.getText().isEmpty() || !FARROWING_FEMALE.getText().matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty and have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            int maleCount = FARROWING_MALE.getText().isEmpty() ? 0 : Integer.parseInt(FARROWING_MALE.getText());
            int femaleCount = FARROWING_FEMALE.getText().isEmpty() ? 0 : Integer.parseInt(FARROWING_FEMALE.getText());
            int total = maleCount + femaleCount;
            FARROWING_TOTAL_PIGLETS.setText(Integer.toString(total));
        }


    }//GEN-LAST:event_jButton9ActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
        // TODO add your handling code here:
        WEANING_SEARCH_EARTAG();
        WEANING_RETRIEVE_DETAILS();
        WEANING_REBREEDING_BTN.setVisible(true);
    }//GEN-LAST:event_jButton11ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        if (WEANING_TOTAL.getText().isEmpty() || WEANING_AW.getText().isEmpty() || !WEANING_AW.getText().matches("\\d+(\\.\\d+)?")
                || WEANING_CALENDAR.getDate() == null) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure all fields are not empty and have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            WEANING_SUBMIT();
            WEANING_RETRIEVE_DETAILS();

            PERFORMANCE_WEANING_RETRIEVE_DETAILS();

        }


    }//GEN-LAST:event_jButton7ActionPerformed

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

    private void jButton12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton12ActionPerformed
        // TODO add your handling code here:
        PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS();
        PERFORMCE_FARROWING_RETRIEVE_DETAILS();
        PERFORMANCE_WEANING_RETRIEVE_DETAILS();
    }//GEN-LAST:event_jButton12ActionPerformed

    private void jButton13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton13ActionPerformed

        cardLayout.show(PAGES, "PAGE_6");
    }//GEN-LAST:event_jButton13ActionPerformed

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

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        notificationModal.setVisible(!notificationModal.isVisible());
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        LOGIN n = new LOGIN();

        n.setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton15ActionPerformed

    private void FARROWING_SEARCH_FIELDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FARROWING_SEARCH_FIELDKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_FARROWING_SEARCH_FIELDKeyTyped

    private void WEANING_REBREEDING_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WEANING_REBREEDING_BTNActionPerformed

        String eartag = WEANING_MAIN_TABLE.getValueAt(0, 0).toString();
        rebreedingModal.setEartag(eartag);
        rebreedingModal.setVisible(true);
    }//GEN-LAST:event_WEANING_REBREEDING_BTNActionPerformed

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
    private javax.swing.JButton ADD_EARTAG;
    private javax.swing.JPanel BREEDING;
    private javax.swing.JTextField BREEDING_BOAR_USED;
    private javax.swing.JTextArea BREEDING_COMMENTS;
    private com.toedter.calendar.JDateChooser BREEDING_DATE;
    private javax.swing.JLabel BREEDING_EARTAG;
    private javax.swing.JLabel BREEDING_EXPECTED_FARROWING;
    private rojeru_san.complementos.RSTableMetro BREEDING_TABLE;
    private rojeru_san.complementos.RSTableMetro CULLED_MAIN_TABLE;
    private javax.swing.JPanel CULLED_SOW;
    private javax.swing.JLabel CULLED_TOTAL_CULLED;
    private javax.swing.JLabel CURRENT_REGSOW_EARTAG;
    private javax.swing.JComboBox<String> DROPDOWN_FOR_BATCH_NUMBER;
    private javax.swing.JPanel EARTAG_CONTAINER;
    private javax.swing.JPanel EXPECTED_FARROWING_LABEL;
    private javax.swing.JPanel FARROWING;
    private javax.swing.JTextField FARROWING_ABW;
    private com.toedter.calendar.JDateChooser FARROWING_ACTUAL;
    private javax.swing.JButton FARROWING_BUTTON;
    private javax.swing.JPanel FARROWING_DETAILS_CONTAINER;
    private javax.swing.JLabel FARROWING_DUE;
    private javax.swing.JLabel FARROWING_EARTAG;
    private javax.swing.JTextField FARROWING_FEMALE;
    private rojeru_san.complementos.RSTableMetro FARROWING_MAIN_TABLE;
    private javax.swing.JTextField FARROWING_MALE;
    private javax.swing.JTextField FARROWING_MORT;
    private rojeru_san.complementos.RSTableMetro FARROWING_ONGOING_BREEDING;
    private javax.swing.JTextField FARROWING_REMARKS;
    private javax.swing.JTextField FARROWING_SEARCH_FIELD;
    private javax.swing.JButton FARROWING_SUBMIT_BUTTON;
    private javax.swing.JLabel FARROWING_TOTAL_PIGLETS;
    private javax.swing.JLabel LATEST_REGSOW_EARTAG;
    private rojeru_san.complementos.RSTableMetro LIST_OF_NOT_FARROWED;
    private rojeru_san.complementos.RSTableMetro LIST_OF_SOW_BY_BATCH;
    private javax.swing.JPanel MAIN_PANEL;
    private javax.swing.JLabel NUMBER_OF_NOTIFICATION;
    private javax.swing.JPanel PAGES;
    private javax.swing.JPanel PERFORMANCE;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_BREEDING_TABLE;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_FARROWING_TABLE;
    private javax.swing.JTextField PERFORMANCE_SEARCHFIELD;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_WEANING_TABLE;
    private javax.swing.JPanel REGISTER_OF_SOW;
    private javax.swing.JTextField REGSOW_ASSIGNED_EMPLOYEE;
    private javax.swing.JTextField REGSOW_BNUMBER;
    private javax.swing.JComboBox<String> REGSOW_BUILDING;
    private javax.swing.JButton REGSOW_BUTTON;
    private com.toedter.calendar.JDateChooser REGSOW_DATE;
    private javax.swing.JTextField REGSOW_PEN;
    private rojeru_san.complementos.RSTableMetro REGSOW_TABLE;
    private javax.swing.JButton START_BREEDING_BUTTON;
    private javax.swing.JButton WARNING_CULL_BUTTON;
    private javax.swing.JLabel WARNING_FORCULLED_LABEL;
    private javax.swing.JPanel WARNING_SOW;
    private rojeru_san.complementos.RSTableMetro WARNING_SOW_DETAILS;
    private rojeru_san.complementos.RSTableMetro WARNING_SOW_LIST_WARNING_SOW;
    private javax.swing.JPanel WEANING;
    private javax.swing.JTextField WEANING_AW;
    private com.toedter.calendar.JDateChooser WEANING_CALENDAR;
    private javax.swing.JLabel WEANING_EARTAG;
    private javax.swing.JTextField WEANING_FEMALE;
    private rojeru_san.complementos.RSTableMetro WEANING_MAIN_TABLE;
    private javax.swing.JTextField WEANING_MALE;
    private javax.swing.JButton WEANING_REBREEDING_BTN;
    private javax.swing.JTextField WEANING_SEARCH_FIELD;
    private javax.swing.JLabel WEANING_TOTAL;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton12;
    private javax.swing.JButton jButton13;
    private javax.swing.JButton jButton14;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JToggleButton jToggleButton1;
    // End of variables declaration//GEN-END:variables

    private void SOW_REGISTRATION() {

        try {

            Date selectedDate = REGSOW_DATE.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();
//                
//            System.out.println(dateString);

            String sql = "INSERT INTO register_sow (eartag, date, bnumber, penbuilding, penroom, assigned_employee) VALUES (?, ?, ?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, LATEST_REGSOW_EARTAG.getText());
            pst.setString(2, dateString);
            pst.setString(3, REGSOW_BNUMBER.getText());
            pst.setString(4, (String) REGSOW_BUILDING.getSelectedItem());
            pst.setString(5, REGSOW_PEN.getText());
            pst.setString(6, REGSOW_ASSIGNED_EMPLOYEE.getText());

            pst.execute();

//            JOptionPane.showMessageDialog(null, ADMIN_REGISTRATION_TYPE.getSelectedItem() + " Registered Succesfully");
            BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void FETCH_CURRENT_EARTAG() {
        try {
            String currentEartagQuery = "SELECT eartag, MAX(eartag) AS highest_eartag FROM register_sow";
            pst = conn.prepareStatement(currentEartagQuery);

            rs = pst.executeQuery();

            if (rs.next()) {
                int currentEartag;
                if (rs.getObject("highest_eartag") == null) {
                    currentEartag = 5000;
                } else {
                    currentEartag = rs.getInt("highest_eartag") + 1;
                }

                String currentEartagStr = String.valueOf(currentEartag);
                CURRENT_REGSOW_EARTAG.setText(currentEartagStr);
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e);

        }
    }

    private void REGISTER_RETRIEVE_REGISTERED_SOW() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT eartag, date, bnumber, penbuilding, penroom, assigned_employee FROM register_sow";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

//            model.addColumn("ID");
            model.addColumn("Eartag");
            model.addColumn("Date");
            model.addColumn("Batch");
            model.addColumn("Building");
            model.addColumn("Pen");
            model.addColumn("Employee");

            while (rs.next()) {
//            int id = rs.getInt("id");
                int eartag = rs.getInt("eartag");
                Date date = rs.getDate("date");
                String bnumber = rs.getString("bnumber");
                String penbuilding = rs.getString("penbuilding");
                String penroom = rs.getString("penroom");
                String assignedEmployee = rs.getString("assigned_employee");

                model.addRow(new Object[]{eartag, date, bnumber, penbuilding, penroom, assignedEmployee});
            }

            if (REGSOW_TABLE != null) {
                REGSOW_TABLE.setModel(model);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER() {
        try {
            String sql = "SELECT bnumber FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            Set<String> batchNumbers = new HashSet<>();

            while (rs.next()) {
                String batchNumber = rs.getString("bnumber");
                batchNumbers.add(batchNumber);
            }

            for (String batchNumber : batchNumbers) {
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

            String query = "SELECT id, eartag, date, bnumber, penbuilding, penroom FROM register_sow WHERE bnumber = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, selectedBatchNumber);
            rs = pst.executeQuery();

            model.addColumn("ID");
            model.addColumn("Eartag");
            model.addColumn("Btach");

            while (rs.next()) {
                int id = rs.getInt("id");
                int eartag = rs.getInt("eartag");

                String bnumber = rs.getString("bnumber");

                model.addRow(new Object[]{id, eartag, bnumber});

            }

            if (LIST_OF_SOW_BY_BATCH != null) {
                LIST_OF_SOW_BY_BATCH.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//    BREEDING 
    private void BREEDING_START_BREEDING() {
        try {            
            boolean isFarrowed = false;
            boolean isCulling = false;

            Date selectedDate = BREEDING_DATE.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();

            String checkSql = "SELECT eartag, culled FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, BREEDING_EARTAG.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                boolean isCulled = rs.getBoolean("culled");
                if (isCulled) {
                    JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " already exists in the breeding table and is marked as culled.");
                    BREEDING_EARTAG.setText("");
                    BREEDING_BOAR_USED.setText("");
                    BREEDING_DATE.setDate(null);
                    BREEDING_EXPECTED_FARROWING.setText("");
                    BREEDING_COMMENTS.setText("");
                    return;
                } else {
                    JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " already exists in the breeding table.");
                    BREEDING_EARTAG.setText("");
                    BREEDING_BOAR_USED.setText("");
                    BREEDING_DATE.setDate(null);
                    BREEDING_EXPECTED_FARROWING.setText("");
                    BREEDING_COMMENTS.setText("");
                    return;
                }
            }

            String sql = "INSERT INTO breeding (eartag, boar_used, breeding_date, expected_farrowing, comments, farrowed, parity, culled, rebreed, breeding_status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            String farrowingUpdate = "SELECT farrowed FROM breeding WHERE eartag = ?";
            String parity = "UPDATE breeding SET parity = 1 WHERE eartag = ?";

            pst = conn.prepareStatement(farrowingUpdate);
            pst.setString(1, BREEDING_EARTAG.getText());
            rs = pst.executeQuery();

            if (rs.next() && rs.getBoolean("farrowed")) {
                JOptionPane.showMessageDialog(null, BREEDING_EARTAG.getText() + " has already been marked as farrowed.");
                BREEDING_EARTAG.setText("");
                BREEDING_BOAR_USED.setText("");
                BREEDING_DATE.setDate(null);
                BREEDING_EXPECTED_FARROWING.setText("");
                BREEDING_COMMENTS.setText("");

                return;
            }

            pst = conn.prepareStatement(sql);
            pst.setString(1, BREEDING_EARTAG.getText());
            pst.setString(2, BREEDING_BOAR_USED.getText());
            pst.setString(3, dateString);
            pst.setString(4, BREEDING_EXPECTED_FARROWING.getText());
            pst.setString(5, BREEDING_COMMENTS.getText());
            pst.setBoolean(6, isFarrowed);
            pst.setInt(7, 0);
            pst.setBoolean(8, isCulling);
            pst.setBoolean(9, false);
            pst.setBoolean(10, true);

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
            BREEDING_COMMENTS.setText("");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void BREEDING_RETRIEVE_BREEDING_DETAILS() {

        try {
            DefaultTableModel model = new DefaultTableModel();
            TableColumnModel columnModel = BREEDING_TABLE.getColumnModel();

            String query = "SELECT b.eartag, b.boar_used, b.breeding_date, b.expected_farrowing, b.comments, b.farrowed, b.culled, b.rebreed, b.breeding_status, b.parity AS highest_parity, rs.penbuilding, rs.penroom, rs.assigned_employee "
                    + "FROM breeding b "
                    + "LEFT JOIN register_sow rs ON b.eartag = rs.eartag "
                    + "WHERE (b.eartag, b.parity) IN ( "
                    + "    SELECT eartag, MAX(parity) "
                    + "    FROM breeding "
                    + "    GROUP BY eartag "
                    + ")";

            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Boar");
            model.addColumn("Date");
            model.addColumn("Expected");
            model.addColumn("Comments");
            model.addColumn("Farrowing");
            model.addColumn("Breeding");
            model.addColumn("Rebreed");
            model.addColumn("Parity");
            model.addColumn("Building");
            model.addColumn("Room");
            model.addColumn("Employee");

            while (rs.next()) {
                int breedingEartag = rs.getInt("eartag");
                Date breeding_date = rs.getDate("breeding_date");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                String comments = rs.getString("comments");

                int isFarrowed = rs.getInt("farrowed");
                String statusForFarrowing = isFarrowed == 1 ? "farrowed" : "not farrowed";

                boolean breedStatus = rs.getBoolean("breeding_status");
                String setStatusForBreeding = breedStatus ? "on breeding" : "not breeding";

                boolean rebreed = rs.getBoolean("rebreed");
                String setStatusForRebreedStatus = rebreed ? "yes" : "no";

                int parity = rs.getInt("highest_parity");
                String penbuilding = rs.getString("penbuilding");
                String penroom = rs.getString("penroom");
                String assignedEmployee = rs.getString("assigned_employee");

                model.addRow(new Object[]{breedingEartag, boar_used, breeding_date, expected_farrowing,
                    comments, statusForFarrowing, setStatusForBreeding, setStatusForRebreedStatus, parity,
                    penbuilding, penroom, assignedEmployee});
            }

            if (BREEDING_TABLE != null) {
                BREEDING_TABLE.setModel(model);

                TableColumn eartagColumn = columnModel.getColumn(0);
                TableColumn boarUsedColumn = columnModel.getColumn(1);
                TableColumn dateColumn = columnModel.getColumn(2);
                TableColumn expectedColumn = columnModel.getColumn(3);
                TableColumn farrowedColumn = columnModel.getColumn(5);
                TableColumn breedingColumn = columnModel.getColumn(6);
                TableColumn rebreedColumn = columnModel.getColumn(7);
                TableColumn parityColumn = columnModel.getColumn(8);
                TableColumn buildingColumn = columnModel.getColumn(9);
                TableColumn roomColumn = columnModel.getColumn(10);

                eartagColumn.setPreferredWidth(10);
                boarUsedColumn.setPreferredWidth(20);
                dateColumn.setPreferredWidth(30);
                expectedColumn.setPreferredWidth(30);
                farrowedColumn.setPreferredWidth(30);
                breedingColumn.setPreferredWidth(30);
                rebreedColumn.setPreferredWidth(20);
                parityColumn.setPreferredWidth(20);
                buildingColumn.setPreferredWidth(20);
                roomColumn.setPreferredWidth(20);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//    FARROWING 
    private void FARROWING_SEARCH_EARTAG() {
        try {
            DefaultTableModel model = new DefaultTableModel();

            String searchValue = FARROWING_SEARCH_FIELD.getText();
            String query = "SELECT * FROM breeding WHERE eartag = ? AND breeding_status = true ORDER BY parity DESC LIMIT 1";

            pst = conn.prepareStatement(query);
            pst.setString(1, searchValue);

            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Boar Used");
            model.addColumn("Expected");

            if (rs.next()) {
                int eartag = rs.getInt("eartag");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                boolean farrowed = rs.getBoolean("farrowed");

                model.addRow(new Object[]{eartag, boar_used, expected_farrowing});

                if (farrowed) {
                    JOptionPane.showMessageDialog(null, "This sow has already farrowed");
                    FARROWING_RETRIEVE_DETAILS();
                    FARROWING_DETAILS_CONTAINER.setVisible(true);
                }

            } else {
                JOptionPane.showMessageDialog(null, "No result found");
                FARROWING_RETRIEVE_DETAILS();
                FARROWING_DETAILS_CONTAINER.setVisible(false);
            }

            if (FARROWING_ONGOING_BREEDING != null) {
                FARROWING_ONGOING_BREEDING.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED() {
        try {
            DefaultTableModel model = new DefaultTableModel();

        String notFarrowed = "SELECT b.eartag, MAX(b.parity) AS highest_parity, b.expected_farrowing "
                + "FROM breeding b "
                + "WHERE b.farrowed = false "
                + "GROUP BY b.eartag";
        
            pst = conn.prepareStatement(notFarrowed);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Expected");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date expected_farrowing = rs.getDate("expected_farrowing");

                model.addRow(new Object[]{eartag, expected_farrowing});
            }

            if (LIST_OF_NOT_FARROWED != null) {
                LIST_OF_NOT_FARROWED.setModel(model);
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void FARROWING_SUBMIT() {

        try {
            String checkSql = "SELECT eartag, culled, farrowed FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, FARROWING_EARTAG.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                boolean isCulled = rs.getBoolean("culled");
                boolean isFarrowed = rs.getBoolean("farrowed");
                if (isCulled) {
                    JOptionPane.showMessageDialog(null, FARROWING_EARTAG.getText() + " already exists in the farrowing table and is marked as culled.", "Error", JOptionPane.ERROR_MESSAGE);
                    FARROWING_EARTAG.setText("");
                    FARROWING_DUE.setText("");
                    FARROWING_ACTUAL.setDate(null);
                    FARROWING_FEMALE.setText("");
                    FARROWING_MALE.setText("");
                    FARROWING_TOTAL_PIGLETS.setText("");
                    FARROWING_ABW.setText("");
                    FARROWING_MORT.setText("");
                    FARROWING_REMARKS.setText("");
                } else if (isFarrowed) {
                    JOptionPane.showMessageDialog(null, FARROWING_EARTAG.getText() + " already exists in the farrowing table and is marked as farrowed.", "Error", JOptionPane.ERROR_MESSAGE);
                    FARROWING_EARTAG.setText("");
                    FARROWING_DUE.setText("");
                    FARROWING_ACTUAL.setDate(null);
                    FARROWING_FEMALE.setText("");
                    FARROWING_MALE.setText("");
                    FARROWING_TOTAL_PIGLETS.setText("");
                    FARROWING_ABW.setText("");
                    FARROWING_MORT.setText("");
                    FARROWING_REMARKS.setText("");
                } else {

                    boolean isCulling = false;
                    Date selectedDate = FARROWING_ACTUAL.getDate();
                    String dateString = new java.sql.Date(selectedDate.getTime()).toString();

                    String sql = "INSERT INTO farrowing_records (eartag, farrowing_actualdate, farrowing_duedate, female_piglets, male_piglets, total_piglets, abw, mortality, remarks, culled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, FARROWING_EARTAG.getText());
                    pst.setString(2, dateString);
                    pst.setString(3, FARROWING_DUE.getText());
                    pst.setString(4, FARROWING_FEMALE.getText());
                    pst.setString(5, FARROWING_MALE.getText());
                    pst.setString(6, FARROWING_TOTAL_PIGLETS.getText());
                    String abwText = FARROWING_ABW.getText();
                    if (!abwText.isEmpty()) {
                        try {
                            double abwValue = Double.parseDouble(abwText);
                            pst.setDouble(7, abwValue);
                        } catch (NumberFormatException ex) {
                            JOptionPane.showMessageDialog(null, "Invalid input for ABW. Please enter a valid number.", "Error", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "ABW field is empty. Please enter a value.", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    pst.setDouble(7, Double.parseDouble(FARROWING_ABW.getText()));
                    pst.setString(8, FARROWING_MORT.getText());
                    pst.setString(9, FARROWING_REMARKS.getText());
                    pst.setBoolean(10, isCulling);

                    pst.execute();

                    // Update breeding record
                    String updateSql = "UPDATE breeding SET farrowed = true, breeding_status = false WHERE eartag = ?";
                    pst = conn.prepareStatement(updateSql);
                    pst.setString(1, FARROWING_EARTAG.getText());
                    pst.execute();

                    JOptionPane.showMessageDialog(null, FARROWING_EARTAG.getText() + " eartag is now farrowed!");

                    FARROWING_RETRIEVE_DETAILS();
                    FARROWING_DETAILS_CONTAINER.setVisible(true);
                    BREEDING_RETRIEVE_BREEDING_DETAILS();
                    
                    FARROWING_EARTAG.setText("");
                    FARROWING_DUE.setText("");
                    FARROWING_ACTUAL.setDate(null);
                    FARROWING_FEMALE.setText("");
                    FARROWING_MALE.setText("");
                    FARROWING_TOTAL_PIGLETS.setText("");
                    FARROWING_ABW.setText("");
                    FARROWING_MORT.setText("");
                    FARROWING_REMARKS.setText("");

                }
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void FARROWING_RETRIEVE_DETAILS() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT fr.eartag, fr.farrowing_actualdate, fr.farrowing_duedate, fr.female_piglets, fr.male_piglets, fr.total_piglets, fr.abw, fr.mortality, fr.remarks, fr.culled, rs.penbuilding, rs.penroom, rs.assigned_employee "
                    + "FROM farrowing_records fr "
                    + "LEFT JOIN register_sow rs ON fr.eartag = rs.eartag "
                    + "WHERE fr.eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(FARROWING_SEARCH_FIELD.getText()));
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Actual");
            model.addColumn("Due");
            model.addColumn("Female");
            model.addColumn("Male");
            model.addColumn("Total");
            model.addColumn("ABW");
            model.addColumn("Mortality");
            model.addColumn("Remarks");
            model.addColumn("Culled");
            model.addColumn("Building");
            model.addColumn("Room");
            model.addColumn("Employee");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date farrowing_actualdate = rs.getDate("farrowing_actualdate");
                Date farrowing_duedate = rs.getDate("farrowing_duedate");
                int female_piglets = rs.getInt("female_piglets");
                int male_piglets = rs.getInt("male_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double abw = rs.getDouble("abw");
                int mortality = rs.getInt("mortality");
                String remarks = rs.getString("remarks");

                int isCulled = rs.getInt("culled");
                String status = isCulled == 1 ? "culled" : "not";

                String penbuilding = rs.getString("penbuilding");
                String penroom = rs.getString("penroom");
                String assignedEmployee = rs.getString("assigned_employee");

                model.addRow(new Object[]{eartag, farrowing_actualdate, farrowing_duedate, female_piglets, male_piglets, total_piglets, abw, mortality, remarks, status, penbuilding, penroom, assignedEmployee});
            }

            if (FARROWING_MAIN_TABLE != null) {
                FARROWING_MAIN_TABLE.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }


    //    WEANING 
private void WEANING_SEARCH_EARTAG() {
    try {
        DefaultTableModel model = new DefaultTableModel();

        String searchValue = WEANING_SEARCH_FIELD.getText();
        String query = "SELECT eartag, farrowed, expected_farrowing "
                + "FROM breeding "
                + "WHERE eartag = ?";

        pst = conn.prepareStatement(query);
        pst.setString(1, searchValue);

        rs = pst.executeQuery();

        model.addColumn("eartag");
        model.addColumn("farrowed");

        if (rs.next()) {
            int eartag = rs.getInt("eartag");
            boolean farrowed = rs.getBoolean("farrowed");
            Date expectedFarrowingDate = rs.getDate("expected_farrowing");

            model.addRow(new Object[]{eartag, farrowed});

            if (farrowed) {
                // Calculate the actual weaning date (28 days after expected farrowing)
                Calendar cal = Calendar.getInstance();
                cal.setTime(expectedFarrowingDate);
                cal.add(Calendar.DAY_OF_MONTH, 28);
                Date actualWeaningDate = cal.getTime();

                // Check if current date is after the actual weaning date
                Date currentDate = new Date();
                if (currentDate.after(actualWeaningDate)) {
                    // Perform weaning operation
                    WEANING_EARTAG.setText(String.valueOf(eartag));
                    // ... perform the weaning operation here
                } else {
                    JOptionPane.showMessageDialog(null, "This sow has not reached the weaning period yet.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "This sow has not farrowed yet.");
            }

        } else {
            JOptionPane.showMessageDialog(null, "No result found");
        }

        // Update the table or display the model as needed
        // ...

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, e);
    }
}

    private void WEANING_SUBMIT() {

        try {
//            isBreeding = true;
            Date selectedDate = WEANING_CALENDAR.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();

            String sql = "INSERT INTO weaning_records (eartag, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw) VALUES (?, ?, ?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, WEANING_EARTAG.getText());
            pst.setString(2, dateString);
            pst.setString(3, WEANING_MALE.getText());
            pst.setString(4, WEANING_FEMALE.getText());
            pst.setString(5, WEANING_TOTAL.getText());
            pst.setDouble(6, Double.parseDouble(WEANING_AW.getText()));

            pst.execute();

            String farrowingUpdate = "UPDATE breeding SET farrowed = FALSE, breeding_status = FALSE WHERE eartag = ?";

            pst = conn.prepareStatement(farrowingUpdate);
            pst.setString(1, WEANING_EARTAG.getText());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, FARROWING_EARTAG.getText() + " EARTAG WEANING DETAILS ARE RECORDED");

            BREEDING_RETRIEVE_BREEDING_DETAILS();

            WEANING_EARTAG.setText("");
            WEANING_CALENDAR.setDate(null);
            WEANING_FEMALE.setText("");
            WEANING_MALE.setText("");
            WEANING_TOTAL.setText("");
            WEANING_AW.setText("");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void WEANING_RETRIEVE_DETAILS() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            String query = "SELECT eartag, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw FROM weaning_records WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(WEANING_SEARCH_FIELD.getText()));
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Actual");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("Total");
            model.addColumn("AW");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date weaning_actualdate = rs.getDate("weaning_actualdate");
                int male_piglets = rs.getInt("male_piglets");
                int female_piglets = rs.getInt("female_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");

                model.addRow(new Object[]{eartag, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
            }

            WEANING_MAIN_TABLE.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//    PERFORMANCE 
    private void PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT boar_used, breeding_date, expected_farrowing, comments, farrowed FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();

//            model.addColumn("EARTAG");
            model.addColumn("Boar Used");
            model.addColumn("Breeding Date");
            model.addColumn("Expected");
            model.addColumn("Comments");
            model.addColumn("Status");

            while (rs.next()) {
//                int eartag = rs.getInt("eartag");
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

//            model.addColumn("eartag");
            model.addColumn("Acutal");
            model.addColumn("Due");
            model.addColumn("Female");
            model.addColumn("Male");
            model.addColumn("Total");
            model.addColumn("ABW");
            model.addColumn("Mortality");
            model.addColumn("Remarks");

            while (rs.next()) {
//                int eartag = rs.getInt("eartag");
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
            String query = "SELECT eartag, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw FROM weaning_records WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Actual");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("Total");
            model.addColumn("AW");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date weaning_actualdate = rs.getDate("weaning_actualdate");
                int male_piglets = rs.getInt("male_piglets");
                int female_piglets = rs.getInt("female_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");

                model.addRow(new Object[]{eartag, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
            }

            PERFORMANCE_WEANING_TABLE.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private Set<String> uploadedNotifications;

    private void initializeUploadedNotifications() {
        uploadedNotifications = new HashSet<>();

        try {

            String sql = "SELECT eartag, notification_message FROM notifications";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String eartag = rs.getString("eartag");
                String notificationMessage = rs.getString("notification_message");
                String notificationKey = eartag + notificationMessage;
                uploadedNotifications.add(notificationKey);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void UPLOAD_NOTIFICATION() {
        initializeUploadedNotifications();

        try {
            String sql = "SELECT eartag, expected_farrowing FROM breeding";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            String sqlForWarning = "SELECT eartag, remarks, total_piglets, mortality, farrowing_actualdate FROM farrowing_records";
            PreparedStatement pstForWarning = conn.prepareStatement(sqlForWarning);
            ResultSet rsForWarning = pstForWarning.executeQuery();

            while (rs.next()) {
                String eartag = rs.getString("eartag");
                LocalDate expectedFarrowing = rs.getDate("expected_farrowing").toLocalDate();

                LocalDate today = LocalDate.now();
                long daysUntilFarrowing = ChronoUnit.DAYS.between(today, expectedFarrowing);
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
                String formattedDate = expectedFarrowing.format(formatter);

                if (daysUntilFarrowing == 0) {
                    String notificationMessage = "Farrowing is happening today " + formattedDate;
                    checkAndStoreNotification(eartag, notificationMessage);
                } else if (daysUntilFarrowing == 1) {
                    String notificationMessage = "Farrowing is expected soon " + formattedDate;
                    checkAndStoreNotification(eartag, notificationMessage);
                } else if (daysUntilFarrowing < 5) {
                    String notificationMessage = "Farrowing is expected soon " + formattedDate;
                    checkAndStoreNotification(eartag, notificationMessage);
                }
            }

            while (rsForWarning.next()) {
                String eartagFromFarrowingRecords = rsForWarning.getString("eartag");

                int remarksCount = 0;

                String remarks = rsForWarning.getString("remarks");
                LocalDate actualFarrowing = rsForWarning.getDate("farrowing_actualdate").toLocalDate();
                int total_piglets = rsForWarning.getInt("total_piglets");
                int mortality = rsForWarning.getInt("mortality");

                LocalDate weaningDate = actualFarrowing.plusDays(28);
                LocalDate currentDate = LocalDate.now();
                //                String formattedDateForWeaning = weaningDate.format(DateTimeFormatter.ofPattern("MMM dd, yyyy"));

                if (currentDate.isEqual(weaningDate)) {
                    String notificationMessageForWeaning = "Today is weaning day!";
                    checkAndStoreNotification(eartagFromFarrowingRecords, notificationMessageForWeaning);
                } else if (currentDate.equals(weaningDate.minusDays(1))) {
                    String notificationMessageForWeaning = "Tomorrow is weaning day!";
                    checkAndStoreNotification(eartagFromFarrowingRecords, notificationMessageForWeaning);
                }

                if (remarks != null) {
                    remarksCount = remarks.split(",").length;
                }

                if (remarksCount > 0) {
                    LocalDate notificationDate = LocalDate.now();
                    String notificationDateString = notificationDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

                    String notificationMessage = "Remarks have reached " + remarksCount + " on " + notificationDateString + ".\n";
                    checkAndStoreNotification(eartagFromFarrowingRecords, notificationMessage);
                }

                if (total_piglets < 7) {
                    LocalDate notificationDate = LocalDate.now();
                    String notificationDateString = notificationDate.format(DateTimeFormatter.ofPattern("MMMM d, yyyy"));

                    String notificationMessage = "Eartag has total piglet count of " + total_piglets + " .";
                    checkAndStoreNotification(eartagFromFarrowingRecords, notificationMessage);
                }

                if (mortality > 0) {
                    String notificationMessage = "Has a mortality of " + mortality + " .";
                    checkAndStoreNotification(eartagFromFarrowingRecords, notificationMessage);
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void checkAndStoreNotification(String eartag, String notificationMessage) throws SQLException {
        String notificationKey = eartag + notificationMessage;

        if (!uploadedNotifications.contains(notificationKey)) {
            storeNotification(eartag, notificationMessage);
            uploadedNotifications.add(notificationKey);
            newNotificationCount++;
        }
    }

    private void storeNotification(String eartag, String notificationMessage) {
        try {
            String sql = "INSERT INTO notifications (eartag, notification_message, created_at) VALUES (?, ?, ?)";
            pst = conn.prepareStatement(sql);
            pst.setString(1, eartag);
            pst.setString(2, notificationMessage);
            pst.setDate(3, java.sql.Date.valueOf(LocalDate.now()));
            pst.executeUpdate();

            String notificationKey = eartag + notificationMessage;
            uploadedNotifications.add(notificationKey);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, ex);
        }
    }

    private void WARNING_FETCH_EARTAG() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String warningSowsQuery = "SELECT DISTINCT eartag "
                    + "FROM farrowing_records "
                    + "WHERE ((female_piglets + male_piglets) < 7 "
                    + "OR mortality > 0 "
                    + "OR remarks IS NOT NULL "
                    + "OR (farrowing_actualdate BETWEEN farrowing_duedate - INTERVAL 3 DAY AND farrowing_duedate + INTERVAL 3 DAY)) "
                    + "AND culled = 0";

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
