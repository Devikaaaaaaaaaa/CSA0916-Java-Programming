# Task A4 – Serialization design

Mark password transient: never persist plaintext credentials; use salted password hashes in a secure credential store instead. Mark live JDBC Connection transient: it is process/session state, not a portable serializable value. Recreate a connection from configuration after deserialization. The complete reachable serializable graph (Hostel → Rooms → Residents) is written; every non-transient referenced object must itself be serializable or serialization fails with NotSerializableException.

serialVersionUID identifies a compatible class version. Adding a phone field to a serializable class: old streams deserialize the new field to its default value (null for String) if compatibility is maintained; application migration should supply a safe default or migration logic. Explicit UID makes compatibility intent visible, but does not guarantee semantic compatibility.

Java serialization is convenient for trusted Java object graphs but is Java-specific, tightly coupled to class structure and unsafe for untrusted input unless strict filters are applied. CSV is human-readable and interoperable but needs escaping/schema conventions. JSON is broadly interoperable and supports explicit schemas, but requires a JSON library and explicit mapping. Avoid deserializing untrusted data.
