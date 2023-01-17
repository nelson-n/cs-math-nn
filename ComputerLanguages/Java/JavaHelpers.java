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
    
}
