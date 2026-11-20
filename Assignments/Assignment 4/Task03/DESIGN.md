# Task 3 – From Sketch to Layout

## Nested panel structure
```text
JFrame (BorderLayout)
├── Title JLabel (NORTH)
├── Center JPanel (BorderLayout)
│   ├── Search JPanel (FlowLayout.LEFT, NORTH)
│   │   ├── JTextField, Choice/JComboBox, Search JButton
│   └── JScrollPane containing JList/JTable (CENTER)
└── Bottom JPanel (BorderLayout, SOUTH)
    ├── Navigation JPanel (FlowLayout.CENTER): Prev, Next, Reserve
    └── Status JLabel (SOUTH)
```

BorderLayout lets the results region consume remaining space as the frame grows. FlowLayout preserves natural control sizing in the search and button rows. GridLayout would force equal cell sizes; CardLayout is useful for switching screens, not this single search page.

## Resize test plan
Run at 420×300, 650×440 and 1000×700. Confirm the result scroll area grows/shrinks while controls remain usable. Capture screenshots at all three sizes after executing locally.

## Alternative comparison
Nested layouts compose cleanly and adapt to resizing; their structure is easy to understand but may require several panels. GridBagLayout can produce a more precise responsive form but needs constraints and more code. Absolute positioning is fragile because controls do not adapt to font, platform or window size.
