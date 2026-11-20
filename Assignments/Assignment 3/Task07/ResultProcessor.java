import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

public class ResultProcessor {
    record Result(String file, boolean success, String message) {}
    static class ProcessFile implements Callable<Result> {
        private final Path file;
        ProcessFile(Path file) { this.file = file; }
        public Result call() {
            try {
                String text = Files.readString(file).trim();
                if (text.isEmpty()) throw new IllegalArgumentException("Empty score file");
                String[] parts = text.split(",");
                if (parts.length != 2) throw new IllegalArgumentException("Expected studentId,score");
                int score = Integer.parseInt(parts[1].trim());
                if (score < 0 || score > 100) throw new IllegalArgumentException("Score must be 0-100");
                return new Result(file.getFileName().toString(), true, parts[0].trim()+" result: "+score);
            } catch (IOException | RuntimeException e) {
                return new Result(file.getFileName().toString(), false,
                    e.getClass().getSimpleName()+": "+e.getMessage());
            }
        }
    }
    public static void main(String[] args) throws Exception {
        Path dir=Path.of("scores"); Files.createDirectories(dir);
        Files.writeString(dir.resolve("student-01.txt"),"S001,85");
        Files.writeString(dir.resolve("student-02.txt"),"S002,92");
        Files.writeString(dir.resolve("student-03.txt"),"S003,not-a-number");
        List<Path> files=List.of(dir.resolve("student-01.txt"),dir.resolve("student-02.txt"),
            dir.resolve("student-03.txt"),dir.resolve("student-04.txt"));
        for (int poolSize : new int[]{1,2,4,8}) {
            long start=System.nanoTime();
            ExecutorService pool=Executors.newFixedThreadPool(poolSize);
            ConcurrentLinkedQueue<Result> results=new ConcurrentLinkedQueue<>();
            try {
                List<Future<Result>> futures=new ArrayList<>();
                for(Path file:files) futures.add(pool.submit(new ProcessFile(file)));
                for(Future<Result> future:futures) results.add(future.get());
            } finally { pool.shutdown(); pool.awaitTermination(1,TimeUnit.MINUTES); }
            long ms=TimeUnit.NANOSECONDS.toMillis(System.nanoTime()-start);
            long ok=results.stream().filter(Result::success).count();
            System.out.println("Pool size: "+poolSize+" | Success: "+ok+" | Failure: "+(results.size()-ok)+" | Time: "+ms+" ms");
            results.forEach(r -> System.out.println((r.success()?"OK ":"FAILED ")+r.file()+" - "+r.message()));
        }
    }
}