# Task 2 – Requirement to Event-Driven Design

## Event table
| Source | Event object | Listener | Action |
|---|---|---|---|
| Department JComboBox | ItemEvent / ActionEvent | ItemListener / ActionListener | Refresh doctor choices |
| Book JButton | ActionEvent | ActionListener | Validate and submit booking |
| Window | WindowEvent | WindowListener / WindowAdapter | Confirm or dispose on close |
| Patient JTextField | ActionEvent | ActionListener | Optional submit on Enter |

## Component containment tree
```text
JFrame AppointmentDesk (BorderLayout)
└── JPanel form (GridLayout 3x2)
    ├── JLabel Department + JComboBox
    ├── JLabel Doctor + JComboBox
    ├── JLabel Patient + JTextField
    └── JButton Book + JLabel Status
```

## Pseudocode
Department changed: read selected department; clear doctor combo; fetch doctors for department; add each doctor; select first if available.
Book: trim patient name; ensure name, department and doctor are selected; validate appointment date/time; call appointment service; show confirmation or actionable error.

## Lifecycle trace
Construct frame → create components → register listeners → load initial doctor list → pack and show → user event dispatches registered listener → handler validates/updates model and view → closing event is received → dispose resources and exit when appropriate.

## Applet comparison
An applet uses container callbacks `init()`, `start()`, `paint()`, `stop()`, and `destroy()`. A desktop Swing window is created explicitly and responds to registered event listeners; it does not use the applet lifecycle.

## Database load strategy
Querying the database on every department change gives fresher data but adds latency and repeated round trips. Loading a department-to-doctor Map once makes lookups approximately O(1) average and reduces database calls, but consumes O(D) memory and can become stale. Use caching with refresh/invalidation when freshness matters.
