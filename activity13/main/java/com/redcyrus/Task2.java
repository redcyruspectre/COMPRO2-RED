package activity13.main.java.com.redcyrus;

public class Task2 implements Runnable{
    private int id;
    private int time;
    private int tasks;

    public Task2(int id, int time, int tasks){
        this.id = id;
        this.time = time;
        this.tasks = tasks;
    }
   @Override
   public void run() {
        System.out.println("Task #"+id);
        for(int i = 1; i <= tasks; i++){
            System.out.printf("Task #%d - %d is complete.\n", id, i);
            try {
                Thread.sleep(time);
            } catch (InterruptedException e) {
                System.out.println("interrupted");
            }
        }
       
   }
}