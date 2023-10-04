package strassenMethod;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
/**This package contains two algorithms to multiply two matrices. 
 * One is the Strassen algorithm, and the other is the Naive algorithm.
 * The program prints the input and solution as output.
 * The program prints out the total number of multiplications by each algorithm. 
 * @author Anam Ahsan
 * @param sourceFilepath is the file to be read
 * @param destinationFilepath is the file to be written into
 */
public class Strassen {
   public static int multiCountS = 0; //Global variable to count number of Strassen multiplications 
   public static int multiCountO = 0; //Global variable to count number of Naive multiplications 
   
   /*
    * Main entry point for the application, reads input file and creates output file.
    */
   public static void main(String[] args) throws IOException {
   //Command line arguments for user to enter a source and destination file path
   String sourceFilePath = args[0]; 
   String destinationFilepath = args[1];
			   	 
      BufferedReader reader; //Initializes the reader
	  FileWriter outputFile = new FileWriter(destinationFilepath); //Initializing output file
	  //FileWriter outputFile = new FileWriter("C:\\Users\\Ahsan\\Desktop\\Algorithms2022\\Program 1\\RESULT.txt");
	  int arrSize = 0;		//Initializes variable to get array size
	
	  int [][] aMatrix = {{}}; //Initialized the Matrix A
	  int [][] bMatrix = {{}}; //Initialized the Matrix B
	                  
	  try {
	     reader = new BufferedReader(new FileReader(sourceFilePath)); //Initializing file to be read
	  	 //reader = new BufferedReader(new FileReader("C:\\Users\\Ahsan\\Desktop\\Algorithms2022\\Program 1\\test.txt"));
	     String line = reader.readLine(); //Read each line of file
	      
	     //Create arrays with input from source file 
	     while (line != null) {
	        if (line.length() < 3) {
	           int s = Integer.parseInt(line);
	           arrSize = s; 
	           aMatrix = new int[arrSize][arrSize];
	           bMatrix = new int[arrSize][arrSize];
	                    
	           //Fills array with elements from source file 
	           fillMatrix(arrSize, reader, aMatrix, bMatrix, outputFile, multiCountS, multiCountO);
	           //Skip lines to move on to next matrices in source file 
	           line = reader.readLine(); 
	           line = reader.readLine();
	        }
	        else {
	           //Error message if source file is empty
	           System.out.println("Input error!");
		       outputFile.write("Input error!");
		       }
		       }
		   //Close input reader
		      reader.close();
		      outputFile.close();
		      } 
		      //This allows exceptions to be caught and traced to the line of code
		   catch (IOException e) {
		      e.printStackTrace();
		      }
		}

