import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/** Swing/JDBC sample. Configure MySQL credentials using environment variables DB_URL, DB_USER, DB_PASSWORD. */
public class EventRegistrationApp extends JFrame {
    private final JTextField student = new JTextField(12);
    private final JComboBox<String> event = new JComboBox<>(new String[]{"Hackathon","Tech Talk","Cultural Fest"});
    private final DefaultTableModel model = new DefaultTableModel(new String[]{"Student ID","Event"},0);
    private final JTable table = new JTable(model);
    private final JLabel status = new JLabel("Ready");
    public EventRegistrationApp() {
        super("Campus Event Registration");
        setLayout(new BorderLayout());
        JMenuBar bar = new JMenuBar(); JMenu file = new JMenu("File");
        JMenuItem refresh = new JMenuItem("Refresh"); refresh.addActionListener(e -> loadAsync());
        JMenuItem exit = new JMenuItem("Exit"); exit.addActionListener(e -> dispose());
        file.add(refresh); file.add(exit); bar.add(file); setJMenuBar(bar);
        JPanel top = new JPanel(); top.add(new JLabel("Student ID")); top.add(student);
        top.add(new JLabel("Event")); top.add(event);
        JButton register = new JButton("Register"); register.addActionListener(e -> registerAsync());
        JButton filter = new JButton("Filter"); filter.addActionListener(e -> loadAsync());
        top.add(register); top.add(filter); add(top,BorderLayout.NORTH);
        add(new JScrollPane(table),BorderLayout.CENTER); add(status,BorderLayout.SOUTH);
        setSize(720,420); setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null);
        loadAsync();
    }
    private Connection connect() throws SQLException {
        String url=System.getenv().getOrDefault("DB_URL","jdbc:mysql://localhost:3306/campus_events");
        return DriverManager.getConnection(url,System.getenv().getOrDefault("DB_USER","root"),System.getenv().getOrDefault("DB_PASSWORD",""));
    }
    private void registerAsync() {
        String id=student.getText().trim(), ev=(String)event.getSelectedItem();
        if (!id.matches("[A-Za-z0-9-]{3,20}")) { status.setText("Enter a valid student ID."); return; }
        status.setText("Registering…");
        new SwingWorker<Void,Void>() {
            protected Void doInBackground() throws Exception {
                try(Connection c=connect(); PreparedStatement p=c.prepareStatement("INSERT INTO registrations(student_id,event_name) VALUES (?,?)")) {
                    p.setString(1,id); p.setString(2,ev); p.executeUpdate();
                } return null;
            }
            protected void done(){ try{get();status.setText("Registration successful.");loadAsync();}
                catch(Exception ex){status.setText("Registration failed (possibly duplicate): "+ex.getCause().getMessage());} }
        }.execute();
    }
    private void loadAsync() {
        String selected=(String)event.getSelectedItem(); status.setText("Loading…");
        new SwingWorker<java.util.List<Object[]>,Void>() {
            protected java.util.List<Object[]> doInBackground() throws Exception {
                java.util.List<Object[]> rows=new java.util.ArrayList<>();
                try(Connection c=connect(); PreparedStatement p=c.prepareStatement(
                    "SELECT student_id,event_name FROM registrations WHERE (? IS NULL OR event_name=?) ORDER BY student_id LIMIT 100 OFFSET 0")) {
                    p.setString(1,selected); p.setString(2,selected);
                    try(ResultSet rs=p.executeQuery()){while(rs.next())rows.add(new Object[]{rs.getString(1),rs.getString(2)});}
                } return rows;
            }
            protected void done(){try{model.setRowCount(0);for(Object[] row:get())model.addRow(row);status.setText("Loaded "+model.getRowCount()+" registrations (first page).");}
                catch(Exception ex){status.setText("Database unavailable: "+ex.getMessage());}}
        }.execute();
    }
    public static void main(String[] args){SwingUtilities.invokeLater(()->new EventRegistrationApp().setVisible(true));}
}