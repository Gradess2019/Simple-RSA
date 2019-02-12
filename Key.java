import java.math.BigInteger;

public class Key {
    private final BigInteger multiplication;
    private final int exponent;

    public Key(int exponent, BigInteger multiplication) {
        this.exponent = exponent;
        this.multiplication = multiplication;
    }

    public BigInteger getMultiplication() {
        return multiplication;
    }

    public int getExponent() {
        return exponent;
    }
}
