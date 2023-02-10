
public class javaFactorial {

    // Recursively compute factorials.
    public static int factorial(int n) {
        int result;
        if(n == 0)
            return 1;
        else
            return factorial(n - 1) * n;
    }

    public static void main(String args[]) {
        int i;
        for(i = 0; i < 100; i++)
            factorial(i);
    }
}