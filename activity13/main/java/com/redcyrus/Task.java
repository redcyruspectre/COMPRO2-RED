package activity13.main.java.com.redcyrus;

public class Task extends Thread{
    private int id;
    public Task(int id){
        this.id = id;
    }
    @Override
    public void run(){
        System.out.println("Running task #" + id);
        for(int i=0; i < 10; i++){
            System.out.println("Sub-task #"+ (i+1) + " from Task #" + id + "  is complete.");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
               System.out.println("Pause interrupted...");
            }
        }
    }
}