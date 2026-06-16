package stockms;

import stockms.ui.LoginFrame;
import stockms.utils.UIHelper;
import javax.swing.SwingUtilities;

/**
 * ╔══════════════════════════════════════════════════════════╗
 *  Stock Management System — Entry Point
 *  Run this class to start the application.
 *
 *  BEFORE RUNNING:
 *   1. Start MySQL (XAMPP or standalone)
 *   2. Create database:  CREATE DATABASE stockms;
 *   3. Update DBConnection.java with your credentials
 *   4. Add mysql-connector-java JAR to classpath
 * ╚══════════════════════════════════════════════════════════╝
 */
public class Main {

    public static void main(String[] args) {
        UIHelper.setGlobalLook();
        SwingUtilities.invokeLater(() -> new LoginFrame().setVisible(true));
    }
}
