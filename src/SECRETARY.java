
import java.awt.CardLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
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
    private final BREEDING_MODAL breedingModal;
    private final REGISTER_UPDATEMODAL regsowUpdateModal;
    private final BREEDING_UPDATEMODAL breedingUpdateModal;
    private final FARROWING_UPDATEMODAL farrowingUpdateModal;
    private final WEANING_UPDATEMODAL weaningUpdateModal;

    private JPanel chartPanel;
    private JPanel pieChartPanel;

    public SECRETARY() {
        conn = DBConnection.getConnection();
        initComponents();
        cardLayout = (CardLayout) (PAGES.getLayout());

        notificationModal = new NOTIFICATIONMODAL();
        notificationModal.setVisible(false);

        breedingModal = new BREEDING_MODAL();
        breedingModal.setVisible(false);

        rebreedingModal = new REBREEDINGMODAL();
        rebreedingModal.setVisible(false);

        //FOR UPDATE 
        regsowUpdateModal = new REGISTER_UPDATEMODAL();
        regsowUpdateModal.setVisible(false);

        breedingUpdateModal = new BREEDING_UPDATEMODAL();
        breedingUpdateModal.setVisible(false);

        farrowingUpdateModal = new FARROWING_UPDATEMODAL();
        farrowingUpdateModal.setVisible(false);

        weaningUpdateModal = new WEANING_UPDATEMODAL();
        weaningUpdateModal.setVisible(false);

        FETCH_CURRENT_EARTAG();
        REGISTER_RETRIEVE_REGISTERED_SOW();
        REGISTER_FETCH_ASSIGNED_EMPLOYEE();
        BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER();

        BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER_TO_USE_IN_REG_SOW();
        BREEDING_RETRIEVE_BREEDING_DETAILS();
        LIST_OF_SOW_DROPDOWN.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                BREEDING_RETRIEVE_SOW_BY_CLASSIFICATION();
            }
        });

        FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();
        FARROWING_RETRIEVE_ALL_FARROWED();
        FARROWING_FETCH_VALUE_FROM_BATCH_NUMBER();

        FARROWING_BATCH_NUMBER.setVisible(false);

//        FARROWING_DETAILS_CONTAINER.setVisible(false);
        RETRIEVE_NOT_WEANED_EARTAGS();

        UPLOAD_NOTIFICATION();

        LIST_OF_NOT_FARROWED.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();

                int eartag = Integer.parseInt(table.getValueAt(row, 0).toString());
                String batch = table.getValueAt(row, 1).toString();

                String farrowingDue = table.getValueAt(row, 2).toString();

                FARROWING_EARTAG.setText(Integer.toString(eartag));
                FARROWING_DUE.setText(farrowingDue);
                FARROWING_SEARCH_FIELD.setText(Integer.toString(eartag));
                FARROWING_BATCH_NUMBER.setText(batch);

            }
        });

//          WEANING
        WEANING_RETRIEVE_DETAILS_ALL_DETAILS();
        WEANING_FETCH_VALUE_FROM_BATCH_NUMBER();

        CLOSE_WEANING_TABLE.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                JTable table = (JTable) evt.getSource();
                int row = table.getSelectedRow();

                int eartag = Integer.parseInt(table.getValueAt(row, 0).toString());
                String batch = table.getValueAt(row, 1).toString();

                WEANING_EARTAG.setText(Integer.toString(eartag));
                WEANING_BATCH_NUMBER_CLOSE.setText(batch);

            }
        });

