# Task 4 – Event-Handling Forensics

A handler with a misspelled method name is not an implementation of the listener interface method, so the event dispatcher never calls it. A label reference that is local to another scope or not updated on the EDT will also fail to show the new count. Minimising/restoring forces repaint; drawing directly only inside the click callback loses marks because pixels are not persistent state.

The repair stores each click as a Point, updates the count label, calls repaint(), and paints every stored point in paintComponent(). `@Override` asks the compiler to verify that the method truly overrides a superclass/interface method, catching misspellings early.

Point storage uses O(n) memory and redraws O(n) dots per repaint. An off-screen BufferedImage also uses O(width×height) pixel memory; drawing new marks can be fast and repaint can blit the image in O(area), but resizing and image lifecycle require care. For this simple pad, storing Points is clear and resolution-independent.
