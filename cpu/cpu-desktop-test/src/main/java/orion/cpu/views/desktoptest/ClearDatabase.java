package orion.cpu.views.desktoptest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Удаление всез таблиц базы данных
 * @author sl
 */
public class ClearDatabase {

    static String userid = "postgres", password = "postgres";
    static String url = "jdbc:postgresql://localhost/cpu";
    static String db = "cpu";
    static Connection con = getPostgresqlJDBCConnection();

    public static void main(String[] args) throws Exception {
        Statement statement = con.createStatement();
        ResultSet rs = statement.executeQuery("select table_schema, table_name from information_schema.tables where table_catalog='" + db + "' and table_schema not in ('information_schema', 'pg_catalog')");
        Set<String> tables=new HashSet<String>();
        while (rs.next()) {
            tables.add(rs.getString("table_schema")+"."+rs.getString("table_name"));
        }
        rs.close();
        while(tables.size()>0){
            Iterator<String> it=tables.iterator();
            while(it.hasNext()){
                try{
                    String tname=it.next();
                    statement.executeUpdate("DROP TABLE "+tname);
                    it.remove();
                    System.out.println("Table dropped: "+tname);
                }catch(SQLException ex){
                }
            }
        }
    }

    public static Connection getPostgresqlJDBCConnection() {

        try {
            Class.forName("org.postgresql.Driver");
        } catch (java.lang.ClassNotFoundException e) {
            System.err.print("ClassNotFoundException: ");
            System.err.println(e.getMessage());
        }

        try {
            con = DriverManager.getConnection(url, userid, password);
            System.out.println("Got Connection.");
        } catch (SQLException ex) {
            System.err.println("SQLException: " + ex.getMessage());
        }

        return con;
    }
}
