import java.sql.*;
public class WalletTransfer {
    public static void transfer(Connection c,long fromId,long toId,double amount)throws SQLException{
        if(amount<=0 || !Double.isFinite(amount))throw new IllegalArgumentException("Amount must be positive");
        boolean old=c.getAutoCommit();c.setAutoCommit(false);
        try{
            try(PreparedStatement debit=c.prepareStatement("UPDATE wallet SET balance=balance-? WHERE wallet_id=? AND balance>=?");
                PreparedStatement credit=c.prepareStatement("UPDATE wallet SET balance=balance+? WHERE wallet_id=?")){
                debit.setDouble(1,amount);debit.setLong(2,fromId);debit.setDouble(3,amount);
                if(debit.executeUpdate()!=1)throw new SQLException("Source wallet missing or insufficient funds");
                credit.setDouble(1,amount);credit.setLong(2,toId);
                if(credit.executeUpdate()!=1)throw new SQLException("Destination wallet not found");
            }
            c.commit();
        }catch(SQLException ex){try{c.rollback();}catch(SQLException rollback){ex.addSuppressed(rollback);}throw ex;}
        finally{c.setAutoCommit(old);}
    }
    public static boolean safeLogin(Connection c,String username,String password)throws SQLException{
        String sql="SELECT user_id FROM app_user WHERE username=? AND password_hash=?";
        try(PreparedStatement p=c.prepareStatement(sql)){p.setString(1,username);p.setString(2,password);try(ResultSet r=p.executeQuery()){return r.next();}}
    }
}