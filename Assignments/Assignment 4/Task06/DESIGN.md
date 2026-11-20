# Task 6 – Design Challenge

## Users, screens and requirements
- Student: register for an event; see success, invalid-input and duplicate-registration feedback.
- Committee member: view registrations, filter by event and export a CSV.
- Screens: registration form, committee dashboard/table, filter controls, export action; confirmation/error dialogs.
- Rules: valid student ID; event required; one registration per student/event; database errors reported without freezing UI.

## Layered design
- UI: Swing JFrame/JPanel/JTable/JDialog; input collection and display only.
- Service: validation, duplicate/business rules, registration orchestration.
- DAO/JDBC: prepared statements, transactions and resource closing.
- Background: SwingWorker for database work; update Swing components only on EDT.

## Class diagram
```text
EventRegistrationFrame -> RegistrationService -> RegistrationDao -> MySQL
CommitteeFrame --------> RegistrationService -> RegistrationDao -> MySQL
RegistrationService: validate(), register(), listByEvent()
RegistrationDao: insert(), findDuplicate(), pageByEvent()
```

## Register pseudocode
Trim and validate student ID and event. Call service on background worker. DAO inserts with unique(student_id,event_name). On success show confirmation and refresh table; on SQL constraint violation report duplicate; otherwise show a safe error message.

## Filter pseudocode
Validate selected event and page number. Execute parameterized SELECT with ORDER BY and LIMIT/OFFSET in SwingWorker. Publish results; replace table model on EDT. Export only the currently selected/loaded rows or stream all pages to CSV.

## Complexity and scalability
A unique index makes duplicate lookup/insert approximately O(log n) B-tree work. Fetching k rows with an indexed filter is approximately O(log n + k), though OFFSET can become slower for deep pages. Loading 100,000 rows at once costs O(n) time and O(n) client memory and can stall rendering. Loading pages of k rows limits each response and table memory to O(k), improves responsiveness and enables predictable UI updates. Keyset pagination can outperform large OFFSET values.

## Security and responsiveness
Use least-privilege database credentials from environment variables, never hard-code secrets, validate all inputs, use PreparedStatement, close JDBC resources with try-with-resources, and run database calls in SwingWorker. The demo assumes MySQL Connector/J is on the classpath and DB_URL/DB_USER/DB_PASSWORD are configured.
