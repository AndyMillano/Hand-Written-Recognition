import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ReadFile {
	static int col = 0;
	static String [] inArray;
	static int[][]digitList;
	static int row = 0;
	
	@SuppressWarnings("unused")
	public static void setUpCSVArray(String s){
		Scanner scanIn = null;
		digitList = new int[2810][65];
		int rowC = 0;
		int row = 0;
		int colc = 0;
		int col = 0;
		String InputLine = "";
		int xnum = 0;
		
		try{
			scanIn = new Scanner(new BufferedReader(new FileReader(s)));
			while(scanIn.hasNextLine()){
				
				//read line in from file
				InputLine = scanIn.nextLine();
				//split the inputline into an array at the commas
				inArray = InputLine.split(",");
				
				//copy values of inArray into myArray
				for(int x = 0; x < inArray.length; x++){
					digitList[rowC][x]= Integer.parseInt(inArray[x]);					
				}
				rowC++;
			}
			
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	public static List<Matrix> readingMatrices(String s) {
		setUpCSVArray(s);
		Matrix input = new Matrix(2810, 8*8);
		Matrix output = new Matrix(2810, 10);
		Matrix outputInDigit = new Matrix(2810,1);
		
		for(int row=0; row<2810; row++){
			for(int column=0; column<64; column++){
				input.set(row, column, digitList[row][column]);
			}
		}
		
		for(int row=0; row<2810; row++){
			output.set(row, digitList[row][64], 1);
			outputInDigit.set(0, row, digitList[row][64]);
		}
		input = input.transpose();
		output = output.transpose();
		
		List<Matrix> dataMatrices = new ArrayList<>();
		dataMatrices.add(input);
		dataMatrices.add(output);
		dataMatrices.add(outputInDigit);
		return dataMatrices;
}    
}