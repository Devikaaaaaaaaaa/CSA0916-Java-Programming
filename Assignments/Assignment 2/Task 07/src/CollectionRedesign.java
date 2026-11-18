import java.util.*;
class Booking {final String id;final long time;Booking(String i,long t){id=i;time=t;}}
public class CollectionRedesign {
 static long scan(List<Booking> list,String id){long s=System.nanoTime();Booking found=null;for(Booking b:list)if(b.id.equals(id)){found=b;break;}if(found==null)throw new AssertionError();return System.nanoTime()-s;}
 static long map(Map<String,Booking> map,String id){long s=System.nanoTime();Booking b=map.get(id);if(b==null)throw new AssertionError();return System.nanoTime()-s;}
 public static void main(String[]x){for(int n:new int[]{1000,10000,100000}){List<Booking> list=new ArrayList<>();Map<String,Booking> index=new HashMap<>();for(int i=0;i<n;i++){Booking b=new Booking("PNR"+i,i);list.add(b);index.put(b.id,b);}String target="PNR"+(n-1);long a=0,b=0;for(int r=0;r<1000;r++){a+=scan(list,target);b+=map(index,target);}System.out.printf("n=%d average lookup ns: list scan=%.1f, hash index=%.1f%n",n,a/1000.0,b/1000.0);System.out.println("Design trade-off: HashMap adds O(n) index storage but changes expected lookup from O(n) to O(1).");}}
}