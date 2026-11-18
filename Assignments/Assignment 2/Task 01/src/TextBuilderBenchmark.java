import java.util.*;
class TextBuilderBenchmark {
 static String byPlus(String[] f){String s="";for(String x:f)s+=x;return s;}
 static String byBuilder(String[] f){StringBuilder b=new StringBuilder();for(String x:f)b.append(x);return b.toString();}
 static String byBuffer(String[] f){StringBuffer b=new StringBuffer();for(String x:f)b.append(x);return b.toString();}
 static long measure(java.util.function.Function<String[],String> fn,String[] fields,int rounds){long start=System.nanoTime();long check=0;for(int r=0;r<rounds;r++)check+=fn.apply(fields).length();long elapsed=System.nanoTime()-start;if(check==0)throw new AssertionError();return elapsed/rounds;}
 static String[] fields(int count){String[] a=new String[count];for(int i=0;i<count;i++)a[i]="FIELD"+i+"|";return a;}
 public static void main(String[] args){int[][] loads={{6,1},{50,10000},{200,1000}};for(int[] load:loads){String[] f=fields(load[0]);int rounds=load[1];System.out.println("\nFields="+load[0]+", tickets="+rounds);System.out.println("String + avg ns: "+measure(TextBuilderBenchmark::byPlus,f,rounds));System.out.println("StringBuilder avg ns: "+measure(TextBuilderBenchmark::byBuilder,f,rounds));System.out.println("StringBuffer avg ns: "+measure(TextBuilderBenchmark::byBuffer,f,rounds));}System.out.println("Per-ticket: StringBuilder; shared multithreaded log: StringBuffer or synchronized logger.");}
}