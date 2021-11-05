package sample;

public class Disasters {
    private String type;
    private int x;
    private int y;
    private int width;
    private int height;
    private long time;

    public Disasters(String t, int x, int y){
        type = t;
        this.x = x;
        this.y = y;
        time = System.nanoTime();
        generateWidthAndHeight();
    }

    public void generateWidthAndHeight(){
        //Width and Height of the area of the disaster is randomly generated based on a set range
        //Lightning is smaller (less width and height) than the hurricane
        if (type.equals("Lightning")){
            width = (int)(Math.random()*2 +1);
            height = (int)(Math.random()*2 +1);
        } else {
            width = (int)(Math.random()*4 +1);
            height = (int)(Math.random()*4 +1);
        }
    }

    public long getTime(){
        return time;
    }

    public String getType() {
        return type;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
