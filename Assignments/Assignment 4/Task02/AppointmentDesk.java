import javax.swing.*;
import java.awt.*;
import java.util.*;

/** Event-driven appointment desk prototype; database access is represented by sample data. */
public class AppointmentDesk extends JFrame {
    private final JComboBox<String> department = new JComboBox<>(new String[]{"Cardiology","Dermatology","Paediatrics"});
    private final JComboBox<String> doctor = new JComboBox<>();
    private final JTextField patient = new JTextField(16);
    private final JLabel status = new JLabel("Choose a department and doctor.");
    private final Map<String, java.util.List<String>> doctors = Map.of(
        "Cardiology", java.util.List.of("Dr. Rao","Dr. Iyer"),
        "Dermatology", java.util.List.of("Dr. Meena","Dr. Kumar"),
        "Paediatrics", java.util.List.of("Dr. Ali","Dr. Priya"));
    public AppointmentDesk() {
        super("Hospital Appointment Desk");
        setLayout(new GridLayout(0,2,8,8));
        add(new JLabel("Department")); add(department);
        add(new JLabel("Doctor")); add(doctor);
        add(new JLabel("Patient name")); add(patient);
        JButton book = new JButton("Book appointment");
        add(book); add(status);
        department.addActionListener(e -> refreshDoctors());
        book.addActionListener(e -> book());
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { dispose(); }
        });
        refreshDoctors(); setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        pack(); setLocationRelativeTo(null);
    }
    private void refreshDoctors() {
        doctor.removeAllItems();
        for (String name : doctors.getOrDefault((String)department.getSelectedItem(), java.util.List.of()))
            doctor.addItem(name);
    }
    private void book() {
        String name = patient.getText().trim();
        if (name.isEmpty() || doctor.getSelectedItem() == null) {
            status.setText("Enter patient name and select a doctor."); return;
        }
        status.setText("Appointment request recorded for " + name + " with " + doctor.getSelectedItem());
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AppointmentDesk().setVisible(true));
    }
}