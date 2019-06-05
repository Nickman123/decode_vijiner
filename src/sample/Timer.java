package sample;

public class Timer {

    private static long startTime, endTime;

    public static void startTimer() {
        startTime = System.currentTimeMillis();
    }


    public static long finishTimer() {
        endTime = System.currentTimeMillis();
        long time = endTime  - startTime;
        return (time / 1000);
    }
}
