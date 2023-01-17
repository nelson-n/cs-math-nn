public class JavaHelpers {

    // Method for printing an array of integers.
    static void PrintArray(int[] intArray) {
        System.out.print("[");
        for(int i = 0; i < intArray.length - 1; i++) System.out.print(intArray[i] + ", ");
        System.out.print(intArray[intArray.length - 1]);
        System.out.print("]");
        System.out.println("");
    }

    // Method for generating random integers.
    public static int GenerateRandomInt(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    // Generate a random array of integers.
    public static void main(String[] args) {
        int[] myArray = new int[10];
        for(int i = 0; i < myArray.length; i++) myArray[i] = GenerateRandomInt(0, 10);
        PrintArray(myArray);
    }





    
}
