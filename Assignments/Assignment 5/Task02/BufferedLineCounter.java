import java.io.*;
import java.nio.file.*;
public class BufferedLineCounter {
    static long countUnbuffered(Path p)throws IOException{
        long lines=0; boolean any=false,lastNewline=false;
        try(InputStream in=Files.newInputStream(p)){
            int b; while((b=in.read())!=-1){any=true;lastNewline=(b=='\\n');if(b=='\\n')lines++;}
        }
        return any&&!lastNewline?lines+1:lines;
    }
    static long countBuffered(Path p)throws IOException{
        long lines=0; boolean any=false,lastNewline=false;
        try(BufferedInputStream in=new BufferedInputStream(Files.newInputStream(p),64*1024)){
            int b;while((b=in.read())!=-1){any=true;lastNewline=(b=='\\n');if(b=='\\n')lines++;}
        }
        return any&&!lastNewline?lines+1:lines;
    }
    public static void main(String[] args)throws Exception{
        if(args.length==0){System.out.println("Usage: java BufferedLineCounter <file>");return;}
        Path p=Path.of(args[0]); long t=System.nanoTime();long a=countUnbuffered(p);long t1=System.nanoTime();
        long b=countBuffered(p);long t2=System.nanoTime();
        System.out.printf("Unbuffered lines=%d time=%.3f ms%n",a,(t1-t)/1e6);
        System.out.printf("Buffered lines=%d time=%.3f ms%n",b,(t2-t1)/1e6);
    }
}