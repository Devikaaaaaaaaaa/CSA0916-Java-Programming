# Task A5 / B2 – Transactions and SQL injection

Transfer ₹500 in one transaction: disable auto-commit; debit source only where balance >= 500; require exactly one affected row; credit destination; require exactly one affected row; commit. On any exception rollback, then restore prior auto-commit. This prevents a committed debit without a matching credit. Two independent auto-commit updates can leave money lost if debit succeeds and credit fails, or inconsistent balances.

Unsafe login pattern `SELECT ... WHERE username=' + input + ' AND password=' + input` lets crafted input alter SQL grammar (for example, a quote and tautology can make the WHERE condition true). Never test injection against a real system. PreparedStatement binds values separately so quote characters remain data. Store salted password hashes and use a password-hashing algorithm; do not compare plaintext passwords or store them directly.
