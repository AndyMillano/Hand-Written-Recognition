import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class Matrix {

    private float[] data;
    private int rows;
    private int cols;

    public Matrix(int rows, int cols) {
        this(rows, cols, 0.0f);
    }

    public Matrix(int rows, int cols, float defaultValue) {
        this.rows = rows;
        this.cols = cols;
        data = new float[rows * cols];
        if (defaultValue != 0.0f) {
            setAll(defaultValue);
        }
    }

    public Matrix(float[][] data) {
        this.rows = data.length;
        this.cols = data[0].length;
        this.data = new float[rows * cols];
        for (int counter = 0; counter < rows; counter++) {
            for (int counter1 = 0; counter1 < cols; counter1++) {
                this.data[counter * cols + counter1] = data[counter][counter1];
            }
        }
    }

    public void save(DataOutputStream out) throws IOException {
        for (int counter = 0, rowCount = getRows(); counter < rowCount; counter++) {
            for (int counter1 = 0, colCount = getCols(); counter1 < colCount; counter1++) {
                out.writeFloat(get(counter, counter1));
            }
        }
    }

    public void load(DataInputStream in) throws IOException {
        for (int counter = 0, rowCount = getRows(); counter < rowCount; counter++) {
            for (int counter1 = 0, colCount = getCols(); counter1 < colCount; counter1++) {
                set(counter, counter1, in.readFloat());
            }
        }
    }

    public Matrix clone() {
        Matrix m = new Matrix(rows, cols);
        m.setFrom(this);
        return m;
    }

    public void setFrom(Matrix m) {
        System.arraycopy(m.data, 0, data, 0, data.length);
    }

    public float get(int iRow, int jCol) {
        return data[iRow * cols + jCol];
    }
    
    public Matrix getOneInputColMatrix(int index){
    	int r = this.rows;
    	Matrix temp = new Matrix(r,1);
    	for(int counter=0; counter<r; counter++){
    		temp.set(counter, 0, this.get(counter, index));
    	}
    	return temp;
    }
    
    public void set(int iRow, int jCol, float value) {
        data[iRow * cols + jCol] = value;
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public Matrix times(Matrix m) {
        if (getCols() != m.getRows()) {
            throw new IllegalArgumentException("Can't multiply matrix with " + getCols() + " cols with matrix with " + m.getRows() + " rows");
        }

        Matrix product = new Matrix(getRows(), m.getCols());

        for (int i = 0, rowCount = product.getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = product.getCols(); j < colCount; j++) {
                float term = 0;

                for (int k = 0, kCount = getCols(); k < kCount; k++) {
                    term += get(i, k) * m.get(k, j);
                }

                product.set(i, j, term);
            }
        }

        return product;
    }

    /**
     * Computes the "Hadamard" product, which is simply multiplying
     * corresponding elements.  So both input matrices and the
     * output matrix must/will be the same size.
     */
    public Matrix hadamardTimes(Matrix m) {
        if (getCols() != m.getCols() || getRows() != m.getRows()) {
            throw new IllegalArgumentException("Can't array multiply matrices of different dimensions (" + getRows() + "," + getCols() + ") vs (" + m.getRows() + "," + m.getCols() + ")");
        }

        Matrix product = new Matrix(getRows(), getCols());

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                product.set(i, j, get(i, j) * m.get(i, j));
            }
        }

        return product;
    }

    public Matrix plus(Matrix m) {
        if (getCols() != m.getCols() || getRows() != m.getRows()) {
            throw new IllegalArgumentException("Can't add matrices of different dimensions (" + getRows() + "," + getCols() + ") vs (" + m.getRows() + "," + m.getCols() + ")");
        }

        Matrix sum = new Matrix(getRows(), getCols());

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                sum.set(i, j, get(i, j) + m.get(i, j));
            }
        }

        return sum;
    }

    public Matrix minus(Matrix m) {
        if (getCols() != m.getCols() || getRows() != m.getRows()) {
            throw new IllegalArgumentException("Can't subtract matrices of different dimensions (" + getCols() + "," + getRows() + " vs " + m.getCols() + "," + m.getRows() + ")");
        }

        Matrix sum = new Matrix(getRows(), getCols());

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                sum.set(i, j, get(i, j) - m.get(i, j));
            }
        }

        return sum;
    }

    public Matrix scalarTimes(float c) {
        Matrix product = new Matrix(getRows(), getCols());

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                product.set(i, j, get(i, j) * c);
            }
        }

        return product;
    }

    public Matrix arrayPow(float exponent) {
        Matrix m = new Matrix(getRows(), getCols());

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                m.set(i, j, (float) Math.pow(get(i, j), exponent));
            }
        }

        return m;
    }

    public float sum() {
        float sum = 0;

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                sum += get(i, j);
            }
        }

        return sum;
    }

    public void setAll(float value) {
        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                set(i, j, value);
            }
        }
    }

    public Matrix transpose() {
        Matrix m = new Matrix(getCols(), getRows());

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                m.set(j, i, get(i, j));
            }
        }
        return m;
    }
    
    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Matrix)) {
            return false;
        }

        Matrix m = (Matrix)object;
        if (getRows() != m.getRows() || getCols() != m.getCols()) {
            return false;
        }

        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                if (get(i, j) != m.get(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }

    public float rmsError(Matrix m) {
        return (float)Math.sqrt(this.minus(m).arrayPow(2.0f).sum() / getRows());
    }

    // Can I make this thing static?  I assume so?
    private static NumberFormat format = new DecimalFormat("#.#####;-#.#####");

    public void print(PrintStream out) {
        for (int i = 0, rowCount = getRows(); i < rowCount; i++) {
            out.print("[ ");
            for (int j = 0, colCount = getCols(); j < colCount; j++) {
                out.print(format.format(get(i, j)));
                out.print(' ');
            }
            out.println("]");
        }
    }
}
