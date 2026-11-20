# Task 1 – GUI Design Review

## Problems in the original design
1. One listener with nested conditions becomes difficult to maintain and test.
2. Business rules inside the UI handler mix presentation and service responsibilities.
3. Comparing strings with `==` compares references; use `equals`/ `equalsIgnoreCase`.
4. Unvalidated text can cause parsing errors or invalid/negative payments.
5. Direct SQL string concatenation risks SQL injection; use JDBC `PreparedStatement`.
6. Database calls and `Thread.sleep()` on the Event Dispatch Thread freeze the interface.
7. No clear success/failure feedback leaves users unsure whether payment completed.
8. Receipt printing is coupled to payment handling and should be a separate action/service.
9. JDBC resources must be closed with try-with-resources; exceptions should be handled meaningfully.

## Corrected structure
- `FeePaymentFrame`: collects input, invokes service, displays feedback.
- `FeeService`: validates amount and student identity; coordinates payment.
- `FeeRepository`: database operations with parameterized SQL.
- `ReceiptService`: generates/prints a receipt after confirmed payment.

## Event-handler sketch
```java
payButton.addActionListener(e -> {
    try {
        PaymentReceipt receipt = feeService.pay(studentIdField.getText(),
                                                  amountField.getText());
        statusLabel.setText("Payment successful");
        receiptButton.setEnabled(true);
    } catch (IllegalArgumentException ex) {
        statusLabel.setText(ex.getMessage());
    } catch (PaymentException ex) {
        statusLabel.setText("Payment could not be completed");
    }
});
```

## Listener comparison
A shared listener centralizes handling but often grows into a large conditional block. Separate listener objects or Swing `Action` objects keep each command cohesive, reusable, testable and easier to enable/disable. For a small form, lambdas are concise; for reusable commands, named Action classes are clearer.

## Design defence (under 100 words)
The redesigned window handles only input and presentation. FeeService owns validation and payment rules, while FeeRepository isolates persistence and uses prepared statements. ReceiptService is independent, so a receipt can be reprinted without charging again. The UI reports validation and service outcomes and never sleeps or performs slow database work on the Event Dispatch Thread. This separation improves testability, security, responsiveness and maintainability.
