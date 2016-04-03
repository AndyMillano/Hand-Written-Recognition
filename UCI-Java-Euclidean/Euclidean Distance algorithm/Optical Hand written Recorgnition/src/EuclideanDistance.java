/*
 * OPTICAL RECORGNITION OF HANDWRITTEN DIGIT DATA SET ALGORITHIM
 * Implemented by Renan Augusto Da Silva Gado
 * This algorithm categorises on of the UCI digit task using Euclidean distance methodology.
 * This algorithm produces a 99 average result based on two a fold test on given data.
 * */

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Scanner;

import javax.swing.JOptionPane;

public class EuclideanDistance {
    static int col = 0;
    static String [] inArray;
    static int[][]dataSetList;
    static int row = 0;
    static int numberOfRows = 2810;
    
    //THIS METHOD READS THE DATASET FILE REMOVES THE "," AND SETS UP THE VALUE INTO AN ARRAY OF DATA
    public static void setUpCSVArray(){
        Scanner scanIn = null;
        dataSetList = new int[numberOfRows][65];
        int rowC = 0;
        String InputLine = "";
        try{
            scanIn = new Scanner(new BufferedReader(new FileReader("cw2DataSet1.csv")));
            //scanIn = new Scanner(new BufferedReader(new FileReader("cw2DataSet2.csv")));
            while(scanIn.hasNextLine()){
                //read line in from file
                InputLine = scanIn.nextLine();
                //split the inputline into an array at the commas
                inArray = InputLine.split(",");   
                //copy values of inArray into myArray
                for(int x = 0; x < inArray.length; x++){
                	dataSetList[rowC][x]= Integer.parseInt(inArray[x]);                    
                }
                rowC++;
            }         
        }
        catch(Exception e){
            System.out.println(e);
        }
    }
   
    //calculates the distance between digits/rows (each row represents a digit)
    private static int distance(int first, int second){
        int totalDistance = 0;
        for (int vectorItem = 0; vectorItem < 65; vectorItem ++) {
            int distance = dataSetList[first][vectorItem] - dataSetList[second][vectorItem];
            //square the values
            distance = distance * distance;
            totalDistance = totalDistance + distance;
        
            //System.out.print(distance + " ");
        }
        return totalDistance;
    }
    
    //GETS THE MINIMUN DISTANCE BETWEEN ROWS AND RETURN ROW
    public static int getMinDistance(int [] distanceArray){
    	int minDistance = 0, minDistanceIndex = 0;
    	// if distance is not zero
    	if(distanceArray[0] != 0){
    		minDistance = distanceArray[0];
    		}
		for (int counter = 0; counter < distanceArray.length; counter++){
			if(distanceArray[counter] <= minDistance   && distanceArray[counter] != 0){
				minDistance = distanceArray[counter];
				minDistanceIndex = counter;
			}
		}
		return minDistanceIndex;   	
    }
    
    // test data and return the amount of correct result
    public static int test(){
        int [] distanceList = new int[numberOfRows];
        int [] listOfCorrectDigits = new int[numberOfRows];
        int category = 0;  
        for (int row = 0; row < numberOfRows; row++) {
        		for (int comparedRow =0; comparedRow < numberOfRows; comparedRow++){
        			distanceList[comparedRow] = distance(comparedRow,row);
        			category = getMinDistance(distanceList);	
        			if (dataSetList[row][64] == dataSetList[category][64]){ 
        				listOfCorrectDigits[row] = 1;
        			}
            }
        }
        int correctDigits = 0;
        for(int counter = 0; counter< numberOfRows; counter++){
    		if(listOfCorrectDigits[counter] == 1) 
    			correctDigits++;
        }
        return correctDigits;
    }
    
    public static void main(String[] args) {
        setUpCSVArray();
        int numberOfCorrectDigits = test();
        System.out.println("The number of correct digits are "+numberOfCorrectDigits + " out of "+numberOfRows);
        // Calculate the percentage
        float percentage;
        percentage = (float)(numberOfCorrectDigits * 100) / numberOfRows;
        System.out.println("The percentage is: "+ percentage+"%");
        JOptionPane.showMessageDialog(null,"The number of correct digits are "+numberOfCorrectDigits + " out of "+ numberOfRows + "\nThe percentage is: "+ percentage+"%");
    }
}