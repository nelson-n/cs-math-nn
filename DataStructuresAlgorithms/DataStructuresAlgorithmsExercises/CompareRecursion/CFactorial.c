
#include<stdio.h>  

// Recursively compute factorials.
int factorial(int n) {
    if (n == 0) {
        return 1;
    } else {
        return n * factorial(n - 1);
    }
}

int main() {
    int i;
    for (i = 1; i <= 100; i++) {
        factorial(i);
    }

}
