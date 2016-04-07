
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 */
public class DigitRecognizer {

    private static boolean verbose = true;

    public static void main(String[] args) throws IOException, InterruptedException {
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(args[0])));
        Network network = Network.load(in);
        in.close();
        String s = "cw2DataSet2.csv";
        List<Matrix> testMatrices = ReadFile.readingMatrices(s);
        Matrix testInput = testMatrices.get(0);
        Matrix testOutput = testMatrices.get(1);
        Matrix testDigVals = testMatrices.get(2);
        int correctCount=0;
        for (int i=0; i<testInput.getCols(); i++) {
            
            Matrix output = network.evaluate(testInput.getOneInputColMatrix(i));
            Digit.DigitScore[] scores = Digit.getDigitsOrderedByScore(output);
            
            if(scores[0].digit==(int)testDigVals.get(0, i)){
            	correctCount = correctCount+1;
            }
            
            System.out.println("Original ( "+ testDigVals.get(0, i) + " )"+ scores[0].digit + " (" + scores[0].score + ")  "
                    + "[ Others "
                    + scores[1].digit + " (" + scores[1].score + ")  "
                    + scores[2].digit + " (" + scores[2].score + ") ]");
            if (verbose) {
                System.out.println();
            }
        }
        float acc = 0;
        acc = ((float)(correctCount)/(float)testOutput.getCols())*100;
        System.out.println(correctCount + " No. of Correctly Classified Examples\n");
        System.out.println(testOutput.getCols() + " Total No. of Test Examples\n");
        System.out.println("Accuracy percentage : " + acc +" \n");
        if (!verbose) {
            System.out.println();
        }
    }
}
