import java.io.*;
import java.nio.file.*;
import java.util.*;

public class SalesReport {
    static List<String> readManual(List<Path> files) throws IOException {
        List<String> rows = new ArrayList<>();
        for (Path file : files) {
            BufferedReader reader = null;
            try {
                reader = Files.newBufferedReader(file);
                String line;
                while ((line = reader.readLine()) != null) rows.add(line);
            } catch (IOException e) {
                throw new IOException("Failed while reading " + file, e);
            } finally {
                if (reader != null) try { reader.close(); }
                catch (IOException closeError) { System.err.println("Close failed: " + closeError.getMessage()); }
            }
        }
        return rows;
    }
    static List<String> readSafely(List<Path> files) throws IOException {
        List<String> rows = new ArrayList<>();
        for (Path file : files) {
            try (BufferedReader reader = Files.newBufferedReader(file)) {
                String line;
                while ((line = reader.readLine()) != null) rows.add(line);
            } catch (IOException e) { throw new IOException("Failed while reading " + file, e); }
        }
        return rows;
    }
    static void writeReport(Path output, List<String> rows) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(output)) {
            writer.write("DAILY SALES SUMMARY"); writer.newLine();
            for (String row : rows) { writer.write(row); writer.newLine(); }
            writer.write("Records processed: " + rows.size()); writer.newLine();
        }
    }
    public static void main(String[] args) throws IOException {
        Path dir = Path.of("data"); Files.createDirectories(dir);
        Files.writeString(dir.resolve("store-a.txt"), "Store A, 12500\nStore A, 9800\n");
        Files.writeString(dir.resolve("store-b.txt"), "Store B, 15300\n");
        List<Path> inputs = List.of(dir.resolve("store-a.txt"), dir.resolve("store-b.txt"));
        List<String> manual = readManual(inputs), safe = readSafely(inputs);
        writeReport(Path.of("sales-report.txt"), safe);
        System.out.println("Manual and try-with-resources results match: " + manual.equals(safe));
        System.out.println("Records processed: " + safe.size());
        System.out.println("Report written to sales-report.txt");
    }
}