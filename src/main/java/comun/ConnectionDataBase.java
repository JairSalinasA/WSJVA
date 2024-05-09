package comun;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class ConnectionDataBase {
    
    private Connection conexion;
    private Statement statem;
    private TipoBaseDatos TipoBD;
    private String Error = "";
    private AutoCommit AutoCommit;
    private String URL,  User,  Pass,  DB,  Puerto;
            
    public ConnectionDataBase(){
       
        this.URL = "85.187.158.28";
        this.User = "postgres";
        this.Pass = "L4g0s%dE%M0ren0";
        this.DB = "culagos";
        this.Puerto = "5432";
        
        TipoBD = TipoBaseDatos.Postgresql;
        AutoCommit = AutoCommit.False;
    }
    
    public void OpenConnection(){
        conexion = ConexionBD(URL,User,Pass,DB,Puerto);
        statem = CreateStatement();
    }
    
    public Boolean getConexion(){
        return conexion != null;
    }
    
    public ResultSet getQuery(String Query){
        
        ResultSet rset = null;
        try{
             rset = getDataConnection(Query);
            
        }catch(Exception ex){}
        
        return rset;
    }
    
    public String executeQuery(String Query){
        try {
            statem.executeUpdate(Query);
            
            return "1";
        } catch (SQLException ex) {
            return ex.toString();
        }
    }
    
    public void Commit() throws SQLException{
        conexion.commit();
    }
    
    public void Rollback() throws SQLException{
        conexion.rollback();
    }
    
    private Connection ConexionBD(String URL, String User, String Pass, String DB, String Puerto){
        try{
            String BaseDeDatos = "";
            
            if(TipoBD == TipoBaseDatos.Oracle){
                Class.forName("oracle.jdbc.OracleDriver");
                BaseDeDatos = "jdbc:oracle:thin:@"+URL+":"+Puerto+":"+DB+"";
            }  
            
            if(TipoBD == TipoBaseDatos.Mysql){
                Class.forName("com.mysql.jdbc.Driver");
                BaseDeDatos = "jdbc:mysql://"+URL+":"+Puerto+"/"+DB;
            }
            
            if(TipoBD == TipoBaseDatos.Sql){
                Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                BaseDeDatos = "jdbc:sqlserver://"+URL+":"+Puerto+";databaseName="+DB; 
            }
            
            if(TipoBD == TipoBaseDatos.Postgresql){
                Class.forName("org.postgresql.Driver");
                BaseDeDatos = "jdbc:postgresql://"+URL+":"+Puerto+"/"+DB; 
            }
            
            Connection conexion = DriverManager.getConnection(BaseDeDatos,User, Pass);
            
            if(AutoCommit == AutoCommit.False)
                conexion.setAutoCommit(false);
            else
                conexion.setAutoCommit(true);
            
            return conexion;
            
        }catch(Exception ex){
            Error = ex.toString();
            return null;
        }
    }
    
    public String ErrorConnection(){
        return Error;
    }    
    
    private ResultSet getDataConnection(String Query) throws SQLException{
        
        ResultSet rset = null;
        
        if (conexion != null) 
            rset = executeQuery(conexion, Query);
        
        else 
            System.out.println("Conexion fallida!");
        
        return rset;
    }
    
    private ResultSet executeQuery(Connection conexion, String Query) throws SQLException{
        
        Statement stmt = conexion.createStatement();
        ResultSet rset = stmt.executeQuery(Query);
        
        return rset;
    }
    
    private Statement CreateStatement(){
        try{
            return conexion.createStatement();
            
        }catch(Exception ex){return null;}
    }
    
    public void CloseConnection() throws SQLException{
        statem.close();
        conexion.close();
    }
    
    public enum TipoBaseDatos{
        Oracle, Mysql, Sql, Postgresql;
    }
    
    public enum AutoCommit{
        True, False
    }
    
}
