import java.util.ArrayList;
import java.util.List;

public class Classroom{

    public static long time = System.currentTimeMillis();
    int size;
    
    // Since it's unknown how many students are going to class, made the arrays dynamic
    public List <String> class1 = new ArrayList<String>();
    public List <String> class2 = new ArrayList<String>();
    public boolean [] student = new boolean[size];

    Classroom(int s){
        size = s;
    }

    public void msg(String m){
        System.out.println("[" + (System.currentTimeMillis() - time) + "] Classroom: " + m);
    }

}
