import javax.swing.*;
import java.awt.*;

/** Responsive library search mock-up using nested BorderLayout/FlowLayout panels. */
public class LibrarySearch extends JFrame {
    private final JTextField query = new JTextField(22);
    private final JComboBox<String> category = new JComboBox<>(new String[]{"All","Books","Journals","Theses"});
    private final DefaultListModel<String> model = new DefaultListModel<>();
    private final JList<String> results = new JList<>(model);
    private final JLabel status = new JLabel("Ready");
    public LibrarySearch() {
        super("Campus Library Search");
        setLayout(new BorderLayout(8,8));
        JLabel title = new JLabel("Campus Library", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(Font.BOLD, 22f));
        add(title, BorderLayout.NORTH);
        JPanel search = new JPanel(new FlowLayout(FlowLayout.LEFT));
        search.add(new JLabel("Search")); search.add(query);
        search.add(category); JButton find = new JButton("Search"); search.add(find);
        JPanel center = new JPanel(new BorderLayout());
        center.add(search, BorderLayout.NORTH);
        results.setVisibleRowCount(10);
        center.add(new JScrollPane(results), BorderLayout.CENTER);
        add(center, BorderLayout.CENTER);
        JPanel south = new JPanel(new BorderLayout());
        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        for (String label : new String[]{"Prev","Next","Reserve"}) {
            JButton b = new JButton(label); buttons.add(b);
            b.addActionListener(e -> status.setText(label + " selected"));
        }
        south.add(buttons, BorderLayout.CENTER); south.add(status, BorderLayout.SOUTH);
        add(south, BorderLayout.SOUTH);
        find.addActionListener(e -> search());
        query.addActionListener(e -> search());
        setDefaultCloseOperation(EXIT_ON_CLOSE); setMinimumSize(new Dimension(420,300));
        setSize(650,440); setLocationRelativeTo(null);
    }
    private void search() {
        model.clear();
        String q = query.getText().trim();
        if (q.isEmpty()) { status.setText("Enter a search term."); return; }
        model.addElement("Java: The Complete Reference");
        model.addElement("Effective Java");
        model.addElement("Introduction to " + q);
        status.setText("3 sample results (" + category.getSelectedItem() + ")");
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new LibrarySearch().setVisible(true)); }
}