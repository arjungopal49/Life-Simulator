package sample;

import javafx.scene.control.ListView;

import java.util.ArrayList;

public class Creature {
    private int x;
    private int y;
    private int gender;
    //1 = male, 2 = female
    private int stage;
    //1 = zombie child, 2 = zombie adult, 3 = goblin child, 4 = goblin adult, 5 = werewolf child, 6 = werewolf adult
    private String typeOfCreature;
    private long age;
    private int strength;
    private int intelligence;
    private long startTime;
    private long reproduceTime;
    private boolean hasReproduced = false;

    public Creature(int x, int y, String t){
        typeOfCreature = t;
        this.x = x;
        this.y = y;
        startTime = System.nanoTime();
        reproduceTime = System.nanoTime();
        age = System.nanoTime();
        gender = (int)(Math.random()*2+1);

        //Different creatures have different ranges of strengths and intelligence
        //For stages, 1 and 2 correspond to zombies, 3 and 4 correspond to goblins, and 5 and 6 correspond to werewolves
        if (typeOfCreature.equals("Zombie")){
            stage = 1;
            intelligence = (int)(Math.random()*60 + 40);
            strength = (int)(Math.random()*40 + 60);
        }
        if (typeOfCreature.equals("Goblin")){
            stage = 3;
            intelligence = (int)(Math.random()*50 + 50);
            strength = (int)(Math.random()*50 + 50);
        }
        if (typeOfCreature.equals("Werewolf")){
            stage = 5;
            intelligence = (int)(Math.random()*40 + 60);
            strength = (int)(Math.random()*60 + 40);
        }


    }
    public void changeLoc(int[][] gameGrid, ArrayList<Creature> creatures, boolean moreThanOne){
 // chance that the creature does a random move and a chance that the creature does a calculated move.
 // chance here is based on intelligence
    if (Math.random()*intelligence<25 || !moreThanOne){
            //random move
            boolean check = false;
            while(!check) {
                int tempx = x;
                int tempy = y;
                if (Math.random() > .5) {
                    tempx++;
                } else {
                    tempx--;
                }
                if (Math.random() > .5) {
                    tempy++;
                } else {
                    tempy--;
                }
                if (gameGrid[tempx][tempy] == 0) {
                    check = true;
                    gameGrid[tempx][tempy] = stage;
                    gameGrid[x][y] = 0;
                    x = tempx;
                    y = tempy;
                }
            }
        } else {
            //calculated move
            int tempx = x;
            int tempy = y;
            Creature nearestType = getNearestType(creatures);
            if (tempx>nearestType.getX()) {
                tempx--;
            }
            if (tempx<nearestType.getX()) {
                tempx++;
            }
            if (tempy>nearestType.getY()) {
                tempy--;
            }
            if (tempy<nearestType.getY()) {
                tempy++;
            }
            if (gameGrid[tempx][tempy] == 0) {
                gameGrid[tempx][tempy] = stage;
                gameGrid[x][y] = 0;
                x = tempx;
                y = tempy;
            }

        }



    }

    public Creature getNearestType(ArrayList<Creature> creatures){
        //finds the nearest creature of the same type by going through all creatures of the same type and finding the one with the least distance using the distance formula
        int creatureIndex = 0;
        double minDistance = 50;
        for (int i = 0; i < creatures.size(); i++){
            if (!(creatures.get(i).getX() == x  && creatures.get(i).getY() == y)) {
                if (creatures.get(i).getTypeOfCreature().equals(this.typeOfCreature)) {
                    int Xdiff = Math.abs(creatures.get(i).getX()-this.x);
                    int Ydiff = Math.abs(creatures.get(i).getY()-this.y);
                    double distance = Math.sqrt((Math.pow(Xdiff, 2) + Math.pow(Ydiff, 2)));
                    if (distance < minDistance) {
                        minDistance = distance;
                        creatureIndex = i;
                    }
                }
            }
        }
        return creatures.get(creatureIndex);
    }


    //This function checks the neighboring squares to see if there are any creatures
    public void checkNeighbor(ArrayList<Creature> creatures, int[][] tempGrid, ArrayList<String> events){
        for (int i = 0;i<creatures.size();i++){
            if(creatures.get(i).getX() >=x-1 && creatures.get(i).getX()<=x+1 && creatures.get(i).getY() >=y-1 && creatures.get(i).getY()<=y+1){
                if (!creatures.get(i).equals(this)) {
                    //if the creatures are of the same species, reproduciton is considered
                    if (creatures.get(i).getTypeOfCreature().equals(typeOfCreature)) {
                        checkReproduction(creatures, i, tempGrid, events);
                    } else { //if they are of different species, a fight is considered
                        fight(creatures, i, tempGrid, events);
                    }
                    break;
                }
            }
        }

    }

