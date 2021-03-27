import java.util.ArrayList;

public class Student extends Thread{
    public static long time = System.currentTimeMillis();
    public Thread student = new Thread();
    Teacher teacher;

    Student(int val, Teacher t){
        student.setName("Student-" + val);
        teacher = t;
    }

    @Override
    public void run() {
        msg("Arriving to school");
        studentSleeper(1500);

        msg("Student is filling out Heath Questionaire.");
        studentSleeper(1500);

        while(teacher.arrived == false){
            msg("Waiting for teacher");
            studentSleeper(2500);
        }

        msg("Allowed into the school now.");    // Teacher has arrived
        student.yield();                        // So they all go into the classroom at the same time

        useBathroom();                          // Students go to use the bathroom

        if(teacher.bBathroomOccupied.size() == 0 && teacher.gBathroomOccupied.size() == 0)    // Students done with bathroom
            teacher.studentsFinishedBathroom = true;

        // First class
        student.yield();
        studentSleeper(5000);

        // Going to class 1
        classInfo(1);

        // Going to class 2
        classInfo(2);

        

    }

    public boolean goingToClass(){
        int p = priorityNum(); 
        
        if(p < 7){
            return true;
        }
        return false;
    }

    /*
        This is where the students wait for a class. The while(teacher.classInSession) holds
        the students until the teacher sets that certain class to true. 

        Once enabled, the student has a random probability of attending class.

        If the class number = i, then they call the teachers function to join class of i.
    */
    public void classInfo(int i){
            int priority;

            while(teacher.classInSession[i - 1] == false){
                studentSleeper(2000);
            }

            if(!goingToClass()){                        // Chooses value from 1-10 if student goes to class
                priority = student.getPriority() + 5;
                student.setPriority(priority);        // Changing the priority if not going to class
                studentSleeper(2000);
                msg("Student is not going to class and is going to walk around campus");
                student.setPriority(5);         // Changing it back to it's original priority
            }

            else{
                priority = (student.getPriority() % 5) + 1;
                student.setPriority(priority);   // Sets priority for student joining to 1
                msg("Joining class");

                if(i == 1)
                    teacher.joinClass1(student.getName());
                
                else if (i == 2)    
                    teacher.joinClass2(student.getName());

                studentSleeper(3000);
                msg("Is sleeping in class");

                while(teacher.classInSession[i - 1] == true && !teacher.isInterrupted()){
                    studentSleeper(1000);
                }
                msg("Is leaving class");
                student.setPriority(5);
            }
    }

    /*
        Randomly decides whether or not the student will attend class
    */
    public int priorityNum(){
        return ((int) (Math.random() * (10 - 1))) + 1;  // Rand value between 1 - 10
    }

    public void msg(String m){
        System.out.println("[" + (System.currentTimeMillis() - time) + "]" + student.getName() + ": " + m);
    }

    /* 
        Need this because a thread can't sleep without a try and catch block. So made 
        a function instead of typing it over multiple times.
    */
    public void studentSleeper(int t){
        try{
            student.sleep(t);
        }

        catch(InterruptedException err){

        }
    }

    /*
        Boys and girls use a certain bathroom based on a certain number in their name.
        If that number is even, they use the boys bathroom. If odd, the girls bathroom.
    */

    public void useBathroom(){
        int nameLength = student.getName().length();
        int nameConverter = Integer.parseInt(student.getName().substring(nameLength - 2, nameLength));
    
        teacher.bathroomB[0] = false; 
        teacher.bathroomB[1] = false;
        teacher.bathroomG[0] = false; 
        teacher.bathroomG[1] = false;


        if(nameConverter % 2 == 0 ){
            useBathroomB(student.getName());
        }

        else{
            useBathroomG(student.getName());
        }
    }

    /*
        useBathroomB and useBathroomG is meant to represent where if the thread is a boy or girl, 
        they use the bathroom based on their gender. 

        Then it is added to an ArrayList where they are dynamicaly added and removed based on the 
        order they joined the ArrayList.

        The thread yields 3 times when the student is waiting for the bathroom.
    */

    public void useBathroomB(String m){
        teacher.waitingBathroomB.add(m);

        if(teacher.bBathroomOccupied.size() < 2){
            teacher.bBathroomOccupied.add(m);
            teacher.waitingBathroomB.remove(m);
            msg("Using boys bathroom");
            studentSleeper(1000);
            msg("Finished using boys bathroom");
            teacher.bBathroomOccupied.remove(m);
        }

        else{
            do{
                msg("Waiting to use the boys bathroom.");
                Thread.yield();
                Thread.yield();
                Thread.yield();

                studentSleeper(1000);

            } while(teacher.bBathroomOccupied.size() == 2);

            teacher.waitingBathroomB.remove(m);
            msg("Using boys bathroom");
            studentSleeper(1000);
            msg("Finished using boys bathroom");
            Thread.yield();
            teacher.bBathroomOccupied.remove(m);            
        }
    }   // Boys bathroom

    public void useBathroomG(String m){
        teacher.waitingBathroomG.add(m);

        if(teacher.gBathroomOccupied.size() < 2){
            teacher.gBathroomOccupied.add(m);
            teacher.waitingBathroomG.remove(m);
            msg("Using girls bathroom");
            studentSleeper(1000);
            msg("Finished using girls bathroom");
            studentSleeper(1000);

            teacher.gBathroomOccupied.remove(m);
        }

        else{
            do{
                msg("Waiting to use the girls bathroom.");
                Thread.yield();
                Thread.yield();
                Thread.yield();

                studentSleeper(1000);

            } while(teacher.gBathroomOccupied.size() == 2);

            teacher.waitingBathroomG.remove(m);
            msg("Using girls bathroom");
            studentSleeper(1000);
            msg("Finished using girls bathroom");
            studentSleeper(1000);

            teacher.gBathroomOccupied.remove(m);            
        }   
    }   // Girls bathroom

}