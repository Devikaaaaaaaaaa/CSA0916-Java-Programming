import java.io.*;
import java.util.*;
class Resident implements Serializable {
    @Serial private static final long serialVersionUID=1L;
    String name,registerNo,phone;
    transient String password;
    transient Object liveDatabaseConnection;
    Resident(String n,String r,String p,String phone){name=n;registerNo=r;password=p;this.phone=phone;}
}
class Room implements Serializable {
    @Serial private static final long serialVersionUID=1L;
    String roomNo; List<Resident> residents=new ArrayList<>();
    Room(String roomNo){this.roomNo=roomNo;}
}
class Hostel implements Serializable {
    @Serial private static final long serialVersionUID=1L;
    String hostelName; List<Room> rooms=new ArrayList<>();
    Hostel(String name){hostelName=name;}
}
public class SerializationModel {
    public static void main(String[] args)throws Exception{
        Hostel h=new Hostel("North Hostel");Room r=new Room("A-101");
        r.residents.add(new Resident("Mira","STU101","secret","9876543210"));h.rooms.add(r);
        try(ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("hostel.ser"))){out.writeObject(h);}
        try(ObjectInputStream in=new ObjectInputStream(new FileInputStream("hostel.ser"))){
            Hostel copy=(Hostel)in.readObject();
            Resident resident=copy.rooms.get(0).residents.get(0);
            System.out.println(copy.hostelName+" / "+resident.name+" / password restored? "+(resident.password!=null));
        }
    }
}