    public void checkReproduction(ArrayList<Creature> creatures, int otherCreature, int tempGrid[][], ArrayList<String> events){
        //checks that both have not reproduced before
        if (!hasReproduced && !creatures.get(otherCreature).getHasReproduced()){
            //checks that both are old enough
            if (reproduceTime > 4000000000.0 && creatures.get(otherCreature).getReproduceTime() > 4000000000.0){
                //checks that both are opposite genders
                if (gender != creatures.get(otherCreature).getGender()){
                    if (Math.random()>0.5){
                        reproduce(creatures, otherCreature, tempGrid, events);
                    }
                }
            }
        }
    }

    public void reproduce(ArrayList<Creature> creatures, int otherCreature, int gameGrid[][], ArrayList<String> events){
        //generates all possible empty locations within 2 blocks of the creature
        ArrayList<Locations> tempLocs = new ArrayList<>();
        for(int i = x-1; i<x+2;i++){
            for(int j = y-1;j<y+2;j++){
                if(x<gameGrid.length && y < gameGrid[0].length){
                    if(gameGrid[i][j] == 0){
                        tempLocs.add(new Locations(i,j));
                    }
                }
            }
        }

        //chooses a random one of those locations to spawn the baby
        if(tempLocs.size()>0){
            int newLoc = (int)(Math.random()*tempLocs.size());
            creatures.add(new Creature(tempLocs.get(newLoc).getX(), tempLocs.get(newLoc).getY(), typeOfCreature));
            gameGrid[creatures.get(creatures.size()-1).getX()][creatures.get(creatures.size()-1).getY()]=stage-1;
            events.add("Two " + typeOfCreature + " creatures reproduced");
            hasReproduced = true;
            creatures.get(otherCreature).setHasReproduced(true);
        }
    }

    public void fight(ArrayList<Creature> creatures, int otherCreature, int tempGrid[][], ArrayList<String> events){
        if (Math.random() > 0.5) {
            //fights are determined by strength and intelligence although strength is worth 2 times more
            int creature1Score = strength*2 + intelligence;
            int creature2Score = creatures.get(otherCreature).getStrength()*2 + creatures.get(otherCreature).getIntelligence();
            creature1Score = creature1Score + (int)(Math.random()*50-25);
            creature2Score = creature2Score + (int)(Math.random()*50-25);
            if (creature1Score > creature2Score){
                tempGrid[creatures.get(otherCreature).getX()][creatures.get(otherCreature).getY()] = 0;
                events.add("A " + typeOfCreature + " killed a " + creatures.get(otherCreature).getTypeOfCreature());
                creatures.remove(otherCreature);
            } else if (creature2Score > creature1Score){
                tempGrid[x][y] = 0;
                events.add("A " + creatures.get(otherCreature).getTypeOfCreature() + " killed a " + typeOfCreature);
                for (int i = 0; i < creatures.size(); i++) {
                    if (creatures.get(i).equals(this)){
                        creatures.remove(i);
                    }
                }
            }
        }

    }


    public void changeAdult(){
        //updates the stage to an Adult by adding 1 to the value
        if (typeOfCreature.equals("Zombie")){
            stage = 2;
        }
        if (typeOfCreature.equals("Goblin")){
            stage = 4;
        }
        if (typeOfCreature.equals("Werewolf")){
            stage = 6;
        }
    }
    public int getGender() { return gender;}
    public int getX(){
        return this.x;
    }
    public void resetStartTime(){
        startTime = System.nanoTime();
    }
    public long getStartTime(){
        return startTime;
    }
    public long getReproduceTime() {
        return reproduceTime;
    }
    public int getY() {
        return this.y;
    }

    public long getAge() {
        return age;
    }

    public String getTypeOfCreature(){
        return typeOfCreature;
    }

    public int getStrength() {
        return strength;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setHasReproduced(boolean hasReproduced) {
        this.hasReproduced = hasReproduced;
    }

    public boolean getHasReproduced() {
        return hasReproduced;
    }
}