    /*
     * This method iterates through constructed arrays to fill with elements from matrices in input file
     * It calls the methods for the algorithms to calculate solutions.
     * It calls the print method when arrays are filled.
   */
	private static void fillMatrix(int arrSize, BufferedReader reader, int[][] aMatrix, int[][] bMatrix, FileWriter outputFile, int mS, int mO) throws IOException {
		String[] line = null;
		int matrixSize = arrSize;  //Variable for array size 
		//Initialize matrices for Strassen and Naive solution 
		int [][] cMatrix = {{}};
		int [][] oMatrix = {{}};
		cMatrix = new int[arrSize][arrSize]; //Make matrix for Strassen matrix correct size 
		//Input elements into array from source file for Matrix 1
		for(int i=0; i < arrSize; i++){
			line = reader.readLine().split(" ");
			if(line.length != matrixSize){
				//If matrix cannot fit in array, give error message 
				System.out.println("ERROR, INPUT LINE IS NOT LENGTH OF MATRIX");
				outputFile.write("ERROR, INPUT LINE IS NOT LENGTH OF MATRIX");
	            }
	        	for(int j = 0; j < arrSize; j++){	        	   
	               aMatrix[i][j] = Integer.parseInt(line[j]);
	               }
	      }
		//Input elements into array from source file for Matrix 1
		for(int i=0; i < arrSize; i++){
			line = reader.readLine().split(" ");
			if(line.length != matrixSize){
				//If matrix cannot fit in array, give error message 
				System.out.println("ERROR, INPUT LINE IS NOT LENGTH OF MATRIX");
				outputFile.write("ERROR, INPUT LINE IS NOT LENGTH OF MATRIX");
	            }
	        	for(int j = 0; j < arrSize; j++){
	                bMatrix[i][j] = Integer.parseInt(line[j]);
	               }
	      }
	    //After input arrays are filled, methods for each respective algorithm are called
	    cMatrix = mainStrass(aMatrix, bMatrix);  
	    oMatrix = ordinary(aMatrix, bMatrix);
	    
	    //Print method is called to print input arrays, solution arrays and multiplication counts to output file
	    System.out.println("\nOrder size: " + matrixSize);
	    System.out.println("\nInput Matrix 1\n-----");
	    outputFile.write("\nOrder size: " + matrixSize);
	    outputFile.write("\nInput Matrix 1\n-----\n");
	    printMatrix(aMatrix, outputFile);
	    System.out.println("\nInput Matrix 2\n-----");
	    outputFile.write("\nInput Matrix 2\n-----\n");
	    printMatrix(bMatrix, outputFile);
	    System.out.println("\nStrass Matrix Solution\n-----");
	    outputFile.write("\nStrass Matrix Solution\n-----\n");
		printMatrix(cMatrix, outputFile);
		System.out.print("\nStrassen Multiplications: " + multiCountS + "\n");
		outputFile.write("\nStrassen Multiplications: " + multiCountS + "\n");
		
		//Re-initialize Strassen multiplication count for new input matrices after printing 
		multiCountS = 0;
		
		System.out.println("\nNaive Matrix Solution\n-----");
		outputFile.write("\nNaive Matrix Solution\n-----\n");
		printMatrix(oMatrix, outputFile);
		System.out.print("\nNaive Multiplications: " + multiCountO + "\n");
		outputFile.write("\nNaive Multiplications: " + multiCountO + "\n");
		
		//Re-initialize Naive multiplication count for new input matrices after printing 
		multiCountO = 0;
	   }

	
    /*
     * This method multiplies the two input matrices using the Strassen Algorithm
     * It calls the methods, partition, add and subtract to do the computations
     * It returns the solution matrix 
   */
	private static int [][] mainStrass(int[][] aMatrix, int[][] bMatrix){
		
		//Get size of input and create new array with it
		int size = aMatrix.length;
		int[][]cMatrix = new int [size][size];
		
		//Initialize variable to partition a matrix or combine it 
		boolean split = false;
		
		//This step serves as a check to multiply matrices of the correct size
		if (size == 1) {
			cMatrix[0][0] = aMatrix[0][0] * bMatrix[0][0];
			//Count every time a multiplication is performed 
			multiCountS += 1;
			
		}
		//This step partitions the matrices, does the 10 summations
		//and 7 recursive multiplications
		else {
			//Initialize array for partition of input matrix A
			int sizeN = size/2;
			int[][] aOne = new int[sizeN][sizeN];		
	        int[][] aTwo = new int[sizeN][sizeN];
	        int[][] aThree = new int[sizeN][sizeN];
	        int[][] aFour = new int[sizeN][sizeN];
	            
	      //Initialize array for partition of input matrix B
	        int[][] bOne = new int[sizeN][sizeN];
	        int[][] bTwo = new int[sizeN][sizeN];
	        int[][] bThree = new int[sizeN][sizeN];
	        int[][] bFour = new int[sizeN][sizeN];
	         
	        //Partition matrix A 
	        
	        //Change boolean to true to allow partitioning of matrices 
	        split = true; 
	        partition(aMatrix, aOne, 0, 0, split); 
	        partition(aMatrix, aTwo, 0, sizeN, split);
	        partition(aMatrix, aThree, sizeN, 0, split); 
	        partition(aMatrix, aFour, sizeN, sizeN, split);
				  
	        //Partition matrix B  
	        partition(bMatrix, bOne, 0, 0, split);
			partition(bMatrix, bTwo, 0, sizeN, split); 
			partition(bMatrix, bThree, sizeN, 0, split); 
			partition(bMatrix, bFour, sizeN, sizeN, split);
				 
	        //The ten summations through addition and subtraction 
			int [][] sumOne = subtract(bTwo, bFour);
			int [][] sumTwo = add(aOne, aTwo);
			int [][] sumThree = add(aThree, aFour);
			int [][] sumFour = subtract(bThree, bOne);
			int [][] sumFive = add(aOne, aFour);
			int [][] sumSix = add(bOne, bFour);
			int [][] sumSeven = subtract(aTwo, aFour);
			int [][] sumEight = add(bThree, bFour);
			int [][] sumNine = subtract(aOne, aThree);
			int [][] sumTen = add(bOne, bTwo);
	         
			//The seven recursive multiplications 
	        int[][] pOne = mainStrass(aOne, sumOne);
	        int[][] pTwo = mainStrass(sumTwo, bFour);
	        int[][] pThree = mainStrass(sumThree, bOne);           
	        int[][] pFour = mainStrass(aFour, sumFour);
	        int[][] pFive = mainStrass(sumFive, sumSix);
	        int[][] pSix = mainStrass(sumSeven, sumEight);
	        int[][] pSeven = mainStrass(sumNine, sumTen);   
	        
	        //The final four submatrices to calculate solutions 
			int[][] cOne = add(subtract(add(pFive, pFour), pTwo), pSix); 
			int[][] cTwo = add(pOne, pTwo); 
			int[][] cThree = add(pThree, pFour); 
			int[][] cFour = subtract(subtract(add(pFive, pOne), pThree), pSeven);
				  
			//Change boolean to false to allow combining of matrices 
			split = false;
			//Call partition method to insert matrices into final solution array
			partition(cOne, cMatrix, 0, 0, split);
			partition(cTwo, cMatrix, 0, sizeN, split); 
			partition(cThree, cMatrix, sizeN, 0, split);
			partition(cFour, cMatrix, sizeN, sizeN, split);
				 
		}		
		return cMatrix; //Final array with Strassen Algorithm solution 
	}
	
