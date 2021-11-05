package sample;


import java.util.Random;
import java.util.ArrayList;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;

public class Controller {
    private Random randNum = new Random();
    private int x = 20;
    private int y = 30;
    private int sliderValue = 1;
    private Button[][] btn = new Button[x][y];
    private int[][] gameGrid = new int[x][y];
    private ArrayList<Creature> creatures = new ArrayList<>();
    private ArrayList<Disasters> disasters = new ArrayList<>();
    private ObservableList<XYChart.Series> series = FXCollections.observableArrayList(new XYChart.Series(), new XYChart.Series(), new XYChart.Series());
    private int zombiePopulation = 0;
    private int goblinPopulation = 0;
    private int werewolfPopulation = 0;
    private int totalPopulation = 0;
    private ArrayList<Integer> time = new ArrayList<>();
    private ArrayList<String> events = new ArrayList<>();

    @FXML
    Slider creatureSlider;
    @FXML
    ListView mainListView;
    @FXML
    GridPane gPane;
    @FXML
    Label label1, label2;
    @FXML
    Button startButton, addCreatureButton;
    @FXML
    LineChart lChart = new LineChart(new NumberAxis(), new NumberAxis());



    @FXML
    private void handleStart() {
        for(int i=0; i<btn.length; i++){
            for(int j=0; j<btn[0].length;j++){
                //Initializing 2D buttons with values i,j
                btn[i][j] = new Button();
                btn[i][j].setStyle("-fx-background-color:#632c02");
                btn[i][j].setPrefWidth(25);
                gPane.add(btn[i][j], j, i);
                gameGrid[i][j]=0;
            }
        }
        gPane.setGridLinesVisible(true);
        gPane.setVisible(true);
        start();
    }

    @FXML
    private void handleSlider(){
        sliderValue = (int) creatureSlider.getValue();
    }

    @FXML
    private void handleAddAnt() {
        addCreature(sliderValue-1);
    }

    @FXML
    private void handleLightning(){
        simDisaster("Lightning");
    }

    @FXML
    private void handleHurricane(){
        simDisaster("Hurricane");
    }

    public void simDisaster(String type){
        //Randomly generates top left corner of disaster and makes sure that there are no other disasters taking place on that square
        int tempx = (int)(Math.random()*12 + 4);
        int tempy = (int)(Math.random()*21 + 4);
        while (gameGrid[tempx][tempy] > 6) {
            tempx = (int)(Math.random()*12 + 4);
            tempy = (int)(Math.random()*21 + 4);
        }
        disasters.add(new Disasters(type, tempx, tempy));
        Disasters disaster = disasters.get(disasters.size()-1);
        for (int i = disaster.getX(); i < disaster.getX()+disaster.getWidth(); i++){
            for (int j = disaster.getY(); j < disaster.getY()+disaster.getHeight(); j++){
                if (gameGrid[i][j] < 7) {
                    //if there are any creatures on the spot a disaster is occurring, they die
                    if (gameGrid[i][j] > 0) {
                        int creatureIndex = 0;
                        for (int x = 0; x < creatures.size(); x++) {
                            if (creatures.get(x).getX() == i && creatures.get(x).getY() == j) {
                                creatureIndex = x;
                            }
                        }
                        events.add("A " + creatures.get(creatureIndex).getTypeOfCreature() + " died to " + disaster.getType());
                        creatures.remove(creatureIndex);
                    }
                    //replaces the game grid square for 8 seconds with the respective disaster
                    if (disaster.getType().equals("Lightning")){
                        gameGrid[i][j] = 8;
                    } else {
                        gameGrid[i][j] = 7;
                    }
                }
            }
        }


    }

    public void addCreature(int type) {
        //Generates a random coordinate that is empty and adds a creature there based on the slider value
        int tempx = (int)(Math.random()*20);
        int tempy = (int)(Math.random()*30) ;
        while (!checkEmpty(tempx, tempy)){
            tempx = (int)(Math.random()*20);
            tempy = (int)(Math.random()*30);
        }
        if (type == 0) {
            creatures.add(new Creature(tempx,tempy, "Zombie"));
            gameGrid[creatures.get(creatures.size()-1).getX()][creatures.get(creatures.size()-1).getY()]=1;
        }
        if (type == 1) {
            creatures.add(new Creature(tempx,tempy, "Goblin"));
            gameGrid[creatures.get(creatures.size()-1).getX()][creatures.get(creatures.size()-1).getY()]=3;
        }
        if (type == 2) {
            creatures.add(new Creature(tempx,tempy, "Werewolf"));
            gameGrid[creatures.get(creatures.size()-1).getX()][creatures.get(creatures.size()-1).getY()]=5;
        }


        updateScreen();
    }

