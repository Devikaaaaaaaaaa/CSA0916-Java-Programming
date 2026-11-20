import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

/** Stores mouse-click coordinates and redraws them after repaint/expose events. */
public class DrawingPad extends JFrame {
    private final List<Point> points = new ArrayList<>();
    private final JLabel count = new JLabel("Clicks: 0");
    private final JPanel canvas = new JPanel() {
        @Override protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLUE);
            for (Point p : points) g.fillOval(p.x - 4, p.y - 4, 8, 8);
        }
    };
    public DrawingPad() {
        super("Mouse Drawing Pad");
        setLayout(new BorderLayout());
        add(count, BorderLayout.NORTH);
        canvas.setBackground(Color.WHITE);
        canvas.addMouseListener(new MouseAdapter() {
            @Override public void mouseClicked(MouseEvent e) {
                points.add(e.getPoint());
                count.setText("Clicks: " + points.size());
                canvas.repaint();
            }
        });
        add(canvas, BorderLayout.CENTER);
        setSize(600,400); setDefaultCloseOperation(EXIT_ON_CLOSE); setLocationRelativeTo(null);
    }
    public static void main(String[] args) { SwingUtilities.invokeLater(() -> new DrawingPad().setVisible(true)); }
}