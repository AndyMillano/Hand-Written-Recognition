import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DigitTrainer {

    public static void evalDigits(Network network, Matrix evalIn, Matrix evalOut, Matrix evalDigs) {
        float accumulatedRMS = 0f;
        int correct = 0;
        for (int i=0; i<evalIn.getCols(); i++){
        	Digit temp = new Digit((int) evalDigs.get(0,i));
            Matrix output = network.evaluate(evalIn.getOneInputColMatrix(i));
            accumulatedRMS += output.rmsError(evalOut.getOneInputColMatrix(i));
            @SuppressWarnings("static-access")
			int computed = temp.convertOutputToDigit(output);
            if (computed == temp.getDigit()) {
                correct++;
            }
        }
        System.out.println("rms = " + accumulatedRMS + " correct = " + ((float) correct)/evalDigs.getRows());
    }

    public static void main(String[] args) throws IOException {
        System.out.print("Loading data...");
        String s1 = "cw2DataSet1.csv";
        String s2 = "cw2DataSet2.csv";
        
        List<Matrix> dataMatrices = ReadFile.readingMatrices(s1);
        Matrix input = dataMatrices.get(0);
        Matrix output = dataMatrices.get(1);
        @SuppressWarnings("unused")
		Matrix trainDigVals = dataMatrices.get(2);
        
        List<Matrix> testMatrices = ReadFile.readingMatrices(s2);
        Matrix testInput = testMatrices.get(0);
        Matrix testOutput = testMatrices.get(1);
        Matrix testDigVals = testMatrices.get(2);
        
        List<Network.TrainingPair> dataset = new ArrayList<>();

          for (int i=0; i<input.getCols(); i++){
               dataset.add(new Network.TrainingPair(input.getOneInputColMatrix(i), output.getOneInputColMatrix(i)));
          }

        System.out.println("Done");

        System.out.println(input.getCols() + " training samples, " + testInput.getCols() + " test samples");

        Network network = new Network(8 * 8, 25, 10);
        evalDigits(network, testInput, testOutput, testDigVals);

        int numEpochs = 500;
        for (int i = 0; i < numEpochs; i++) {
            System.out.print("Training (iteration " + (i + 1) + " of " + (numEpochs) + ") ... ");
            network.train(dataset, 10, 3f);
            System.out.println("Done");
            evalDigits(network, testInput, testOutput, testDigVals);
        }

        DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream("outputNet")));
        network.save(out);
        out.close();
    }
}
