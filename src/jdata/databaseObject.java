
package jdata;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.*;
import javax.swing.JOptionPane;

/**
 * @author oozzal
 */
public final class databaseObject {

    public String appName = "jData";
    public String host = "jdbc:mysql://localhost/jData";
    public String driver = "com.mysql.jdbc.Driver";
    public Connection con = null;
    public String user = "jData_user";
    public String pass  = "jData_pass";
    public String query = null;
    public PreparedStatement ps = null;
    public Statement stmnt = null;
    public ResultSet rs = null;
    public String downloadFolder = new File("test.txt").getAbsolutePath();
    public String filePath = null;
    public String fileName = null;
    
    public databaseObject(String userName, String password) {
            this.user = userName;
            this.pass = password;
    }

    public databaseObject() {
        loadDriver();
        getConnection();
    }

    public boolean loadDriver() {
        try {
            Class.forName(this.driver);  //load driver
            return true;
        } catch(ClassNotFoundException e) { //sql service not started
            //e.printStackTrace();
            return false;
        }
    }

    public boolean getConnection() {
         try {
            this.con = DriverManager.getConnection(this.host,this.user,this.pass);
            return true;
        } catch(SQLException e) {//may be first execution
            //e.printStackTrace();
            return false;
        }
    }

    public boolean createUser() {
        boolean check;
        this.query = "INSERT INTO "+ this.appName +".users(username,password) VALUES('" + this.user + "', AES_ENCRYPT('" + this.user + "','"+ this.pass +"'))";
        check = this.execute_update();
        if(!check) return check;
        this.query = "CREATE TABLE IF NOT EXISTS "+ this.appName +"." + this.user + "_files(ID INT NOT NULL PRIMARY KEY AUTO_INCREMENT," +
                             "name VARCHAR(100) NOT NULL UNIQUE KEY,file LONGBLOB NOT NULL)";
        check = this.execute_update();
        return check;
    }

    public boolean upload() {
        try{
            this.con.setAutoCommit(false);
            File file = new File(this.filePath);
            this.fileName = file.getName();
            FileInputStream fis = new FileInputStream(file);
            this.ps = con.prepareStatement(this.query);
            this.ps.setString(1, this.fileName);
            this.ps.setBinaryStream(2, fis, (int) file.length());
            this.ps.executeUpdate();
            con.commit();
            this.ps.close();
            fis.close();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        } catch(FileNotFoundException e){
            e.printStackTrace();
            return false;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean download() {
        try{
            this.ps = con.prepareStatement(this.query);
            this.rs = this.ps.executeQuery();
            while (this.rs.next()) {
                //save blob as an image
                FileOutputStream fos = new FileOutputStream(this.downloadFolder + this.fileName);

                byte[] buf = new byte[3000];

                InputStream is  = this.rs.getBinaryStream(1);
                int read = 0;
                while ((read = is.read(buf)) > 0) {
                    fos.write(buf, 0, read);
                }
                fos.close();
                is.close();
            }
            this.ps.close();
            return true;
        } catch(SQLException e) {
            e.printStackTrace();
            return false;
        } catch(IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean execute_update() {
        try {
            this.ps = this.con.prepareStatement(this.query);
            this.ps.executeUpdate();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(databaseObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean checkInputError(String check){
        return check.contains(" ");
    }

    public boolean checkLogin() {
        String check = this.user.concat(this.pass);
        if(checkInputError(check)) {
            return false;
        }
        try {
            this.stmnt = this.con.createStatement(); //Statement is an interface for creating an object that executes statements
            this.query = "SELECT * FROM users WHERE password=AES_ENCRYPT('" + this.user + "','" + this.pass + "')";
            this.rs = this.stmnt.executeQuery(this.query);	//here users is the table name
            if(this.rs.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException ex) {
            Logger.getLogger(databaseObject.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public void showDialog(String message) {
        JOptionPane.showMessageDialog(null, message);
    }

    public void aboutjData() {
        javax.swing.JOptionPane.showMessageDialog(null, "" + this.appName + " Copyright � 2010", "About " + this.appName + "",1);
    }

}