    public void updateScreen(){
    // 0 = empty, 1 = zombie child, 2 = zombie adult, 3 = goblin child, 4 = goblin adult, 5 = werewolf child, 6 = werewolf adult, 7 = hurricane, 8 = lightning
        for(int i=0; i<btn.length; i++) {
            for (int j = 0; j < btn[0].length; j++) {
                if (gameGrid[i][j]==0){
                    btn[i][j].setStyle("-fx-background-color:#632c02");
                }
                else if (gameGrid[i][j]==1){
                    btn[i][j].setStyle("-fx-background-color:#ff8a8a");
                }
                else if (gameGrid[i][j]==2){
                    btn[i][j].setStyle("-fx-background-color:#ff0000");
                }
                else if (gameGrid[i][j]==3){
                    btn[i][j].setStyle("-fx-background-color:#00ff00");
                }
                else if (gameGrid[i][j]==4){
                    btn[i][j].setStyle("-fx-background-color:#0b7d0b");
                }
                else if (gameGrid[i][j]==5){
                    btn[i][j].setStyle("-fx-background-color:#989e9b");
                }
                else if (gameGrid[i][j]==6){
                    btn[i][j].setStyle("-fx-background-color:#565c59");
                }
                else if (gameGrid[i][j]==7){
                    btn[i][j].setStyle("-fx-background-color:#12d8db");
                }
                else if (gameGrid[i][j]==8){
                    btn[i][j].setStyle("-fx-background-color:#272b2b");
                }
            }
        }
        updateListView();
    }

    public void updateListView(){
        //takes the last 10 events that occurred and puts them on the listview
        mainListView.getItems().clear();
        if (events.size()>0) {
            if (events.size() < 10) {
                for (int i = events.size() - 1; i > -1; i--) {
                    mainListView.getItems().add(events.get(i));
                }
            } else {
                for (int i = events.size() - 1; i > events.size() - 11; i--) {
                    mainListView.getItems().add(events.get(i));
                }
            }
        }
    }

