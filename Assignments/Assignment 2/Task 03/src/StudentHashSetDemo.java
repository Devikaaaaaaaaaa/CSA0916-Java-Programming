import java.util.*;
final class Student {
 private final String regNo,name;
 Student(String r,String n){if(r==null||r.isBlank())throw new IllegalArgumentException("Registration required");regNo=r.trim().toUpperCase();name=n;}
 @Override public boolean equals(Object o){if(this==o)return true;if(!(o instanceof Student))return false;Student s=(Student)o;return regNo.equals(s.regNo);}
 @Override public int hashCode(){return Objects.hash(regNo);}
 @Override public String toString(){return regNo+"-"+name;}
}
public class StudentHashSetDemo {
 public static void main(String[]x){Set<Student> scans=new HashSet<>();scans.add(new Student("STU101","Asha"));scans.add(new Student("STU102","Bala"));scans.add(new Student("STU101","Asha duplicate"));System.out.println("Unique students: "+scans.size());System.out.println("Contains STU101: "+scans.contains(new Student("STU101","Different name")));Set<Student> tree=new TreeSet<>(Comparator.comparing(s->s.toString().split("-")[0]));tree.addAll(scans);System.out.println("TreeSet unique: "+tree.size());Map<String,Student> byReg=new HashMap<>();for(Student s:scans)byReg.put(s.toString().split("-")[0],s);System.out.println("HashMap unique: "+byReg.size());}
}