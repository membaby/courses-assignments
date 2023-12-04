import java.math.BigDecimal;
import java.math.BigInteger;

public class Fibonacci {

	public static BigInteger[][] multiply2x2Matrices(BigInteger[][] a, BigInteger[][] b){
		BigInteger c00 = a[0][0].multiply(b[0][0]).add(a[0][1].multiply(b[1][0]));
		BigInteger c01 = a[0][0].multiply(b[0][1]).add(a[0][1].multiply(b[1][1]));
		BigInteger c10 = a[1][0].multiply(b[0][0]).add(a[1][1].multiply(b[1][0]));
		BigInteger c11 = a[1][0].multiply(b[0][1]).add(a[1][1].multiply(b[1][1]));
		return new BigInteger[][] {{c00, c01},{c10, c11}};
	}

	public static BigInteger[][] matPower(BigInteger[][] mat, BigInteger exponent) {
		if (exponent.equals(BigInteger.ZERO)) {
			BigInteger[][] IdentityMatrix = new BigInteger[mat.length][mat.length];
			for (int i=0; i<mat.length; i++) {
				IdentityMatrix[i][i] = BigInteger.ONE;
				for (int j=0; j<mat.length; j++) {
					if (j != i) IdentityMatrix[i][j] = BigInteger.ZERO;
				}
			}
			
			return IdentityMatrix;

		} else if (exponent.mod(BigInteger.valueOf(2)).equals(BigInteger.ZERO)) {
			BigInteger[][] HalfPowerMatrix = matPower(mat, exponent.divide(BigInteger.valueOf(2)));
			return multiply2x2Matrices(HalfPowerMatrix, HalfPowerMatrix);
		
		} else {
			return multiply2x2Matrices(mat, matPower(mat, exponent.subtract(BigInteger.ONE)));
		}
	}

	public static BigInteger getNthFibonacciNumber(BigInteger n) {	
		BigInteger[][] b = new BigInteger[][] {{BigInteger.ZERO, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ONE}};
		return matPower(b, n)[1][0];
	}
	
	public static BigDecimal getNthFibonacciNumberRatio(BigInteger n) {
		BigInteger[][] b = new BigInteger[][] {{BigInteger.ZERO, BigInteger.ONE}, {BigInteger.ONE, BigInteger.ONE}};
		BigInteger[][] c = matPower(b, n);
		BigDecimal F1 = new BigDecimal(c[1][0]);
		BigDecimal F2 = new BigDecimal(c[0][0]);
		return F1.divide(F2, new java.math.MathContext(1000));
	}
	
	public static void main(String[] args) {
		System.out.println("Fibonacci 10000th Number is:");
		System.out.println(getNthFibonacciNumber(BigInteger.valueOf(10000)));
		System.out.println("\nFibonacci 10000th Number Ratio (F10000 / F9999) is:");
		System.out.println(getNthFibonacciNumberRatio(BigInteger.valueOf(10000)));
	}
}