    /*
     * This method partitions arrays or puts together matrices in an array 
     * dependent on the boolean variable 'choice'
   */
    public static void partition(int[][] aMatrix, int[][] bMatrix, int row, int cols, boolean choice){
    	int L; //initialize variable for length
    	if (choice){
    		//Using array size iterate through array A and split into new array B
    	   L = bMatrix. length;
		   for(int iA = 0, iB = row; iA < L; iA++, iB++)
		      for(int jA = 0, jB = cols; jA < L; jA++, jB++)
			     bMatrix[iA][jA] = aMatrix[iB][jB];
    	}
    	else {
    	   //Using array size iterate through arrays A and put into new array B
    	   L = aMatrix. length;
           for(int iA = 0, iB = row; iA < L; iA++, iB++)
              for(int jA = 0, jB = cols; jA < L; jA++, jB++)
                 bMatrix[iB][jB] = aMatrix[iA][jA];
    	}
 
    }
	
    /*
     * This method iterates through the two matrices and adds each value
     * the sum is put into a new matrix called 'sum'
   */
    public static int[][] add(int[][] aMatrix, int[][] bMatrix) {
        int n = aMatrix.length;
        int[][] sum = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum[i][j] = aMatrix[i][j] + bMatrix[i][j];
            }
        }
        return sum;
    }
	
    /*
     * This method iterates through the two matrices and subtracts each value
     * the output is put into a new matrix called 'sum'
   */
    public static int[][] subtract(int[][] aMatrix, int[][] bMatrix) {
        int n = aMatrix.length;
        int[][] sum = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                sum[i][j] = aMatrix[i][j] - bMatrix[i][j];
            }
        }
        return sum;
    }
    
    /*
     * This method multiplies the two input matrices using the Naive Algorithm
     * it directly multiplies elements by iterating through the arrays 
     * It returns the solution matrix in a new array 
   */
    public static int[][] ordinary(int [][] aMatrix, int [][] bMatrix){
    	int L = aMatrix.length;
		int[][]oMatrix = new int [L][L];
        for (int i = 0; i < L; i++){
            for (int j = 0; j < L; j++){
                oMatrix[i][j] = 0;               
                for (int k = 0; k < L; k++){
                    oMatrix[i][j] += aMatrix[i][k]*bMatrix[k][j];
                    
                    //Count every time a multiplication is performed 
                    multiCountO += 1;
                }
            }
        }        
        return oMatrix;
    }
	
    /*
     * This method prints matrices in the correct form 
   */
    private static void printMatrix(int[][] aMatrix, FileWriter outputFile) throws IOException{    
		for(int i = 0; i< aMatrix.length; i++){
			for(int j = 0; j<aMatrix[0].length; j++){
				System.out.print(aMatrix[i][j] + " ");		
		        int b = aMatrix[i][j];
		        outputFile.write(b + " ");
		        }
		    System.out.println();		    
		    outputFile.write("\n");
		        }
		    }
}
