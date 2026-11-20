import java.sql.*;
import java.util.*;
public class StudentDao implements Dao<Student,Integer> {
    private final Connection connection;
    public StudentDao(Connection connection){this.connection=connection;}
    public int insert(Student s)throws SQLException{
        String sql="INSERT INTO student(student_id,register_no,full_name,email) VALUES(?,?,?,?)";
        try(PreparedStatement p=connection.prepareStatement(sql)){bind(p,s);return p.executeUpdate();}
    }
    private void bind(PreparedStatement p,Student s)throws SQLException{
        p.setInt(1,s.studentId());p.setString(2,s.registerNo());p.setString(3,s.fullName());p.setString(4,s.email());
    }
    private Student map(ResultSet r)throws SQLException{return new Student(r.getInt("student_id"),r.getString("register_no"),r.getString("full_name"),r.getString("email"));}
    public Optional<Student> findById(Integer id)throws SQLException{
        try(PreparedStatement p=connection.prepareStatement("SELECT student_id,register_no,full_name,email FROM student WHERE student_id=?")){p.setInt(1,id);try(ResultSet r=p.executeQuery()){return r.next()?Optional.of(map(r)):Optional.empty();}}
    }
    public List<Student> findAll()throws SQLException{
        List<Student> out=new ArrayList<>();try(PreparedStatement p=connection.prepareStatement("SELECT student_id,register_no,full_name,email FROM student");ResultSet r=p.executeQuery()){while(r.next())out.add(map(r));}return out;
    }
    public boolean update(Student s)throws SQLException{
        String sql="UPDATE student SET register_no=?,full_name=?,email=? WHERE student_id=?";
        try(PreparedStatement p=connection.prepareStatement(sql)){p.setString(1,s.registerNo());p.setString(2,s.fullName());p.setString(3,s.email());p.setInt(4,s.studentId());return p.executeUpdate()==1;}
    }
    public boolean delete(Integer id)throws SQLException{
        try(PreparedStatement p=connection.prepareStatement("DELETE FROM student WHERE student_id=?")){p.setInt(1,id);return p.executeUpdate()==1;}
    }
}