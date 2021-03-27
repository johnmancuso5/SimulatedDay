public class Main {
    public static long time = System.currentTimeMillis();
	private static boolean allAreDone;

    public static void main(String[] args) {

        int totalStudents = 13;

        // *** Classroom is used in teacher ***
        Teacher teacher = new Teacher(totalStudents);
        Student[] t = new Student[totalStudents];

        teacher.msg("Is waking up");
        teacher.start();

        // All students will have the same teacher and classroom since only one object
        // has been created
        for (int i = 1; i <= totalStudents; i++) {
            t[i - 1] = new Student(i, teacher); // Have to do i + 1 inside student to avoid OutofBounds
            t[i - 1].msg("Is waking up");
            t[i - 1].start();
        }
        
        // Waiting for teacher to finish teaching
        while(teacher.teacherFinished == false){
            for(int i = 0; i < totalStudents; i++){
                t[i].studentSleeper(1000);
            }
        }

        // Once done teaching, all students will go home
        for(int i = totalStudents - 1; i >= 0; i--){
            try{
                t[i].msg("Is going home now.");
                t[i].join();
            }
            catch (InterruptedException err){
    
            }
        }

        // Once all students are done, the teacher will no longer sleep
        while(!AllAreDone(t, totalStudents)){
            teacher.teacherSleeper(1000);
        }

        try{
            teacher.join();
            teacher.msg("       *** Is going home now. ***");
        }

        catch (InterruptedException err){

        }
    }

    public static void msg(String m) {
        System.out.println("[" + (System.currentTimeMillis() - time) + "] Main: " + m);
    }

    // This checks if all the student threads are alive, if they are the teacher will finish up
    public static boolean AllAreDone(Student[] s, int total) {
        for(int i = 0; i < total; i++){
            if(s[i].isAlive()){
                return false;
            }
        }
        return true;
    }
}
