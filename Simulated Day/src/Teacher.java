import java.util.ArrayList;
import java.util.Vector;
import java.util.Arrays;

public class Teacher extends Thread {

    public static long time = System.currentTimeMillis();
    Classroom cla;
    Thread teacher = new Thread("Teacher");
    int size;
    boolean arrived = false;
    boolean studentsFinishedBathroom = false;
    boolean [] studentsFinishedDay;
    boolean teacherFinished = false;
    boolean [] classInSession = new boolean[4];

    public boolean [] bathroomB = new boolean [2];
    public boolean [] bathroomG = new boolean [2];
    public Vector <String> waitingBathroomB = new Vector<String>();    
    public Vector <String> waitingBathroomG = new Vector<String>();  
    public Vector <String> bBathroomOccupied = new Vector<String>(); 
    public Vector <String> gBathroomOccupied = new Vector<String>();  
 


    Teacher(int n){
        cla = new Classroom(n);
        studentsFinishedDay = new boolean[n];
        size = n;
    }

    @Override
    public void run() {
        setStudentsDoneToFalse();
        setClassToFalse();
        teacherSleeper(7000);
        msg("       *** Teacher has arrived ***");
        arrived = true;

        while(studentsFinishedBathroom == false){
            msg("       *** Waiting for students to finish using the bathroom. ***");
            teacherSleeper(6500);
        }

        msg("       *** All students are done in the bathroom, the classroom is open. ***");
        teacherSleeper(3000);

        teacher.yield();
        msg("       *** First period will begin ***");

        // First Class of the day
        msg("       *** Teaching his first class ***");
        classInSession[0] = true;
        teacherSleeper(9000);

        teacher.interrupt();
        msg("       *** Has finished teaching the class ***");
        classInSession[0] = false;
        
        msg("       *** Has finished teaching the class and now has Office Hours ***");
        teacherSleeper(10000);
        msg("       *** Teacher has finished Office Hours ***");

        // Second Class of the day
        msg("       *** Teaching his second class ***");
        classInSession[1] = true;
        teacherSleeper(10000);

        teacher.interrupt();
        msg("       *** Has finished teaching for the day ***");
        classInSession[1] = false;
        teacherSleeper(5000);

        // Attendance Records
        breakdown();
        teacherFinished = true;

    }

    public void msg(String m){
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + teacher.getName() + ": " + m);
    }

    public void teacherSleeper(int t){
        try{
            teacher.sleep(t);
        }

        catch(InterruptedException err){

        }
    }

    // Print Statement of who showed up to each class
    public void breakdown(){
        System.out.println("\nAttendance Records\nStudent: Class 1: Class 2:");

        for(int i = 0; i < size; i++){
            String s = "Student-" + Integer.toString(i+1);
            System.out.print("Student [" + (i+1) + "]   "); // 14

            if(cla.class1.contains(s) && i < 9)
                System.out.print(" Y\t");
            else if (i < 10)   
                System.out.print("  \t");

            if(cla.class1.contains(s) && i >= 9)
                System.out.print(" Y\t");
            else if(i >= 10)   
                System.out.print(" \t");

            if(cla.class2.contains(s))
                System.out.print("Y");
            else
                System.out.print(" ");

            System.out.println();

        }
    }

    // Initializes the array where students are done for the day to false
    public void setStudentsDoneToFalse(){
        for(int i = 0; i < 4; i++){
            studentsFinishedDay[i] = false;
        }
    }

    // Initializes the array where classes are done to false
    public void setClassToFalse(){
        for(int i = 0; i < 4; i++){
            classInSession[i] = false;
        }
    }

    // Students join each class
    public void joinClass1(String m){
        cla.class1.add(m);
    }

    public void joinClass2(String m){
        cla.class2.add(m);

    }

    // When student is finished, it's this is called in the Student class
    public void studentIsDone(int m){
        cla.student[m] = true;
    }
}
