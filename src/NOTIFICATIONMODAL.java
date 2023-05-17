
import java.awt.BorderLayout;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;



/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

/**
 *
 * @author Reydel
 */
public class NOTIFICATIONMODAL extends JFrame implements Runnable {

    /**
     * Creates new form NOTIFICATIONMODAL
     */
    
    Connection conn = null;
    PreparedStatement pst = null;
    ResultSet rs = null;
    
    
    public NOTIFICATIONMODAL() {
        setUndecorated(true);

        conn = DBConnection.getConnection();
        initComponents();
        
        
        FETCH_NOTIFICATION_TO_JLIST();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        NOTIFICATION_CONTAINER_SCROLL_PANE = new javax.swing.JScrollPane();
        NOTIFICATION_CONTAINER = new javax.swing.JList<>();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        NOTIFICATION_CONTAINER_SCROLL_PANE.setOpaque(false);

        NOTIFICATION_CONTAINER.setModel(new javax.swing.AbstractListModel<String>() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public String getElementAt(int i) { return strings[i]; }
        });
        NOTIFICATION_CONTAINER_SCROLL_PANE.setViewportView(NOTIFICATION_CONTAINER);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(NOTIFICATION_CONTAINER_SCROLL_PANE, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(NOTIFICATION_CONTAINER_SCROLL_PANE, javax.swing.GroupLayout.DEFAULT_SIZE, 498, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(NOTIFICATIONMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NOTIFICATIONMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NOTIFICATIONMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NOTIFICATIONMODAL.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NOTIFICATIONMODAL().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JList<String> NOTIFICATION_CONTAINER;
    private javax.swing.JScrollPane NOTIFICATION_CONTAINER_SCROLL_PANE;
    // End of variables declaration//GEN-END:variables
    
        private void initializeNotificationTabs() {
         JTabbedPane tabbedPane = new JTabbedPane();
         
         DefaultListModel<String> farrowingListModel = fetchFarrowingNotifications();
         JScrollPane farrowingScrollPane = createScrollPane(farrowingListModel);
         tabbedPane.addTab("Farrowing", farrowingScrollPane);
         
         DefaultListModel<String> warningListModel = fetchWarningNotifications();
         JScrollPane warningScrollPane = createScrollPane(warningListModel);
         tabbedPane.addTab("Warning", warningScrollPane);

         DefaultListModel<String> weaningListModel = fetchWeaningNotifications();
         JScrollPane weaningScrollPane = createScrollPane(weaningListModel);
         tabbedPane.addTab("Weaning", weaningScrollPane);

         NOTIFICATION_CONTAINER_SCROLL_PANE.setViewportView(tabbedPane);
//         updateNotificationCounter(warningListModel.size() + farrowingListModel.size() + weaningListModel.size());
     }

        private DefaultListModel<String> fetchWarningNotifications() {
            DefaultListModel<String> warningListModel = new DefaultListModel<>();

            try {
                String sql = "SELECT eartag, notification_message FROM notifications ORDER BY id DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String eartag = rs.getString("eartag");
                    String notificationMessage = rs.getString("notification_message");

                    if (notificationMessage.contains("remarks") || notificationMessage.contains("mortality") || notificationMessage.contains("piglet count")) {
                        String notification = "Eartag: " + eartag + " - " + notificationMessage;
                        warningListModel.addElement(notification);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

            return warningListModel;
        }

        private DefaultListModel<String> fetchFarrowingNotifications() {
            DefaultListModel<String> farrowingListModel = new DefaultListModel<>();

            try {
                String sql = "SELECT eartag, notification_message FROM notifications ORDER BY id DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String eartag = rs.getString("eartag");
                    String notificationMessage = rs.getString("notification_message");

                    if (notificationMessage.contains("Farrowing")) {
                        String notification = "Eartag: " + eartag + " - " + notificationMessage;
                        farrowingListModel.addElement(notification);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

            return farrowingListModel;
        }

        private DefaultListModel<String> fetchWeaningNotifications() {
            DefaultListModel<String> weaningListModel = new DefaultListModel<>();

            try {
                String sql = "SELECT eartag, notification_message FROM notifications ORDER BY id DESC";
                pst = conn.prepareStatement(sql);
                rs = pst.executeQuery();

                while (rs.next()) {
                    String eartag = rs.getString("eartag");
                    String notificationMessage = rs.getString("notification_message");

                    if (notificationMessage.contains("weaning")) {
                        String notification = "Eartag: " + eartag + " - " + notificationMessage;
                        weaningListModel.addElement(notification);
                    }
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, ex);
            }

            return weaningListModel;
        }

     private JScrollPane createScrollPane(DefaultListModel<String> listModel) {
         JList<String> notificationList = new JList<>(listModel);
         JScrollPane scrollPane = new JScrollPane(notificationList);

         return scrollPane;
     }

     private void FETCH_NOTIFICATION_TO_JLIST() {
         initializeNotificationTabs();
     }
    

    @Override
    public void run() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
        
}
