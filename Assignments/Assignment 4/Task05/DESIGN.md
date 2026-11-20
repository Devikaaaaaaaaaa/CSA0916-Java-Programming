# Task 5 – Same Screen, Different Event Styles

Run `java CurrencyConverter lambda`, or pass `implements`, `inner`, or `anonymous` to compare approaches. Enter in the amount field also converts. WindowAdapter handles close events.

| Style | Readability | Reuse / access |
|---|---|---|
| Frame implements ActionListener | Simple for one small frame, but frame accumulates listener responsibilities | Easy field access; less reusable |
| Named inner class | Clear named handler and separate method | Direct access to outer fields; reusable within frame |
| Anonymous inner class | Keeps handler near button registration | Direct outer access; verbose and less reusable |
| Lambda | Concise for one-method listener | Captures frame fields; concise, but best for short handlers |

A method reference is concise when a compatible method already exists (for example, `convertButton.addActionListener(e -> convert())` can be replaced with a small adapter method reference only if its signature matches). A shared Swing Action is preferable when multiple controls invoke the same command or need shared enabled/disabled state. The supplied demo uses a fixed illustrative USD/INR rate of 83; it is not a live exchange rate.
