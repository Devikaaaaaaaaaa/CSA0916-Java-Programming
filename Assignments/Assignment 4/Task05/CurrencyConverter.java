import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/** Four equivalent listener styles are selectable for comparison. Demo rate: USD to INR = 83. */
public class CurrencyConverter extends JFrame {
    private final JTextField amount = new JTextField("10", 10);
    private final JComboBox<String> from = new JComboBox<>(new String[]{"USD","INR"});
    private final JComboBox<String> to = new JComboBox<>(new String[]{"INR","USD"});
    private final JLabel result = new JLabel("Result appears here");
    public CurrencyConverter(String style) {
        super("Currency Converter — " + style);
        JPanel form = new JPanel(new FlowLayout());
        form.add(amount); form.add(from); form.add(new JLabel("to")); form.add(to);
        JButton convert = new JButton("Convert");
        form.add(convert); form.add(result); add(form);
        Runnable action = this::convert;
        switch (style) {
            case "implements": convert.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ action.run(); }}); break;
            case "inner": convert.addActionListener(new ConvertHandler()); break;
            case "anonymous": convert.addActionListener(new ActionListener(){ public void actionPerformed(ActionEvent e){ convert(); }}); break;
            default: convert.addActionListener(e -> convert());
        }
        amount.addActionListener(e -> convert());
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter(){ @Override public void windowClosing(WindowEvent e){ dispose(); }});
        setSize(520,130); setLocationRelativeTo(null);
    }
    private class ConvertHandler implements ActionListener { public void actionPerformed(ActionEvent e){ convert(); } }
    private void convert() {
        try {
            double value = Double.parseDouble(amount.getText().trim());
            if (!Double.isFinite(value) || value < 0) throw new NumberFormatException();
            double usd = from.getSelectedItem().equals("USD") ? value : value / 83.0;
            double answer = to.getSelectedItem().equals("USD") ? usd : usd * 83.0;
            String message = String.format("%.2f %s = %.2f %s", value, from.getSelectedItem(), answer, to.getSelectedItem());
            result.setText(message); JOptionPane.showMessageDialog(this, message, "Conversion", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            result.setText("Enter a valid non-negative amount.");
            JOptionPane.showMessageDialog(this, "Please enter a valid non-negative number.", "Invalid amount", JOptionPane.ERROR_MESSAGE);
        }
    }
    public static void main(String[] args) {
        String style = args.length == 0 ? "lambda" : args[0].toLowerCase();
        SwingUtilities.invokeLater(() -> new CurrencyConverter(style).setVisible(true));
    }
}