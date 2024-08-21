package TankGame.src;

public class Sample implements Runnable{ // or extends Thread

    // can share runnable objects
    @Override
    public void run() {
        System.out.println("hello");
    }

    public static void main(String[] args) {
        for(int i = 0; i < 10; i++){
            Thread t = new Thread(new Sample());
            t.start();
        }
    }
}