//        warning 
        WARNING_FETCH_EARTAG();
        WEANING_BATCH_NUMBER_CLOSE.setVisible(false);

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
        rSButtonHover1 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover2 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover3 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover4 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover5 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover6 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover7 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover8 = new rojeru_san.complementos.RSButtonHover();
        jLabel41 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        PAGES = new javax.swing.JPanel();
        MAIN_PANEL = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jLabel40 = new javax.swing.JLabel();
        REGISTER_OF_SOW = new javax.swing.JPanel();
        jScrollPane16 = new javax.swing.JScrollPane();
        REGSOW_TABLE = new rojeru_san.complementos.RSTableMetro();
        DROPDOWN_FOR_BATCH_NUMBER = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        REGSOW_DATE = new com.toedter.calendar.JDateChooser();
        jLabel1 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        REGSOW_BUILDING = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        EARTAG_CONTAINER = new javax.swing.JPanel();
        LATEST_REGSOW_EARTAG = new javax.swing.JLabel();
        CURRENT_REGSOW_EARTAG = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        REGSOW_PEN = new javax.swing.JTextField();
        rSButtonHover9 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover10 = new rojeru_san.complementos.RSButtonHover();
        REGSOW_BNUMBER = new javax.swing.JComboBox<>();
        REGSOW_ASSIGNED_EMPLOYEE = new javax.swing.JComboBox<>();
        jPanel19 = new javax.swing.JPanel();
        jLabel49 = new javax.swing.JLabel();
        rSButtonHover21 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover22 = new rojeru_san.complementos.RSButtonHover();
        BREEDING = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        BREEDING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jPanel18 = new javax.swing.JPanel();
        jLabel48 = new javax.swing.JLabel();
        rSButtonHover14 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover20 = new rojeru_san.complementos.RSButtonHover();
        LIST_OF_SOW_DROPDOWN = new javax.swing.JComboBox<>();
        rSButtonHover26 = new rojeru_san.complementos.RSButtonHover();
        FARROWING = new javax.swing.JPanel();
        FARROWING_SEARCH_FIELD = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        rSButtonHover12 = new rojeru_san.complementos.RSButtonHover();
        jLabel25 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        FARROWING_EARTAG = new javax.swing.JLabel();
        FARROWING_ABW = new javax.swing.JTextField();
        FARROWING_MORT = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        FARROWING_REMARKS = new javax.swing.JTextField();
        jLabel27 = new javax.swing.JLabel();
        FARROWING_ACTUAL = new com.toedter.calendar.JDateChooser();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        FARROWING_MALE = new javax.swing.JTextField();
        jLabel18 = new javax.swing.JLabel();
        FARROWING_FEMALE = new javax.swing.JTextField();
        jButton9 = new javax.swing.JButton();
        jPanel6 = new javax.swing.JPanel();
        FARROWING_TOTAL_PIGLETS = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jPanel5 = new javax.swing.JPanel();
        FARROWING_DUE = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jScrollPane14 = new javax.swing.JScrollPane();
        FARROWING_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();
        jLabel38 = new javax.swing.JLabel();
        jScrollPane15 = new javax.swing.JScrollPane();
        LIST_OF_NOT_FARROWED = new rojeru_san.complementos.RSTableMetro();
        rSButtonHover13 = new rojeru_san.complementos.RSButtonHover();
        jPanel17 = new javax.swing.JPanel();
        jLabel47 = new javax.swing.JLabel();
        jLabel51 = new javax.swing.JLabel();
        jLabel52 = new javax.swing.JLabel();
        FARROWING_DROPDOWN_FOR_BATCH_NUMBER = new javax.swing.JComboBox<>();
        FARROWING_BATCH_NUMBER = new javax.swing.JLabel();
        rSButtonHover24 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover25 = new rojeru_san.complementos.RSButtonHover();
        WEANING = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        WEANING_SEARCH_FIELD = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        rSButtonHover15 = new rojeru_san.complementos.RSButtonHover();
        WEANING_AW = new javax.swing.JTextField();
        jLabel29 = new javax.swing.JLabel();
        jPanel9 = new javax.swing.JPanel();
        WEANING_TOTAL = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        WEANING_EARTAG = new javax.swing.JLabel();
        WEANING_MALE = new javax.swing.JTextField();
        WEANING_FEMALE = new javax.swing.JTextField();
        jLabel33 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        WEANING_CALENDAR = new com.toedter.calendar.JDateChooser();
        jLabel30 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();
        jPanel16 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        rSButtonHover19 = new rojeru_san.complementos.RSButtonHover();
        jScrollPane17 = new javax.swing.JScrollPane();
        CLOSE_WEANING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jLabel50 = new javax.swing.JLabel();
        WEANING_TABLE_CONTAINER = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        WEANING_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();
        WEANING_REBREEDING_BTN = new rojeru_san.complementos.RSButtonHover();
        WEANING_DROPDOWN_FOR_BATCH_NUMBER = new javax.swing.JComboBox<>();
        jLabel53 = new javax.swing.JLabel();
        rSButtonHover23 = new rojeru_san.complementos.RSButtonHover();
        rSButtonHover27 = new rojeru_san.complementos.RSButtonHover();
        WEANING_BATCH_NUMBER_CLOSE = new javax.swing.JLabel();
        VIEW_RECORDS = new javax.swing.JPanel();
        PERFORMANCE_SEARCHFIELD = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        jLabel36 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        PERFORMANCE_FARROWING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jScrollPane7 = new javax.swing.JScrollPane();
        PERFORMANCE_WEANING_TABLE = new rojeru_san.complementos.RSTableMetro();
        jScrollPane8 = new javax.swing.JScrollPane();
        PERFORMANCE_BREEDING_TABLE = new rojeru_san.complementos.RSTableMetro();
        rSButtonHover16 = new rojeru_san.complementos.RSButtonHover();
        jLabel42 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jLabel45 = new javax.swing.JLabel();
        WARNING_SOW = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        WARNING_FORCULLED_LABEL = new javax.swing.JLabel();
        WARNING_CULL_BUTTON = new rojeru_san.complementos.RSButtonHover();
        jScrollPane9 = new javax.swing.JScrollPane();
        WARNING_SOW_DETAILS = new rojeru_san.complementos.RSTableMetro();
        jScrollPane10 = new javax.swing.JScrollPane();
        WARNING_SOW_LIST_WARNING_SOW = new rojeru_san.complementos.RSTableMetro();
        jPanel13 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        CULLED_SOW = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        CULLED_TOTAL_CULLED = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jScrollPane11 = new javax.swing.JScrollPane();
        CULLED_MAIN_TABLE = new rojeru_san.complementos.RSTableMetro();
        jPanel14 = new javax.swing.JPanel();
        jLabel44 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(26, 46, 53));
        jPanel1.setMinimumSize(new java.awt.Dimension(250, 330));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSButtonHover1.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover1.setForeground(new java.awt.Color(0, 0, 0));
        rSButtonHover1.setText("HOME");
        rSButtonHover1.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover1.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover1.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover1ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 90, 160, -1));

        rSButtonHover2.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover2.setText("REGISTER");
        rSButtonHover2.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover2.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover2ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 160, -1));

        rSButtonHover3.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover3.setText("BREEDING");
        rSButtonHover3.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover3.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover3ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, 160, -1));

        rSButtonHover4.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover4.setText("FARROWING");
        rSButtonHover4.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover4.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover4ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 160, -1));

        rSButtonHover5.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover5.setText("WEANING");
        rSButtonHover5.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover5.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover5ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 290, 160, -1));

        rSButtonHover6.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover6.setText("VIEW RECORDS");
        rSButtonHover6.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover6.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover6ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 340, 160, -1));

        rSButtonHover7.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover7.setText("WARNING SOW");
        rSButtonHover7.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover7.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover7ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 160, -1));

        rSButtonHover8.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover8.setText("CULLED SOW");
        rSButtonHover8.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover8.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover8ActionPerformed(evt);
            }
        });
        jPanel1.add(rSButtonHover8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 440, 160, -1));

        jLabel41.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pictures/notification.png"))); // NOI18N
        jLabel41.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel41MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel41, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 650, -1, 40));

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pictures/logout.png"))); // NOI18N
        jLabel8.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel8MouseClicked(evt);
            }
        });
        jPanel1.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 650, 50, 40));

        jSplitPane2.setLeftComponent(jPanel1);

        PAGES.setLayout(new java.awt.CardLayout());

        MAIN_PANEL.setBackground(new java.awt.Color(255, 217, 90));
        MAIN_PANEL.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel10.setBackground(new java.awt.Color(26, 46, 53));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pictures/larawan2.jpg"))); // NOI18N
        jPanel10.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 900, 400));

        jLabel39.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel39.setForeground(new java.awt.Color(255, 255, 255));
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("SOW MONITORING SYSTEM");
        jPanel10.add(jLabel39, new org.netbeans.lib.awtextra.AbsoluteConstraints(170, 60, 770, 60));

        jLabel40.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel40.setForeground(new java.awt.Color(255, 217, 90));
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("WELCOME TO RDJ FARM INC. ");
        jPanel10.add(jLabel40, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 0, 770, 60));

        MAIN_PANEL.add(jPanel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 1110, 620));

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
            REGSOW_TABLE.getColumnModel().getColumn(3).setHeaderValue("Building");
            REGSOW_TABLE.getColumnModel().getColumn(4).setResizable(false);
            REGSOW_TABLE.getColumnModel().getColumn(4).setHeaderValue("Employee");
        }

        REGISTER_OF_SOW.add(jScrollPane16, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 80, 850, 550));

        DROPDOWN_FOR_BATCH_NUMBER.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Batch" }));
        REGISTER_OF_SOW.add(DROPDOWN_FOR_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(970, 40, 150, 30));

        jPanel2.setBackground(new java.awt.Color(26, 46, 53));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        jPanel2.add(REGSOW_DATE, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 190, 40));

        jLabel1.setForeground(new java.awt.Color(255, 217, 90));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATE");
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(80, 40, 50, -1));

        jLabel4.setForeground(new java.awt.Color(255, 217, 90));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("BUILDING");
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 130, 70, -1));

        REGSOW_BUILDING.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "1", "2", "3" }));
        jPanel2.add(REGSOW_BUILDING, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 150, 80, 30));

        jLabel5.setForeground(new java.awt.Color(255, 217, 90));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("PEN");
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 130, 70, -1));

        jLabel2.setForeground(new java.awt.Color(255, 217, 90));
        jLabel2.setText("ASSIGNED EMPLOYEE");
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 210, -1, -1));

        jLabel12.setForeground(new java.awt.Color(255, 217, 90));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("EARTAG");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 350, 90, 40));

        EARTAG_CONTAINER.setBackground(new java.awt.Color(153, 153, 153));
        EARTAG_CONTAINER.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        LATEST_REGSOW_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        LATEST_REGSOW_EARTAG.setForeground(new java.awt.Color(255, 255, 0));
        LATEST_REGSOW_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        EARTAG_CONTAINER.add(LATEST_REGSOW_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 40));

        jPanel2.add(EARTAG_CONTAINER, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 190, 40));

        CURRENT_REGSOW_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        CURRENT_REGSOW_EARTAG.setForeground(new java.awt.Color(255, 255, 0));
        CURRENT_REGSOW_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CURRENT_REGSOW_EARTAG.setText("5000");
        jPanel2.add(CURRENT_REGSOW_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 350, 60, 40));

        jLabel6.setForeground(new java.awt.Color(255, 217, 90));
        jLabel6.setText("BATCH NUMBER");
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 290, -1, -1));
        jPanel2.add(REGSOW_PEN, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 150, 70, 30));

        rSButtonHover9.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover9.setText("ADD EARTAG");
        rSButtonHover9.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover9.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover9.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover9ActionPerformed(evt);
            }
        });
        jPanel2.add(rSButtonHover9, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 400, 140, -1));

        rSButtonHover10.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover10.setText("REGISTER");
        rSButtonHover10.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover10.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover10.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover10ActionPerformed(evt);
            }
        });
        jPanel2.add(rSButtonHover10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 520, 130, -1));

        REGSOW_BNUMBER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                REGSOW_BNUMBERActionPerformed(evt);
            }
        });
        jPanel2.add(REGSOW_BNUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 310, 190, 40));

        jPanel2.add(REGSOW_ASSIGNED_EMPLOYEE, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 230, 190, 40));

        REGISTER_OF_SOW.add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 80, 240, 610));

        jPanel19.setBackground(new java.awt.Color(26, 46, 53));
        jPanel19.setForeground(new java.awt.Color(26, 46, 53));
        jPanel19.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel49.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel49.setForeground(new java.awt.Color(255, 217, 90));
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("REGISTER SOW");
        jPanel19.add(jLabel49, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        REGISTER_OF_SOW.add(jPanel19, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 350, 50));

        rSButtonHover21.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover21.setText("DELETE");
        rSButtonHover21.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover21.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover21.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover21.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover21ActionPerformed(evt);
            }
        });
        REGISTER_OF_SOW.add(rSButtonHover21, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 640, 80, 30));

        rSButtonHover22.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover22.setText("UPDATE");
        rSButtonHover22.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover22.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover22.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover22.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover22ActionPerformed(evt);
            }
        });
        REGISTER_OF_SOW.add(rSButtonHover22, new org.netbeans.lib.awtextra.AbsoluteConstraints(1030, 640, 80, 30));

        PAGES.add(REGISTER_OF_SOW, "PAGE_1");

        BREEDING.setBackground(new java.awt.Color(255, 217, 90));
        BREEDING.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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

        BREEDING.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 110, 1070, 520));

        jPanel18.setBackground(new java.awt.Color(26, 46, 53));
        jPanel18.setForeground(new java.awt.Color(26, 46, 53));
        jPanel18.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel48.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel48.setForeground(new java.awt.Color(255, 217, 90));
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("BREEDING");
        jPanel18.add(jLabel48, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        BREEDING.add(jPanel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(740, 10, 360, 50));

        rSButtonHover14.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover14.setText("SHOW BREEDING MODAL");
        rSButtonHover14.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover14.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover14.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover14.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover14ActionPerformed(evt);
            }
        });
        BREEDING.add(rSButtonHover14, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 60, 170, 30));

        rSButtonHover20.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover20.setText("REFRESH");
        rSButtonHover20.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover20.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover20.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover20.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover20ActionPerformed(evt);
            }
        });
        BREEDING.add(rSButtonHover20, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 70, 120, 30));

        LIST_OF_SOW_DROPDOWN.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All", "Breeding", "Farrowing", "Weaning", "Culled" }));
        BREEDING.add(LIST_OF_SOW_DROPDOWN, new org.netbeans.lib.awtextra.AbsoluteConstraints(980, 70, 110, 30));

        rSButtonHover26.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover26.setText("UPDATE");
        rSButtonHover26.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover26.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover26.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover26.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover26ActionPerformed(evt);
            }
        });
        BREEDING.add(rSButtonHover26, new org.netbeans.lib.awtextra.AbsoluteConstraints(1010, 640, 80, 30));

        PAGES.add(BREEDING, "PAGE_2");

        FARROWING.setBackground(new java.awt.Color(255, 217, 90));
        FARROWING.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_SEARCH_FIELD.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        FARROWING_SEARCH_FIELD.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                FARROWING_SEARCH_FIELDKeyTyped(evt);
            }
        });
        FARROWING.add(FARROWING_SEARCH_FIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 60, 290, 40));

        jPanel11.setBackground(new java.awt.Color(26, 46, 53));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        rSButtonHover12.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover12.setText("FARROW");
        rSButtonHover12.setColorHover(new java.awt.Color(255, 217, 90));
        rSButtonHover12.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover12.setColorTextHover(new java.awt.Color(26, 46, 53));
        rSButtonHover12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover12ActionPerformed(evt);
            }
        });
        jPanel11.add(rSButtonHover12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 589, 190, -1));

        jLabel25.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel25.setForeground(new java.awt.Color(255, 217, 90));
        jLabel25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel25.setText("ABW");
        jPanel11.add(jLabel25, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 370, 190, -1));

        jPanel7.setBackground(new java.awt.Color(153, 153, 153));
        jPanel7.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FARROWING_EARTAG.setForeground(new java.awt.Color(255, 217, 90));
        FARROWING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel7.add(FARROWING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 40));

        jPanel11.add(jPanel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 50, 200, 40));

        FARROWING_ABW.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel11.add(FARROWING_ABW, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 390, 190, 40));

        FARROWING_MORT.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel11.add(FARROWING_MORT, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 460, 190, 40));

        jLabel26.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel26.setForeground(new java.awt.Color(255, 217, 90));
        jLabel26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel26.setText("MORT");
        jPanel11.add(jLabel26, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 440, 190, -1));

        FARROWING_REMARKS.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jPanel11.add(FARROWING_REMARKS, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 530, 190, 40));

        jLabel27.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel27.setForeground(new java.awt.Color(255, 217, 90));
        jLabel27.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel27.setText("REMARKS");
        jPanel11.add(jLabel27, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 510, 190, -1));
        jPanel11.add(FARROWING_ACTUAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 190, 200, 40));

        jLabel21.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel21.setForeground(new java.awt.Color(255, 217, 90));
        jLabel21.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel21.setText("ACTUAL DATE");
        jPanel11.add(jLabel21, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 170, 200, -1));

        jLabel22.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel22.setForeground(new java.awt.Color(255, 217, 90));
        jLabel22.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel22.setText("M");
        jPanel11.add(jLabel22, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 240, 60, -1));

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
        jPanel11.add(FARROWING_MALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 260, 60, 30));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(255, 217, 90));
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("F");
        jPanel11.add(jLabel18, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 240, 60, -1));

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
        jPanel11.add(FARROWING_FEMALE, new org.netbeans.lib.awtextra.AbsoluteConstraints(90, 260, 60, 30));

        jButton9.setText("=");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jPanel11.add(jButton9, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, 50, 30));

        jPanel6.setBackground(new java.awt.Color(153, 153, 153));
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_TOTAL_PIGLETS.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FARROWING_TOTAL_PIGLETS.setForeground(new java.awt.Color(255, 217, 90));
        FARROWING_TOTAL_PIGLETS.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel6.add(FARROWING_TOTAL_PIGLETS, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 40));

        jPanel11.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 330, 190, 40));

        jLabel23.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel23.setForeground(new java.awt.Color(255, 217, 90));
        jLabel23.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel23.setText("TOTAL BORN");
        jPanel11.add(jLabel23, new org.netbeans.lib.awtextra.AbsoluteConstraints(60, 310, 120, -1));

        jLabel24.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel24.setForeground(new java.awt.Color(255, 217, 90));
        jLabel24.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel24.setText("DUE DATE");
        jPanel11.add(jLabel24, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 100, 200, -1));

        jPanel5.setBackground(new java.awt.Color(153, 153, 153));
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        FARROWING_DUE.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        FARROWING_DUE.setForeground(new java.awt.Color(255, 217, 90));
        FARROWING_DUE.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel5.add(FARROWING_DUE, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 200, 40));

        jPanel11.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 120, 200, 40));

        jLabel20.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel20.setForeground(new java.awt.Color(255, 217, 90));
        jLabel20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel20.setText("EAR TAG");
        jPanel11.add(jLabel20, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 200, -1));

        FARROWING.add(jPanel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 240, 680));

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
        FARROWING_MAIN_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        FARROWING_MAIN_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        FARROWING_MAIN_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        FARROWING_MAIN_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        FARROWING_MAIN_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        FARROWING_MAIN_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        FARROWING_MAIN_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane14.setViewportView(FARROWING_MAIN_TABLE);

        FARROWING.add(jScrollPane14, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 340, 820, 310));

        jLabel38.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel38.setText("SEARCH EARTAG IF FARROWED AND VIEW RECORDS");
        FARROWING.add(jLabel38, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 30, 300, 30));

        LIST_OF_NOT_FARROWED.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Eartag", "Batch", "Expected", "Days before expected farrowing"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        LIST_OF_NOT_FARROWED.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        LIST_OF_NOT_FARROWED.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        LIST_OF_NOT_FARROWED.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        LIST_OF_NOT_FARROWED.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        LIST_OF_NOT_FARROWED.setFuenteFilas(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        LIST_OF_NOT_FARROWED.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        LIST_OF_NOT_FARROWED.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane15.setViewportView(LIST_OF_NOT_FARROWED);
        if (LIST_OF_NOT_FARROWED.getColumnModel().getColumnCount() > 0) {
            LIST_OF_NOT_FARROWED.getColumnModel().getColumn(0).setResizable(false);
            LIST_OF_NOT_FARROWED.getColumnModel().getColumn(1).setResizable(false);
            LIST_OF_NOT_FARROWED.getColumnModel().getColumn(2).setResizable(false);
            LIST_OF_NOT_FARROWED.getColumnModel().getColumn(3).setResizable(false);
        }

        FARROWING.add(jScrollPane15, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 150, 820, 140));

        rSButtonHover13.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover13.setText("SEARCH");
        rSButtonHover13.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover13.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover13.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover13.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover13ActionPerformed(evt);
            }
        });
        FARROWING.add(rSButtonHover13, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 60, 80, 40));

        jPanel17.setBackground(new java.awt.Color(26, 46, 53));
        jPanel17.setForeground(new java.awt.Color(26, 46, 53));
        jPanel17.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel47.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel47.setForeground(new java.awt.Color(255, 217, 90));
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("FARROWING");
        jPanel17.add(jLabel47, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        FARROWING.add(jPanel17, new org.netbeans.lib.awtextra.AbsoluteConstraints(720, 10, 360, 50));

        jLabel51.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel51.setText("LIST OF ALL FARROWED EARTAGS");
        FARROWING.add(jLabel51, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 310, 230, 30));

        jLabel52.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel52.setText("LIST OF EARTAG THAT ARE CURRENTLY NOT FARROWED");
        FARROWING.add(jLabel52, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 120, 360, 30));

        FARROWING_DROPDOWN_FOR_BATCH_NUMBER.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Batch" }));
        FARROWING_DROPDOWN_FOR_BATCH_NUMBER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FARROWING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed(evt);
            }
        });
        FARROWING.add(FARROWING_DROPDOWN_FOR_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(930, 300, 150, 30));

        FARROWING_BATCH_NUMBER.setText("batch here");
        FARROWING.add(FARROWING_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 100, 90, 30));

        rSButtonHover24.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover24.setText("REFRESH");
        rSButtonHover24.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover24.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover24.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover24.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover24ActionPerformed(evt);
            }
        });
        FARROWING.add(rSButtonHover24, new org.netbeans.lib.awtextra.AbsoluteConstraints(960, 100, 120, 30));

        rSButtonHover25.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover25.setText("UPDATE");
        rSButtonHover25.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover25.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover25.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover25.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover25ActionPerformed(evt);
            }
        });
        FARROWING.add(rSButtonHover25, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 660, 80, 30));

        PAGES.add(FARROWING, "PAGE_3");

        WEANING.setBackground(new java.awt.Color(255, 217, 90));
        WEANING.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel32.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel32.setText("LIST OF ALL CURRENTLY WEANED");
        WEANING.add(jLabel32, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 310, 210, 30));
        WEANING.add(WEANING_SEARCH_FIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 90, 250, 40));

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
        jPanel9.add(WEANING_TOTAL, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 0, 190, 40));

        jPanel12.add(jPanel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 250, 210, 40));

        jPanel8.setBackground(new java.awt.Color(153, 153, 153));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        WEANING_EARTAG.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        WEANING_EARTAG.setForeground(new java.awt.Color(255, 217, 90));
        WEANING_EARTAG.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jPanel8.add(WEANING_EARTAG, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 210, 40));

        jPanel12.add(jPanel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 210, 40));

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

        WEANING.add(jPanel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 70, 270, 560));

        jPanel16.setBackground(new java.awt.Color(26, 46, 53));
        jPanel16.setForeground(new java.awt.Color(26, 46, 53));
        jPanel16.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel46.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel46.setForeground(new java.awt.Color(255, 217, 90));
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("WEANING");
        jPanel16.add(jLabel46, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        WEANING.add(jPanel16, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 10, 360, 50));

        rSButtonHover19.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover19.setText("SUBMIT");
        rSButtonHover19.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover19.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover19.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover19ActionPerformed(evt);
            }
        });
        WEANING.add(rSButtonHover19, new org.netbeans.lib.awtextra.AbsoluteConstraints(580, 90, 80, 40));

        CLOSE_WEANING_TABLE.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Eartag", "Batch", "Actual Farrowing", "Expected Weaning", "Days Before Weaning"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        CLOSE_WEANING_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        CLOSE_WEANING_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        CLOSE_WEANING_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        CLOSE_WEANING_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        CLOSE_WEANING_TABLE.setColorSelBackgound(new java.awt.Color(255, 217, 90));
        CLOSE_WEANING_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        CLOSE_WEANING_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        CLOSE_WEANING_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        jScrollPane17.setViewportView(CLOSE_WEANING_TABLE);
        if (CLOSE_WEANING_TABLE.getColumnModel().getColumnCount() > 0) {
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(0).setResizable(false);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(0).setPreferredWidth(20);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(1).setResizable(false);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(2).setResizable(false);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(2).setPreferredWidth(30);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(3).setResizable(false);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(3).setPreferredWidth(20);
            CLOSE_WEANING_TABLE.getColumnModel().getColumn(4).setResizable(false);
        }

        WEANING.add(jScrollPane17, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 160, 760, 130));

        jLabel50.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("CHECK EARTAG AND VIEW WEANING RECORD");
        WEANING.add(jLabel50, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 60, 270, 30));

        WEANING_TABLE_CONTAINER.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        WEANING_MAIN_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        WEANING_MAIN_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        WEANING_MAIN_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        WEANING_MAIN_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        WEANING_MAIN_TABLE.setFuenteFilas(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        WEANING_MAIN_TABLE.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        WEANING_MAIN_TABLE.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane5.setViewportView(WEANING_MAIN_TABLE);

        WEANING_TABLE_CONTAINER.add(jScrollPane5, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 760, 290));

        WEANING.add(WEANING_TABLE_CONTAINER, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 340, 760, 290));

        WEANING_REBREEDING_BTN.setBackground(new java.awt.Color(26, 46, 53));
        WEANING_REBREEDING_BTN.setForeground(new java.awt.Color(255, 217, 90));
        WEANING_REBREEDING_BTN.setText("START REBREEDING");
        WEANING_REBREEDING_BTN.setColorHover(new java.awt.Color(26, 46, 53));
        WEANING_REBREEDING_BTN.setColorText(new java.awt.Color(255, 217, 90));
        WEANING_REBREEDING_BTN.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WEANING_REBREEDING_BTNActionPerformed(evt);
            }
        });
        WEANING.add(WEANING_REBREEDING_BTN, new org.netbeans.lib.awtextra.AbsoluteConstraints(600, 640, -1, 40));

        WEANING_DROPDOWN_FOR_BATCH_NUMBER.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Batch" }));
        WEANING_DROPDOWN_FOR_BATCH_NUMBER.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WEANING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed(evt);
            }
        });
        WEANING.add(WEANING_DROPDOWN_FOR_BATCH_NUMBER, new org.netbeans.lib.awtextra.AbsoluteConstraints(920, 300, 150, 30));

        jLabel53.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("CLOSE TO WEANING");
        WEANING.add(jLabel53, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 130, 130, 30));

        rSButtonHover23.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover23.setText("REFRESH");
        rSButtonHover23.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover23.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover23.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover23.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover23ActionPerformed(evt);
            }
        });
        WEANING.add(rSButtonHover23, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 90, 120, 30));

        rSButtonHover27.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover27.setText("UPDATE");
        rSButtonHover27.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover27.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover27.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover27.setFont(new java.awt.Font("Tahoma", 1, 10)); // NOI18N
        rSButtonHover27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover27ActionPerformed(evt);
            }
        });
        WEANING.add(rSButtonHover27, new org.netbeans.lib.awtextra.AbsoluteConstraints(990, 640, 80, 30));

        WEANING_BATCH_NUMBER_CLOSE.setText("batch diria tol");
        WEANING.add(WEANING_BATCH_NUMBER_CLOSE, new org.netbeans.lib.awtextra.AbsoluteConstraints(460, 140, 100, -1));

        PAGES.add(WEANING, "PAGE_4");

        VIEW_RECORDS.setBackground(new java.awt.Color(255, 217, 90));
        VIEW_RECORDS.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        VIEW_RECORDS.add(PERFORMANCE_SEARCHFIELD, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 60, 260, 40));

        jLabel35.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel35.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel35.setText("FARROWING");
        VIEW_RECORDS.add(jLabel35, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 60, 520, 40));

        jLabel36.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel36.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel36.setText("BREEDING");
        VIEW_RECORDS.add(jLabel36, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 340, 510, 40));

        jLabel37.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("WEANING");
        VIEW_RECORDS.add(jLabel37, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 340, 540, 40));

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
        PERFORMANCE_FARROWING_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        PERFORMANCE_FARROWING_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        PERFORMANCE_FARROWING_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        PERFORMANCE_FARROWING_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
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

        VIEW_RECORDS.add(jScrollPane6, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 110, 1050, 220));

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
        PERFORMANCE_WEANING_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        PERFORMANCE_WEANING_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        PERFORMANCE_WEANING_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        PERFORMANCE_WEANING_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
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

        VIEW_RECORDS.add(jScrollPane7, new org.netbeans.lib.awtextra.AbsoluteConstraints(560, 380, 520, 300));

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
        PERFORMANCE_BREEDING_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        PERFORMANCE_BREEDING_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        PERFORMANCE_BREEDING_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        PERFORMANCE_BREEDING_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
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

        VIEW_RECORDS.add(jScrollPane8, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 380, 510, 300));

        rSButtonHover16.setBackground(new java.awt.Color(255, 255, 255));
        rSButtonHover16.setText("SEARCH");
        rSButtonHover16.setColorHover(new java.awt.Color(26, 46, 53));
        rSButtonHover16.setColorText(new java.awt.Color(26, 46, 53));
        rSButtonHover16.setColorTextHover(new java.awt.Color(255, 217, 90));
        rSButtonHover16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rSButtonHover16ActionPerformed(evt);
            }
        });
        VIEW_RECORDS.add(rSButtonHover16, new org.netbeans.lib.awtextra.AbsoluteConstraints(950, 60, 130, -1));

        jLabel42.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        jLabel42.setText("SEARCH USING EARTAG");
        VIEW_RECORDS.add(jLabel42, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 40, 150, -1));

        jPanel15.setBackground(new java.awt.Color(26, 46, 53));
        jPanel15.setForeground(new java.awt.Color(26, 46, 53));
        jPanel15.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel45.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel45.setForeground(new java.awt.Color(255, 217, 90));
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("VIEW RECORDS");
        jPanel15.add(jLabel45, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        VIEW_RECORDS.add(jPanel15, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 360, 50));

        PAGES.add(VIEW_RECORDS, "PAGE_5");

        WARNING_SOW.setBackground(new java.awt.Color(255, 217, 90));
        WARNING_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(26, 46, 53));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("WARNING SOW");
        WARNING_SOW.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 40, 120, 40));
        WARNING_SOW.add(WARNING_FORCULLED_LABEL, new org.netbeans.lib.awtextra.AbsoluteConstraints(810, 50, 80, 20));

        WARNING_CULL_BUTTON.setBackground(new java.awt.Color(26, 46, 53));
        WARNING_CULL_BUTTON.setText("CULL");
        WARNING_CULL_BUTTON.setColorHover(new java.awt.Color(255, 217, 90));
        WARNING_CULL_BUTTON.setColorTextHover(new java.awt.Color(26, 46, 53));
        WARNING_CULL_BUTTON.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WARNING_CULL_BUTTONActionPerformed(evt);
            }
        });
        WARNING_SOW.add(WARNING_CULL_BUTTON, new org.netbeans.lib.awtextra.AbsoluteConstraints(670, 560, -1, -1));

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
        WARNING_SOW_DETAILS.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        WARNING_SOW_DETAILS.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        WARNING_SOW_DETAILS.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        WARNING_SOW_DETAILS.setColorForegroundHead(new java.awt.Color(255, 217, 90));
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

        WARNING_SOW.add(jScrollPane9, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 80, 710, 620));

        WARNING_SOW_LIST_WARNING_SOW.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null},
                {null, null},
                {null, null},
                {null, null}
            },
            new String [] {
                "Eartag", "Feedback"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        WARNING_SOW_LIST_WARNING_SOW.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        WARNING_SOW_LIST_WARNING_SOW.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        WARNING_SOW_LIST_WARNING_SOW.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        WARNING_SOW_LIST_WARNING_SOW.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        WARNING_SOW_LIST_WARNING_SOW.setFuenteFilas(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        WARNING_SOW_LIST_WARNING_SOW.setFuenteFilasSelect(new java.awt.Font("Tahoma", 1, 8)); // NOI18N
        WARNING_SOW_LIST_WARNING_SOW.setFuenteHead(new java.awt.Font("Tahoma", 1, 12)); // NOI18N
        jScrollPane10.setViewportView(WARNING_SOW_LIST_WARNING_SOW);
        if (WARNING_SOW_LIST_WARNING_SOW.getColumnModel().getColumnCount() > 0) {
            WARNING_SOW_LIST_WARNING_SOW.getColumnModel().getColumn(0).setResizable(false);
            WARNING_SOW_LIST_WARNING_SOW.getColumnModel().getColumn(1).setResizable(false);
        }

        WARNING_SOW.add(jScrollPane10, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 80, 320, 620));

        jPanel13.setBackground(new java.awt.Color(26, 46, 53));
        jPanel13.setForeground(new java.awt.Color(26, 46, 53));
        jPanel13.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel43.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel43.setForeground(new java.awt.Color(255, 217, 90));
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("WARNING PAGE");
        jPanel13.add(jLabel43, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        WARNING_SOW.add(jPanel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 360, 50));

        PAGES.add(WARNING_SOW, "PAGE_6");

        CULLED_SOW.setBackground(new java.awt.Color(255, 217, 90));
        CULLED_SOW.setForeground(new java.awt.Color(51, 0, 51));
        CULLED_SOW.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(26, 46, 53));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("LIST OF CULLED SOW");
        CULLED_SOW.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(620, 50, 300, 40));

        CULLED_TOTAL_CULLED.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        CULLED_TOTAL_CULLED.setForeground(new java.awt.Color(26, 46, 53));
        CULLED_TOTAL_CULLED.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        CULLED_TOTAL_CULLED.setText("10000");
        CULLED_SOW.add(CULLED_TOTAL_CULLED, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 220, 200, 70));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(26, 46, 53));
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
        CULLED_MAIN_TABLE.setColorBackgoundHead(new java.awt.Color(26, 46, 53));
        CULLED_MAIN_TABLE.setColorFilasForeground1(new java.awt.Color(0, 0, 0));
        CULLED_MAIN_TABLE.setColorFilasForeground2(new java.awt.Color(0, 0, 0));
        CULLED_MAIN_TABLE.setColorForegroundHead(new java.awt.Color(255, 217, 90));
        jScrollPane11.setViewportView(CULLED_MAIN_TABLE);

        CULLED_SOW.add(jScrollPane11, new org.netbeans.lib.awtextra.AbsoluteConstraints(680, 100, 180, 580));

        jPanel14.setBackground(new java.awt.Color(26, 46, 53));
        jPanel14.setForeground(new java.awt.Color(26, 46, 53));
        jPanel14.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel44.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        jLabel44.setForeground(new java.awt.Color(255, 217, 90));
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("CULLED PAGE");
        jPanel14.add(jLabel44, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 0, 280, 50));

        CULLED_SOW.add(jPanel14, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 360, 50));

        PAGES.add(CULLED_SOW, "PAGE_7");

        jSplitPane2.setRightComponent(PAGES);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1405, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 717, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void FARROWING_MALEActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FARROWING_MALEActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FARROWING_MALEActionPerformed

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

    private void FARROWING_SEARCH_FIELDKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_FARROWING_SEARCH_FIELDKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_FARROWING_SEARCH_FIELDKeyTyped

    private void rSButtonHover1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover1ActionPerformed
        cardLayout.show(PAGES, "MAIN_PANEL");
    }//GEN-LAST:event_rSButtonHover1ActionPerformed

    private void rSButtonHover2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover2ActionPerformed
        cardLayout.show(PAGES, "PAGE_1");
    }//GEN-LAST:event_rSButtonHover2ActionPerformed

    private void rSButtonHover3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover3ActionPerformed
        cardLayout.show(PAGES, "PAGE_2");
        BREEDING_RETRIEVE_BREEDING_DETAILS();

    }//GEN-LAST:event_rSButtonHover3ActionPerformed

    private void rSButtonHover4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover4ActionPerformed
        cardLayout.show(PAGES, "PAGE_3");

        FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();
    }//GEN-LAST:event_rSButtonHover4ActionPerformed

    private void rSButtonHover5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover5ActionPerformed
        cardLayout.show(PAGES, "PAGE_4");
    }//GEN-LAST:event_rSButtonHover5ActionPerformed

    private void rSButtonHover6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover6ActionPerformed
        cardLayout.show(PAGES, "PAGE_5");
    }//GEN-LAST:event_rSButtonHover6ActionPerformed

    private void rSButtonHover7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover7ActionPerformed
        cardLayout.show(PAGES, "PAGE_6");
    }//GEN-LAST:event_rSButtonHover7ActionPerformed

    private void rSButtonHover8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover8ActionPerformed
        cardLayout.show(PAGES, "PAGE_7");
    }//GEN-LAST:event_rSButtonHover8ActionPerformed

    private void jLabel41MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel41MouseClicked
        notificationModal.setVisible(!notificationModal.isVisible());
    }//GEN-LAST:event_jLabel41MouseClicked

    private void jLabel8MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel8MouseClicked
        LOGIN n = new LOGIN();

        n.setVisible(true);
        dispose();
    }//GEN-LAST:event_jLabel8MouseClicked

    private void rSButtonHover9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover9ActionPerformed
        String addEartag = CURRENT_REGSOW_EARTAG.getText();
        LATEST_REGSOW_EARTAG.setText(addEartag);
    }//GEN-LAST:event_rSButtonHover9ActionPerformed

    private void rSButtonHover10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover10ActionPerformed

        String pen = REGSOW_PEN.getText().trim();
        String employee = (String) REGSOW_ASSIGNED_EMPLOYEE.getSelectedItem();
        String bNumber = (String) REGSOW_BNUMBER.getSelectedItem();

        if (!pen.isEmpty() && !pen.matches("\\d+")) {
            JOptionPane.showMessageDialog(null, "Invalid input for pen. Please make sure it is a number.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!employee.isEmpty() && !employee.matches("^[a-zA-Z ]+$")) {
            JOptionPane.showMessageDialog(null, "Invalid input for employee. Please make sure it contains only alphabets.", "Error", JOptionPane.ERROR_MESSAGE);
        } else if (!bNumber.isEmpty() && !bNumber.matches("^[a-zA-Z0-9]+$")) {
            JOptionPane.showMessageDialog(null, "Invalid input for bNumber. Please make sure it contains letters and numbers only.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            SOW_REGISTRATION();
            REGISTER_RETRIEVE_REGISTERED_SOW();
//            BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER();

            REGSOW_DATE.setDate(null);
//            REGSOW_BNUMBER.setSelectedItem(null);
            LATEST_REGSOW_EARTAG.setText("");
            REGSOW_BUILDING.setSelectedIndex(0);
            REGSOW_ASSIGNED_EMPLOYEE.setSelectedIndex(0);
            REGSOW_PEN.setText("");

            FETCH_CURRENT_EARTAG();
        }

    }//GEN-LAST:event_rSButtonHover10ActionPerformed

    private void rSButtonHover12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover12ActionPerformed
        String abw = FARROWING_ABW.getText().trim();
        String mort = FARROWING_MORT.getText().trim();

        if ((!mort.matches("\\d+") || Double.parseDouble(abw) <= 0 || !abw.matches("[0-9]+\\.?[0-9]*")) && FARROWING_REMARKS.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Invalid input. Please make sure at least one field is not empty or all fields have the correct format.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            FARROWING_SUBMIT();
            FARROWING_RETRIEVE_DETAILS();
            BREEDING_RETRIEVE_BREEDING_DETAILS();
            FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();
            // Perform other actions as needed

            WARNING_FETCH_EARTAG();
            CULLED_FETCH_EARTAG();

            UPLOAD_NOTIFICATION();
        }
    }//GEN-LAST:event_rSButtonHover12ActionPerformed

    private void rSButtonHover13ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover13ActionPerformed
        FARROWING_SEARCH_EARTAG();

        FARROWING_RETRIEVE_DETAILS();

    }//GEN-LAST:event_rSButtonHover13ActionPerformed

    private void rSButtonHover15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover15ActionPerformed
        WEANING_SUBMIT();
    }//GEN-LAST:event_rSButtonHover15ActionPerformed

    private void rSButtonHover16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover16ActionPerformed
        PERFORMANCE_BREEDING_RETRIEVE_BREEDING_DETAILS();
        PERFORMCE_FARROWING_RETRIEVE_DETAILS();
        PERFORMANCE_WEANING_RETRIEVE_DETAILS();
    }//GEN-LAST:event_rSButtonHover16ActionPerformed

    private void WARNING_CULL_BUTTONActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WARNING_CULL_BUTTONActionPerformed
        String culledValue = WARNING_FORCULLED_LABEL.getText();
        int culledIntEartag = Integer.parseInt(culledValue);
        try {

            String checkCulledQuery = "SELECT culled FROM farrowing_records WHERE eartag = ?";
            pst = conn.prepareStatement(checkCulledQuery);
            pst.setInt(1, culledIntEartag);
            rs = pst.executeQuery();

            if (rs.next() && rs.getBoolean("culled")) {
                JOptionPane.showMessageDialog(null, "Sow with eartag number " + culledValue + " is already culled.");
                CULLED_FETCH_EARTAG();
                return;
            }

            String breedingUpdateQuery = "UPDATE breeding SET sow_status = ? WHERE eartag = ?";
            PreparedStatement breedingUpdateStmt = conn.prepareStatement(breedingUpdateQuery);
            breedingUpdateStmt.setInt(1, 3);
            breedingUpdateStmt.setInt(2, culledIntEartag);
            breedingUpdateStmt.executeUpdate();

            String farrowingUpdateQuery = "UPDATE farrowing_records SET culled = ? WHERE eartag = ?";
            PreparedStatement farrowingUpdateStmt = conn.prepareStatement(farrowingUpdateQuery);
            farrowingUpdateStmt.setBoolean(1, true);
            farrowingUpdateStmt.setInt(2, culledIntEartag);
            farrowingUpdateStmt.executeUpdate();

            String weaningUpdateQuery = "UPDATE weaning_records SET culled = ? WHERE eartag = ?";
            PreparedStatement weaningUpdateStmt = conn.prepareStatement(weaningUpdateQuery);
            weaningUpdateStmt.setBoolean(1, true);
            weaningUpdateStmt.setInt(2, culledIntEartag);
            weaningUpdateStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Culling status for sow with eartag number " + culledValue + " has been updated to culled.");

            WARNING_FETCH_EARTAG();
            CULLED_FETCH_EARTAG();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }//GEN-LAST:event_WARNING_CULL_BUTTONActionPerformed

    private void rSButtonHover19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover19ActionPerformed

        WEANING_SEARCH_EARTAG();
    }//GEN-LAST:event_rSButtonHover19ActionPerformed

    private void REGSOW_BNUMBERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_REGSOW_BNUMBERActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_REGSOW_BNUMBERActionPerformed

    private void rSButtonHover14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover14ActionPerformed

        var isVisible = breedingModal.isVisible();
        breedingModal.setVisible(!isVisible);

    }//GEN-LAST:event_rSButtonHover14ActionPerformed

    private void rSButtonHover20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover20ActionPerformed
        BREEDING_RETRIEVE_BREEDING_DETAILS();
    }//GEN-LAST:event_rSButtonHover20ActionPerformed

    private void rSButtonHover22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover22ActionPerformed
        int selectedRow = REGSOW_TABLE.getSelectedRow();
        String eartag = REGSOW_TABLE.getValueAt(selectedRow, 0).toString();

        regsowUpdateModal.REGISTER_RETRIEVE_REGISTERED_SOW(eartag);
        regsowUpdateModal.setVisible(true);
    }//GEN-LAST:event_rSButtonHover22ActionPerformed

    private void FARROWING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FARROWING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_FARROWING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed

    private void WEANING_REBREEDING_BTNActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WEANING_REBREEDING_BTNActionPerformed
        int selectedRow = WEANING_MAIN_TABLE.getSelectedRow();
        String eartag = WEANING_MAIN_TABLE.getValueAt(selectedRow, 0).toString();
        String batch = WEANING_MAIN_TABLE.getValueAt(selectedRow, 1).toString();

        rebreedingModal.setEartag(eartag, batch);
        rebreedingModal.setVisible(true);
    }//GEN-LAST:event_WEANING_REBREEDING_BTNActionPerformed

    private void WEANING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WEANING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_WEANING_DROPDOWN_FOR_BATCH_NUMBERActionPerformed

    private void rSButtonHover23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover23ActionPerformed
        WEANING_RETRIEVE_DETAILS_ALL_DETAILS();
        RETRIEVE_NOT_WEANED_EARTAGS();
    }//GEN-LAST:event_rSButtonHover23ActionPerformed

    private void rSButtonHover24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover24ActionPerformed
        FARROWING_RETRIEVE_ALL_FARROWED();
        FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED();
    }//GEN-LAST:event_rSButtonHover24ActionPerformed

    private void rSButtonHover25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover25ActionPerformed
        int selectedRow = FARROWING_MAIN_TABLE.getSelectedRow();
        String eartag = FARROWING_MAIN_TABLE.getValueAt(selectedRow, 0).toString();

        farrowingUpdateModal.FARROWING_RETRIEVE_RECORD(eartag);
        farrowingUpdateModal.setVisible(true);
    }//GEN-LAST:event_rSButtonHover25ActionPerformed

    private void rSButtonHover26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover26ActionPerformed
        int selectedRow = BREEDING_TABLE.getSelectedRow();
        String eartag = BREEDING_TABLE.getValueAt(selectedRow, 0).toString();

        breedingUpdateModal.BREEDING_RETRIEVE_BREEDING_SOW(eartag);
        breedingUpdateModal.setVisible(true);
    }//GEN-LAST:event_rSButtonHover26ActionPerformed

    private void rSButtonHover27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover27ActionPerformed
        int selectedRow = WEANING_MAIN_TABLE.getSelectedRow();
        String eartag = WEANING_MAIN_TABLE.getValueAt(selectedRow, 0).toString();

        weaningUpdateModal.WEANING_RETRIEVE_RECORD(eartag);
        weaningUpdateModal.setVisible(true);


    }//GEN-LAST:event_rSButtonHover27ActionPerformed

    private void rSButtonHover21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rSButtonHover21ActionPerformed

        int selectedRow = REGSOW_TABLE.getSelectedRow();
        String eartag = REGSOW_TABLE.getValueAt(selectedRow, 0).toString();

        try {
            String query = "DELETE FROM register_sow WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setString(1, eartag);

            int rowsDeleted = pst.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Sow registration deleted successfully!");
                REGISTER_RETRIEVE_REGISTERED_SOW();
            } else {
                JOptionPane.showMessageDialog(null, "Failed to delete sow registration.");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_rSButtonHover21ActionPerformed

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
    private javax.swing.JPanel BREEDING;
    private rojeru_san.complementos.RSTableMetro BREEDING_TABLE;
    private rojeru_san.complementos.RSTableMetro CLOSE_WEANING_TABLE;
    private rojeru_san.complementos.RSTableMetro CULLED_MAIN_TABLE;
    private javax.swing.JPanel CULLED_SOW;
    private javax.swing.JLabel CULLED_TOTAL_CULLED;
    private javax.swing.JLabel CURRENT_REGSOW_EARTAG;
    private javax.swing.JComboBox<String> DROPDOWN_FOR_BATCH_NUMBER;
    private javax.swing.JPanel EARTAG_CONTAINER;
    private javax.swing.JPanel FARROWING;
    private javax.swing.JTextField FARROWING_ABW;
    private com.toedter.calendar.JDateChooser FARROWING_ACTUAL;
    private javax.swing.JLabel FARROWING_BATCH_NUMBER;
    private javax.swing.JComboBox<String> FARROWING_DROPDOWN_FOR_BATCH_NUMBER;
    private javax.swing.JLabel FARROWING_DUE;
    private javax.swing.JLabel FARROWING_EARTAG;
    private javax.swing.JTextField FARROWING_FEMALE;
    private rojeru_san.complementos.RSTableMetro FARROWING_MAIN_TABLE;
    private javax.swing.JTextField FARROWING_MALE;
    private javax.swing.JTextField FARROWING_MORT;
    private javax.swing.JTextField FARROWING_REMARKS;
    private javax.swing.JTextField FARROWING_SEARCH_FIELD;
    private javax.swing.JLabel FARROWING_TOTAL_PIGLETS;
    private javax.swing.JLabel LATEST_REGSOW_EARTAG;
    private rojeru_san.complementos.RSTableMetro LIST_OF_NOT_FARROWED;
    private javax.swing.JComboBox<String> LIST_OF_SOW_DROPDOWN;
    private javax.swing.JPanel MAIN_PANEL;
    private javax.swing.JPanel PAGES;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_BREEDING_TABLE;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_FARROWING_TABLE;
    private javax.swing.JTextField PERFORMANCE_SEARCHFIELD;
    private rojeru_san.complementos.RSTableMetro PERFORMANCE_WEANING_TABLE;
    private javax.swing.JPanel REGISTER_OF_SOW;
    private javax.swing.JComboBox<String> REGSOW_ASSIGNED_EMPLOYEE;
    private javax.swing.JComboBox<String> REGSOW_BNUMBER;
    private javax.swing.JComboBox<String> REGSOW_BUILDING;
    private com.toedter.calendar.JDateChooser REGSOW_DATE;
    private javax.swing.JTextField REGSOW_PEN;
    private rojeru_san.complementos.RSTableMetro REGSOW_TABLE;
    private javax.swing.JPanel VIEW_RECORDS;
    private rojeru_san.complementos.RSButtonHover WARNING_CULL_BUTTON;
    private javax.swing.JLabel WARNING_FORCULLED_LABEL;
    private javax.swing.JPanel WARNING_SOW;
    private rojeru_san.complementos.RSTableMetro WARNING_SOW_DETAILS;
    private rojeru_san.complementos.RSTableMetro WARNING_SOW_LIST_WARNING_SOW;
    private javax.swing.JPanel WEANING;
    private javax.swing.JTextField WEANING_AW;
    private javax.swing.JLabel WEANING_BATCH_NUMBER_CLOSE;
    private com.toedter.calendar.JDateChooser WEANING_CALENDAR;
    private javax.swing.JComboBox<String> WEANING_DROPDOWN_FOR_BATCH_NUMBER;
    private javax.swing.JLabel WEANING_EARTAG;
    private javax.swing.JTextField WEANING_FEMALE;
    private rojeru_san.complementos.RSTableMetro WEANING_MAIN_TABLE;
    private javax.swing.JTextField WEANING_MALE;
    private rojeru_san.complementos.RSButtonHover WEANING_REBREEDING_BTN;
    private javax.swing.JTextField WEANING_SEARCH_FIELD;
    private javax.swing.JPanel WEANING_TABLE_CONTAINER;
    private javax.swing.JLabel WEANING_TOTAL;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel18;
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
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane17;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSplitPane jSplitPane2;
    private rojeru_san.complementos.RSButtonHover rSButtonHover1;
    private rojeru_san.complementos.RSButtonHover rSButtonHover10;
    private rojeru_san.complementos.RSButtonHover rSButtonHover12;
    private rojeru_san.complementos.RSButtonHover rSButtonHover13;
    private rojeru_san.complementos.RSButtonHover rSButtonHover14;
    private rojeru_san.complementos.RSButtonHover rSButtonHover15;
    private rojeru_san.complementos.RSButtonHover rSButtonHover16;
    private rojeru_san.complementos.RSButtonHover rSButtonHover19;
    private rojeru_san.complementos.RSButtonHover rSButtonHover2;
    private rojeru_san.complementos.RSButtonHover rSButtonHover20;
    private rojeru_san.complementos.RSButtonHover rSButtonHover21;
    private rojeru_san.complementos.RSButtonHover rSButtonHover22;
    private rojeru_san.complementos.RSButtonHover rSButtonHover23;
    private rojeru_san.complementos.RSButtonHover rSButtonHover24;
    private rojeru_san.complementos.RSButtonHover rSButtonHover25;
    private rojeru_san.complementos.RSButtonHover rSButtonHover26;
    private rojeru_san.complementos.RSButtonHover rSButtonHover27;
    private rojeru_san.complementos.RSButtonHover rSButtonHover3;
    private rojeru_san.complementos.RSButtonHover rSButtonHover4;
    private rojeru_san.complementos.RSButtonHover rSButtonHover5;
    private rojeru_san.complementos.RSButtonHover rSButtonHover6;
    private rojeru_san.complementos.RSButtonHover rSButtonHover7;
    private rojeru_san.complementos.RSButtonHover rSButtonHover8;
    private rojeru_san.complementos.RSButtonHover rSButtonHover9;
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
            pst.setString(3, (String) REGSOW_BNUMBER.getSelectedItem());
            pst.setString(4, (String) REGSOW_BUILDING.getSelectedItem());
            pst.setString(5, REGSOW_PEN.getText());
            pst.setString(6, (String) REGSOW_ASSIGNED_EMPLOYEE.getSelectedItem());

            pst.execute();

//            JOptionPane.showMessageDialog(null, ADMIN_REGISTRATION_TYPE.getSelectedItem() + " Registered Succesfully");
//            BREEDING_RETRIEVE_SOW_BY_BATCH_NUMBER();
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
                    currentEartag = 4563;
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
            String sql = "SELECT DISTINCT bnumber FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String batchNumber = rs.getString("bnumber");
                DROPDOWN_FOR_BATCH_NUMBER.addItem(batchNumber);
            }

            DROPDOWN_FOR_BATCH_NUMBER.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object selectedBatch = DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();
                    if (selectedBatch.equals("All Batch")) {
                        REGISTER_RETRIEVE_REGISTERED_SOW();
                    } else {
                        REGISTER_RETRIEVE_REGISTERED_SOW_BY_BATCH();
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void REGISTER_RETRIEVE_REGISTERED_SOW_BY_BATCH() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String selectedBatchNumber = (String) DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();
            String query = "SELECT eartag, date, bnumber, penbuilding, penroom, assigned_employee FROM register_sow WHERE bnumber = ?";

            pst = conn.prepareStatement(query);
            pst.setString(1, selectedBatchNumber);
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

    private void REGISTER_FETCH_ASSIGNED_EMPLOYEE() {
        try {
            String sql = "SELECT DISTINCT assigned_employee FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String batchNumber = rs.getString("assigned_employee");
                REGSOW_ASSIGNED_EMPLOYEE.addItem(batchNumber);
            }

            REGSOW_ASSIGNED_EMPLOYEE.addItem("Add Employee?"); // Add "Add Batch" option

            REGSOW_ASSIGNED_EMPLOYEE.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object selectedBatch = REGSOW_ASSIGNED_EMPLOYEE.getSelectedItem();
                    if (selectedBatch.equals("Add Employee?")) {
                        try {
                            String newBatch = JOptionPane.showInputDialog("Enter a new employee:");

                            REGSOW_ASSIGNED_EMPLOYEE.addItem(newBatch);
                            REGSOW_ASSIGNED_EMPLOYEE.setSelectedItem(newBatch);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void BREEDING_FETCH_VALUE_FROM_BATCH_NUMBER_TO_USE_IN_REG_SOW() {
        try {
            String sql = "SELECT DISTINCT bnumber FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String batchNumber = rs.getString("bnumber");
                REGSOW_BNUMBER.addItem(batchNumber);
            }

            REGSOW_BNUMBER.addItem("Add Batch?"); // Add "Add Batch" option

            REGSOW_BNUMBER.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Object selectedBatch = REGSOW_BNUMBER.getSelectedItem();
                    if (selectedBatch.equals("Add Batch?")) {
                        try {
                            String newBatch = JOptionPane.showInputDialog("Enter a new batch number:");

                            REGSOW_BNUMBER.addItem(newBatch);
                            REGSOW_BNUMBER.setSelectedItem(newBatch);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, ex);
                        }
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//    BREEDING 
    private void BREEDING_RETRIEVE_BREEDING_DETAILS() {

        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT b.eartag, b.boar_used, b.breeding_date, b.expected_farrowing, b.breeding_type, b.lactate, b.rebreed, b.sow_status, b.parity AS highest_parity, rs.penbuilding, rs.penroom, rs.assigned_employee "
                    + "FROM breeding b "
                    + "LEFT JOIN register_sow rs ON b.eartag = rs.eartag "
                    + "WHERE (b.eartag, b.parity) IN ( "
                    + "    SELECT eartag, MAX(parity) "
                    + "    FROM breeding "
                    + "    GROUP BY eartag "
                    + ")"
                    + "ORDER BY b.eartag, b.parity DESC";

            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Boar");
            model.addColumn("Date");
            model.addColumn("Expected");

            model.addColumn("Type");
            model.addColumn("Lactate");

            model.addColumn("Building");
            model.addColumn("Room");
            model.addColumn("Employee");
            model.addColumn("Parity");

            model.addColumn("Rebreed");
            model.addColumn("Sow Status");

            while (rs.next()) {
                int breedingEartag = rs.getInt("eartag");
                Date breeding_date = rs.getDate("breeding_date");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                String breeding_type = rs.getString("breeding_type");
                String lactate = rs.getString("lactate");

                boolean rebreed = rs.getBoolean("rebreed");
                String setStatusForRebreedStatus = rebreed ? "yes" : "no";

                int parity = rs.getInt("highest_parity");
                String penbuilding = rs.getString("penbuilding");
                String penroom = rs.getString("penroom");
                String assignedEmployee = rs.getString("assigned_employee");

                int sowStatus = rs.getInt("sow_status");
                String sowStatusString = "";
                sowStatusString = switch (sowStatus) {
                    case 0 ->
                        "Breeding";
                    case 1 ->
                        "Farrowed";
                    case 2 ->
                        "Weaned";
                    case 3 ->
                        "Culled";
                    default ->
                        "Unknown";
                };

                model.addRow(new Object[]{
                    breedingEartag, boar_used, breeding_date, expected_farrowing, breeding_type,
                    lactate, penbuilding, penroom, assignedEmployee, parity,
                    setStatusForRebreedStatus, sowStatusString
                });
            }

            if (BREEDING_TABLE != null) {
                BREEDING_TABLE.setModel(model);

            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

//    FARROWING 
    private void FARROWING_SEARCH_EARTAG() {
        try {
            String searchValue = FARROWING_SEARCH_FIELD.getText();
            String query = "SELECT * FROM breeding WHERE eartag = ? AND sow_status = 0";

            pst = conn.prepareStatement(query);
            pst.setString(1, searchValue);

            rs = pst.executeQuery();

            if (rs.next()) {
//            int eartag = rs.getInt("eartag");
//            String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                int sow_status = rs.getInt("sow_status");

                switch (sow_status) {
                    case 3:
                        JOptionPane.showMessageDialog(null, "This sow has been culled");
                        return;
                    case 1:
                        JOptionPane.showMessageDialog(null, "This sow has already farrowed on " + expected_farrowing);
                        FARROWING_RETRIEVE_DETAILS();
                        break;
                    default:
                        JOptionPane.showMessageDialog(null, "The sow is not yer farrowed and its expected farrowing date for this sow is " + expected_farrowing);
                        FARROWING_RETRIEVE_DETAILS();
                        break;
                }
            } else {
                JOptionPane.showMessageDialog(null, "No result found");
                FARROWING_RETRIEVE_DETAILS();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void FARROWING_LIST_OF_EARTAGS_CURRENTLY_NOT_FARROWED() {
        try {
            DefaultTableModel model = new DefaultTableModel();

            String notFarrowed = "SELECT b.eartag, MAX(b.parity) AS highest_parity, b.expected_farrowing, b.batch_number "
                    + "FROM breeding b "
                    + "WHERE b.sow_status = 0 "
                    + "GROUP BY b.eartag";

            pst = conn.prepareStatement(notFarrowed);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Batch");
            model.addColumn("Expected");
            model.addColumn("Pre-farrowing interval");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                String bnumber = rs.getString("batch_number");

                long currentTime = System.currentTimeMillis();
                long expectedTime = expected_farrowing.getTime();
                long daysBefore = TimeUnit.DAYS.convert(expectedTime - currentTime, TimeUnit.MILLISECONDS);

                model.addRow(new Object[]{eartag, bnumber, expected_farrowing, daysBefore});
            }

            if (LIST_OF_NOT_FARROWED != null) {
                LIST_OF_NOT_FARROWED.setModel(model);
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(null, "An error occurred while retrieving not farrowed eartags");
        }
    }

    private void FARROWING_SUBMIT() {

        try {
            String checkSql = "SELECT eartag, sow_status FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(checkSql);
            pst.setString(1, FARROWING_EARTAG.getText());
            rs = pst.executeQuery();
            if (rs.next()) {
                int sow_status = rs.getInt("sow_status");

//                culled
                switch (sow_status) {
                    case 3:
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
                        break;
                    case 1:
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
                        break;
                    default:
                        boolean isCulling = false;
                        Date selectedDate = FARROWING_ACTUAL.getDate();
                        String dateString = new java.sql.Date(selectedDate.getTime()).toString();
                        String sql = "INSERT INTO farrowing_records (eartag, batch_number, farrowing_actualdate, farrowing_duedate, female_piglets, male_piglets, total_piglets, abw, mortality, remarks, culled) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                        pst = conn.prepareStatement(sql);
                        pst.setString(1, FARROWING_EARTAG.getText());
                        pst.setString(2, FARROWING_BATCH_NUMBER.getText());
                        pst.setString(3, dateString);
                        pst.setString(4, FARROWING_DUE.getText());
                        pst.setString(5, FARROWING_FEMALE.getText());
                        pst.setString(6, FARROWING_MALE.getText());
                        pst.setString(7, FARROWING_TOTAL_PIGLETS.getText());
                        pst.setString(8, FARROWING_ABW.getText());
                        pst.setString(9, FARROWING_MORT.getText());
                        pst.setString(10, FARROWING_REMARKS.getText());
                        pst.setBoolean(11, isCulling);
                        pst.execute();

                        // Update breeding record
                        String updateSql = "UPDATE breeding SET sow_status = 1 WHERE eartag = ?";
                        pst = conn.prepareStatement(updateSql);
                        pst.setString(1, FARROWING_EARTAG.getText());
                        pst.execute();

                        JOptionPane.showMessageDialog(null, FARROWING_EARTAG.getText() + " eartag is now farrowed!");

                        FARROWING_RETRIEVE_DETAILS();
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
                        break;
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

    private void FARROWING_FETCH_VALUE_FROM_BATCH_NUMBER() {
        try {
            String sql = "SELECT DISTINCT bnumber FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String batchNumber = rs.getString("bnumber");
                FARROWING_DROPDOWN_FOR_BATCH_NUMBER.addItem(batchNumber);
            }

            FARROWING_DROPDOWN_FOR_BATCH_NUMBER.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    Object selectedBatch = FARROWING_DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();
                    if (selectedBatch.equals("All Batch")) {
                        FARROWING_RETRIEVE_ALL_FARROWED();
                    } else {
                        FARROWING_RETRIEVE_BY_BATCH();
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void FARROWING_RETRIEVE_BY_BATCH() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            String selectedBatchNumber = (String) FARROWING_DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();

            String query = "SELECT fr.eartag, fr.farrowing_actualdate, fr.farrowing_duedate, fr.female_piglets, fr.male_piglets, fr.total_piglets, fr.abw, fr.mortality, fr.remarks, fr.culled, rs.penbuilding, rs.penroom, rs.assigned_employee "
                    + "FROM farrowing_records fr "
                    + "LEFT JOIN register_sow rs ON fr.eartag = rs.eartag WHERE fr.batch_number = ? and culled = false";

            pst = conn.prepareStatement(query);
            pst.setString(1, selectedBatchNumber);
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

    private void FARROWING_RETRIEVE_ALL_FARROWED() {
        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT fr.eartag, fr.farrowing_actualdate, fr.farrowing_duedate, fr.female_piglets, fr.male_piglets, fr.total_piglets, fr.abw, fr.mortality, fr.remarks, fr.culled, rs.penbuilding, rs.penroom, rs.assigned_employee "
                    + "FROM farrowing_records fr "
                    + "LEFT JOIN register_sow rs ON fr.eartag = rs.eartag WHERE culled = false";
            pst = conn.prepareStatement(query);
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

            String searchValue = WEANING_SEARCH_FIELD.getText();
            String query = "SELECT eartag, sow_status, expected_farrowing "
                    + "FROM breeding "
                    + "WHERE eartag = ? AND sow_status != 3";
            pst = conn.prepareStatement(query);
            pst.setString(1, searchValue);

            rs = pst.executeQuery();

            if (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date expectedFarrowingDate = rs.getDate("expected_farrowing");

                WEANING_EARTAG.setText(String.valueOf(eartag));

                Calendar cal = Calendar.getInstance();
                cal.setTime(expectedFarrowingDate);
                cal.add(Calendar.DAY_OF_MONTH, 28);
                Date actualWeaningDate = cal.getTime();

                Date currentDate = new Date();
                if (currentDate.after(actualWeaningDate)) {
                    JOptionPane.showMessageDialog(null, "This sow has reached the weaning period.");
                } else {
                    JOptionPane.showMessageDialog(null, "This sow has not reached the weaning period yet.");
                }

                WEANING_RETRIEVE_DETAILS();
            } else {
                JOptionPane.showMessageDialog(null, "No result found or the sow has been culled.");
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void RETRIEVE_NOT_WEANED_EARTAGS() {
        try {
            DefaultTableModel model = new DefaultTableModel();

            String query = "SELECT fr.eartag, fr.batch_number, fr.farrowing_actualdate "
                    + "FROM farrowing_records fr "
                    + "LEFT JOIN weaning_records wr ON fr.eartag = wr.eartag "
                    + "WHERE wr.eartag IS NULL";
            pst = conn.prepareStatement(query);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Batch");
            model.addColumn("Actual Farrowing");
            model.addColumn("Expected Weaning");
            model.addColumn("Days Before Weaning");

            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                Date actualFarrowing = rs.getDate("farrowing_actualdate");
                String batch_number = rs.getString("batch_number");
                Calendar cal = Calendar.getInstance();
                cal.setTime(actualFarrowing);
                cal.add(Calendar.DAY_OF_MONTH, 28);
                Date expectedWeaning = cal.getTime();

                long currentTime = System.currentTimeMillis();
                long weaningTime = expectedWeaning.getTime();
                long daysBeforeWeaning = TimeUnit.DAYS.convert(weaningTime - currentTime, TimeUnit.MILLISECONDS);

                model.addRow(new Object[]{eartag, batch_number, actualFarrowing, dateFormat.format(expectedWeaning), daysBeforeWeaning});
            }

            if (CLOSE_WEANING_TABLE != null) {
                CLOSE_WEANING_TABLE.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "An error occurred while retrieving not weaned eartags.");
        }
    }

    private void WEANING_SUBMIT() {

        try {
            Date selectedDate = WEANING_CALENDAR.getDate();
            String dateString = new java.sql.Date(selectedDate.getTime()).toString();

            String sql = "INSERT INTO weaning_records (eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw, culled) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

            pst = conn.prepareStatement(sql);
            pst.setString(1, WEANING_EARTAG.getText());
            pst.setString(2, WEANING_BATCH_NUMBER_CLOSE.getText());

            pst.setString(3, dateString);
            pst.setString(4, WEANING_MALE.getText());
            pst.setString(5, WEANING_FEMALE.getText());
            pst.setString(6, WEANING_TOTAL.getText());
            pst.setDouble(7, Double.parseDouble(WEANING_AW.getText()));
            pst.setBoolean(8, false);

            pst.execute();

            String farrowingUpdate = "UPDATE breeding SET sow_status = 2 WHERE eartag = ?";

            pst = conn.prepareStatement(farrowingUpdate);
            pst.setString(1, WEANING_EARTAG.getText());
            pst.executeUpdate();

            JOptionPane.showMessageDialog(null, FARROWING_EARTAG.getText() + " EARTAG WEANING DETAILS ARE RECORDED");

            WEANING_RETRIEVE_DETAILS();
//            BREEDING_RETRIEVE_BREEDING_DETAILS();

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

    private void WEANING_FETCH_VALUE_FROM_BATCH_NUMBER() {
        try {
            String sql = "SELECT DISTINCT bnumber FROM register_sow";
            pst = conn.prepareStatement(sql);
            rs = pst.executeQuery();

            while (rs.next()) {
                String batchNumber = rs.getString("bnumber");
                WEANING_DROPDOWN_FOR_BATCH_NUMBER.addItem(batchNumber);
            }

            WEANING_DROPDOWN_FOR_BATCH_NUMBER.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {

                    Object selectedBatch = WEANING_DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();
                    if (selectedBatch.equals("All Batch")) {
                        WEANING_RETRIEVE_DETAILS_ALL_DETAILS();
                    } else {
                        WEANING_RETRIEVE_BY_BATCH();
                    }
                }
            });
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void WEANING_RETRIEVE_DETAILS() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            String query = "SELECT eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw FROM weaning_records WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(WEANING_EARTAG.getText()));
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Batch");

            model.addColumn("Actual");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("Total");
            model.addColumn("AW");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                String batch_number = rs.getString("batch_number");

                Date weaning_actualdate = rs.getDate("weaning_actualdate");
                int male_piglets = rs.getInt("male_piglets");
                int female_piglets = rs.getInt("female_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");

                model.addRow(new Object[]{eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
            }

            WEANING_MAIN_TABLE.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void WEANING_RETRIEVE_DETAILS_ALL_DETAILS() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            String query = "SELECT eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw "
                    + "FROM weaning_records "
                    + "WHERE culled = false "
                    + "AND (eartag, weaning_id) IN ("
                    + "   SELECT eartag, MAX(weaning_id) "
                    + "   FROM weaning_records "
                    + "   GROUP BY eartag "
                    + ") "
                    + "AND NOT EXISTS ("
                    + "   SELECT 1 "
                    + "   FROM breeding "
                    + "   WHERE weaning_records.eartag = breeding.eartag"
                    + ")";

            pst = conn.prepareStatement(query);

            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Batch");

            model.addColumn("Actual");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("Total");
            model.addColumn("AW");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                String batch_number = rs.getString("batch_number");
                Date weaning_actualdate = rs.getDate("weaning_actualdate");
                int male_piglets = rs.getInt("male_piglets");
                int female_piglets = rs.getInt("female_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");

                model.addRow(new Object[]{eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
            }

            WEANING_MAIN_TABLE.setModel(model);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

    private void WEANING_RETRIEVE_BY_BATCH() {
        try {
            DefaultTableModel model = new DefaultTableModel();
            String selectedBatchNumber = (String) WEANING_DROPDOWN_FOR_BATCH_NUMBER.getSelectedItem();
            String query = "SELECT eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw "
                    + "FROM weaning_records "
                    + "WHERE culled = false "
                    + "AND batch_number = ? "
                    + "AND (eartag, weaning_id) IN ("
                    + "   SELECT eartag, MAX(weaning_id) "
                    + "   FROM weaning_records "
                    + "   GROUP BY eartag "
                    + ") "
                    + "AND NOT EXISTS ("
                    + "   SELECT 1 "
                    + "   FROM breeding "
                    + "   WHERE weaning_records.eartag = breeding.eartag"
                    + ")";

            pst = conn.prepareStatement(query);
            pst.setString(1, selectedBatchNumber);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Batch");

            model.addColumn("Actual");
            model.addColumn("Male");
            model.addColumn("Female");
            model.addColumn("Total");
            model.addColumn("AW");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                String batch_number = rs.getString("batch_number");
                Date weaning_actualdate = rs.getDate("weaning_actualdate");
                int male_piglets = rs.getInt("male_piglets");
                int female_piglets = rs.getInt("female_piglets");
                int total_piglets = rs.getInt("total_piglets");
                double aw = rs.getDouble("aw");

                model.addRow(new Object[]{eartag, batch_number, weaning_actualdate, male_piglets, female_piglets, total_piglets, aw});
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

            String query = "SELECT boar_used, breeding_date, expected_farrowing, rebreed FROM breeding WHERE eartag = ?";
            pst = conn.prepareStatement(query);
            pst.setInt(1, Integer.parseInt(PERFORMANCE_SEARCHFIELD.getText()));
            rs = pst.executeQuery();

//            model.addColumn("EARTAG");
            model.addColumn("Boar Used");
            model.addColumn("Breeding Date");
            model.addColumn("Expected");
            model.addColumn("Rebreed");

            while (rs.next()) {
//                int eartag = rs.getInt("eartag");
                Date breeding_date = rs.getDate("breeding_date");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                int isFarrowed = rs.getInt("rebreed");
                String status = isFarrowed == 1 ? "rebreed" : "no";

                model.addRow(new Object[]{boar_used, breeding_date, expected_farrowing, status});
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
            String warningSowsQuery = "SELECT DISTINCT eartag, "
                    + "CASE "
                    + "    WHEN total_piglets < 7 THEN 'Total piglets less than 7' "
                    + "    WHEN mortality > 0 THEN 'Mortality greater than 0' "
                    + "    WHEN remarks IS NOT NULL AND remarks <> '' THEN 'Remarks not empty' "
                    + "    WHEN (farrowing_actualdate < farrowing_duedate - INTERVAL 3 DAY "
                    + "          OR farrowing_actualdate > farrowing_duedate + INTERVAL 3 DAY) THEN 'Actual date outside the 3-day marking' "
                    + "END AS criteria "
                    + "FROM farrowing_records "
                    + "WHERE (total_piglets < 7 "
                    + "    OR mortality > 0 "
                    + "    OR (remarks IS NOT NULL AND remarks <> '') "
                    + "    OR (farrowing_actualdate < farrowing_duedate - INTERVAL 3 DAY "
                    + "        OR farrowing_actualdate > farrowing_duedate + INTERVAL 3 DAY)) "
                    + "    AND culled = 0";

            pst = conn.prepareStatement(warningSowsQuery);
            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Description");

            while (rs.next()) {
                int eartag = rs.getInt("eartag");
                String criteria = rs.getString("criteria");

                model.addRow(new Object[]{eartag, criteria});
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

    private void BREEDING_RETRIEVE_SOW_BY_CLASSIFICATION() {

        try {
            DefaultTableModel model = new DefaultTableModel();
//            String selected = (String) LIST_OF_SOW_DROPDOWN.getSelectedItem();

            String query = "SELECT b.eartag, b.boar_used, b.breeding_date, b.expected_farrowing, b.breeding_type, b.lactate, b.rebreed, b.sow_status, b.parity AS highest_parity, rs.penbuilding, rs.penroom, rs.assigned_employee "
                    + "FROM breeding b "
                    + "LEFT JOIN register_sow rs ON b.eartag = rs.eartag "
                    + "WHERE (b.eartag, b.parity) IN ( "
                    + "    SELECT eartag, MAX(parity) "
                    + "    FROM breeding "
                    + "    GROUP BY eartag "
                    + ") "
                    + "AND b.sow_status = ?";

            pst = conn.prepareStatement(query);

            if (LIST_OF_SOW_DROPDOWN.getSelectedItem() == "Breeding") {
                pst.setInt(1, 0);
            } else if (LIST_OF_SOW_DROPDOWN.getSelectedItem() == "Farrowing") {
                pst.setInt(1, 1);
            } else if (LIST_OF_SOW_DROPDOWN.getSelectedItem() == "Weaning") {
                pst.setInt(1, 2);
            } else if (LIST_OF_SOW_DROPDOWN.getSelectedItem() == "Culled") {
                pst.setInt(1, 3);
            } else {
                BREEDING_RETRIEVE_BREEDING_DETAILS();
            }

            rs = pst.executeQuery();

            model.addColumn("Eartag");
            model.addColumn("Boar");
            model.addColumn("Date");
            model.addColumn("Expected");

            model.addColumn("Type");
            model.addColumn("Lactate");

            model.addColumn("Building");
            model.addColumn("Room");
            model.addColumn("Employee");
            model.addColumn("Parity");

            model.addColumn("Rebreed");
            model.addColumn("Sow Status");

            while (rs.next()) {

                int breedingEartag = rs.getInt("eartag");
                Date breeding_date = rs.getDate("breeding_date");
                String boar_used = rs.getString("boar_used");
                Date expected_farrowing = rs.getDate("expected_farrowing");
                String breeding_type = rs.getString("breeding_type");
                String lactate = rs.getString("lactate");

                boolean rebreed = rs.getBoolean("rebreed");
                String setStatusForRebreedStatus = rebreed ? "yes" : "no";

                int parity = rs.getInt("highest_parity");
                String penbuilding = rs.getString("penbuilding");
                String penroom = rs.getString("penroom");
                String assignedEmployee = rs.getString("assigned_employee");

                int sowStatus = rs.getInt("sow_status");
                String sowStatusString = "";
                sowStatusString = switch (sowStatus) {
                    case 0 ->
                        "Breeding";
                    case 1 ->
                        "Farrowed";
                    case 2 ->
                        "Weaned";
                    case 3 ->
                        "Culled";
                    default ->
                        "Unknown";
                };

                model.addRow(new Object[]{
                    breedingEartag, boar_used, breeding_date, expected_farrowing, breeding_type,
                    lactate, penbuilding, penroom, assignedEmployee, parity,
                    setStatusForRebreedStatus, sowStatusString
                });
            }

            if (BREEDING_TABLE != null) {
                BREEDING_TABLE.setModel(model);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }

}