    public void start() {
        startButton.setVisible(false);
        label1.setVisible(true);
        label2.setVisible(true);
        creatureSlider.setVisible(true);
        addCreatureButton.setVisible(true);
        time.add(0);

        series.get(0).setName("Zombies");
        series.get(1).setName("Goblins");
        series.get(2).setName("Werewolves");
        long mainStartTime = System.nanoTime();
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(creatures.size()>0) {
                    for (int i = 0; i < creatures.size(); i++) {
                        if (now - creatures.get(i).getStartTime() > 1000000000.0) {

                            //checks if there is more than one of the type of creature so that when it changes locations, it knows if it has the ability to do the calculated move
                            boolean moreThanOne = false;
                            if (creatures.get(i).getTypeOfCreature().equals("Zombie")){
                                if (zombiePopulation>1){
                                    moreThanOne = true;
                                }
                            }
                            if (creatures.get(i).getTypeOfCreature().equals("Goblin")){
                                if (goblinPopulation>1){
                                    moreThanOne = true;
                                }
                            }
                            if (creatures.get(i).getTypeOfCreature().equals("Werewolf")){
                                if (werewolfPopulation>1){
                                    moreThanOne = true;
                                }
                            }

                            //main movement functions
                            creatures.get(i).changeLoc(gameGrid, creatures, moreThanOne);
                            creatures.get(i).checkNeighbor(creatures, gameGrid, events);
                            creatures.get(i).resetStartTime();


                        }


                    }
                    //CheckDeath
                    for (int i = creatures.size() - 1; i >= 0; i--) {
                        if ((now - creatures.get(i).getAge() > 100000000000.0)) {
                            if (decideIfHappens(9)) {
                                gameGrid[creatures.get(i).getX()][creatures.get(i).getY()] = 0;
                                events.add("A " + creatures.get(i).getTypeOfCreature() + " died");
                                creatures.remove(i);
                            }
                        } else if ((now - creatures.get(i).getAge() > 70000000000.0)) {
                            if (decideIfHappens(3)) {
                                gameGrid[creatures.get(i).getX()][creatures.get(i).getY()] = 0;
                                events.add("A " + creatures.get(i).getTypeOfCreature() + " died");
                                creatures.remove(i);
                            }
                        } else if ((now - creatures.get(i).getAge() > 30000000000.0)) {
                            if (decideIfHappens(2)) {
                                gameGrid[creatures.get(i).getX()][creatures.get(i).getY()] = 0;
                                events.add("A " + creatures.get(i).getTypeOfCreature() + " died");
                                creatures.remove(i);
                            }
                        } else {
                            if (decideIfHappens(0)) {
                                gameGrid[creatures.get(i).getX()][creatures.get(i).getY()] = 0;
                                events.add("A " + creatures.get(i).getTypeOfCreature() + " died");
                                creatures.remove(i);
                            }
                        }

                    }
                    //change to adult
                    for (int i = 0; i < creatures.size(); i++) {
                        if (now - creatures.get(i).getAge() > 3000000000.0) {
                            creatures.get(i).changeAdult();
                        }
                    }
                    //update graph
                    if ((int) ((now - mainStartTime) / 1000000000) > time.get(time.size() - 1)) {
                        time.add(time.get(time.size() - 1) + 1);
                        calculatePopulations();
                        series.get(0).getData().add(new XYChart.Data<Number,Number>(time.get(time.size()-1), zombiePopulation));
                        series.get(1).getData().add(new XYChart.Data<Number,Number>(time.get(time.size()-1), goblinPopulation));
                        series.get(2).getData().add(new XYChart.Data<Number,Number>(time.get(time.size()-1), werewolfPopulation));
                        lChart.getData().clear();
                        lChart.getData().addAll(series);

                    }



                }
                //if a disaster has been on screen for 8 seconds, this code removes it
                if (disasters.size()>0){
                    for (int x = 0; x < disasters.size(); x++){
                        if ((now - disasters.get(x).getTime())/1000000000 > 8) {
                            System.out.println("check");
                            for (int i = disasters.get(x).getX(); i < disasters.get(x).getX()+disasters.get(x).getWidth(); i++) {
                                for (int j = disasters.get(x).getY(); j < disasters.get(x).getY() + disasters.get(x).getHeight(); j++) {
                                    if (gameGrid[i][j] > 6) {
                                        gameGrid[i][j] = 0;
                                    }
                                }
                            }
                            disasters.remove(x);
                        }
                    }
                }
                updateScreen();

            }

        }.start();
    }

    public void calculatePopulations(){
        //goes through the creatures array and calculates individual and total populations
        zombiePopulation = 0;
        goblinPopulation = 0;
        werewolfPopulation = 0;
        for (int i = 0; i < creatures.size(); i++){
            if (creatures.get(i).getTypeOfCreature().equals("Zombie")){
                zombiePopulation++;
            }
            if (creatures.get(i).getTypeOfCreature().equals("Goblin")){
                goblinPopulation++;
            }
            if (creatures.get(i).getTypeOfCreature().equals("Werewolf")){
                werewolfPopulation++;
            }
        }
        totalPopulation = zombiePopulation + goblinPopulation + werewolfPopulation;


    }

    public boolean checkEmpty(int i, int j){
        return gameGrid[i][j] ==0;
    }


    public boolean decideIfHappens(int chance){
        //can add as many cases as you would like with whatever probabilities you want
        switch (chance){
            case 0://Almost no chance of happening
                return randNum.nextInt(100000)>99990;
            case 1://slight chance of happening

                return randNum.nextInt(1000)>850;
            case 2://slightly greater chance of happening etc...
                return randNum.nextInt(1000)>950;
            case 3:
                return randNum.nextInt(1000)>600;
            case 4:
                return randNum.nextInt(1000)>400;
            case 5:
                return randNum.nextInt(1000)>350;
            case 6:
                return randNum.nextInt(1000)>300;
            case 7:
                return randNum.nextInt(1000)>200;
            case 8:
                return randNum.nextInt(1000)>100;
            case 9://95% chance of happening
                return randNum.nextInt(1000)>50;
            default://always happens
                return randNum.nextInt(1000)>0;
        }


    }


}
