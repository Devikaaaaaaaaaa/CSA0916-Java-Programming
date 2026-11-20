import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Delimited bus-pass store. Format: id|registerNo|name|route|stop|validUntil|amount */
public class PassStore {
    public record Pass(String id,String registerNo,String name,int route,String stop,String validUntil,double amount) {
        public String toLine(){return String.join("|", id,registerNo,name,Integer.toString(route),stop,validUntil,Double.toString(amount));}
        static Pass parse(String line) {
            String[] f=line.split("\\|",-1);
            if(f.length!=7 || Arrays.stream(f).anyMatch(String::isBlank)) throw new IllegalArgumentException("wrong field count or empty field");
            int route=Integer.parseInt(f[3]); double amount=Double.parseDouble(f[6]);
            if(route<=0 || !Double.isFinite(amount) || amount<0) throw new IllegalArgumentException("invalid numeric value");
            return new Pass(f[0],f[1],f[2],route,f[4],f[5],amount);
        }
    }
    private final Path file;
    public PassStore(Path file){this.file=file;}
    public void add(Pass pass) throws IOException {
        Path parent=file.toAbsolutePath().getParent(); if(parent!=null) Files.createDirectories(parent);
        try(BufferedWriter out=Files.newBufferedWriter(file, StandardOpenOption.CREATE,StandardOpenOption.APPEND)){
            out.write(pass.toLine()); out.newLine();
        }
    }
    public Pass find(String id) throws IOException {
        if(!Files.exists(file)) return null;
        try(BufferedReader in=Files.newBufferedReader(file)){
            String line;
            while((line=in.readLine())!=null){
                try { Pass p=Pass.parse(line); if(p.id().equals(id)) return p; }
                catch(RuntimeException damaged){ /* skip malformed record and continue */ }
            }
        }
        return null;
    }
    public List<Pass> listByRoute(int route) throws IOException {
        List<Pass> result=new ArrayList<>(); if(!Files.exists(file)) return result;
        try(BufferedReader in=Files.newBufferedReader(file)){
            String line; while((line=in.readLine())!=null) try {Pass p=Pass.parse(line);if(p.route()==route)result.add(p);}catch(RuntimeException ignored){}
        } return result;
    }
    public static void main(String[] args)throws Exception{
        Path p=Path.of(args.length>0?args[0]:"bus-passes.txt"); PassStore store=new PassStore(p);
        store.add(new Pass("P1003","STU1003","Kavya",12,"Main Gate","2027-03-31",1250));
        System.out.println("Find P1003: "+store.find("P1003"));
        System.out.println("Find damaged P1002: "+store.find("P1002"));
    }
}