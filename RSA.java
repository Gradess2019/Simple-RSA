import java.math.BigInteger;

public class RSA {

    private int firstNum;
    private int secondNum;
    private BigInteger multiplication;
    private BigInteger euler;
    private int openExponent;
    private int secretExponent;
    private Key publicKey;
    private Key privateKey;

    private static final int DELTA_RANGE = 15;
    private static final int DEFAULT_MIN = 150;
    private static final int DEFAULT_MAX = 450;

    RSA(long min, long max) {
        if (min > max) {
            min ^= max;
            max ^= min;
            min ^= max;
        }

        if ((max - min) < DELTA_RANGE) {
            min = DEFAULT_MIN;
            max = DEFAULT_MAX;
            System.err.println("Диапазон слишком мал!\nНовые значения: \nmin = " + min + "\nmax = " + max);
        }

        if (min > 5000 || max > 5000) {
            min = DEFAULT_MIN;
            max = DEFAULT_MAX;
            System.err.println("Диапазон слишком большой!\nНовые значения: \nmin = " + min + "\nmax = " + max);
        }


        firstNum = getPrimeNum(min, max);
        secondNum = getPrimeNum(min, max);

        multiplication = BigInteger.valueOf(firstNum * secondNum);

        euler = BigInteger.valueOf((firstNum - 1) * (secondNum - 1));
        if (euler.equals(BigInteger.ZERO)) {
            throw new RuntimeException("Euler function equals 0!");
        }

        openExponent = setOpenExponent();
        secretExponent = setSecretExponent();

        publicKey = new Key(openExponent, multiplication);
        privateKey = new Key(secretExponent, multiplication);
    }

    public Key getPublicKey() {
        return publicKey;
    }

    public Key getPrivateKey() {
        return privateKey;
    }

    private int getPrimeNum(long min, long max) {
        int resultNum;
        while (true) {
            resultNum = (int) (Math.random() * (max - min) + min);
            if (isPrime(resultNum)) {
                return resultNum;
            }
        }
    }

    private int setOpenExponent() {
        int resultNum;
        //noinspection ConstantConditions
        while (true) {
            resultNum = getPrimeNum(0, euler.longValue() - 1);
            if (!euler.mod(BigInteger.valueOf(resultNum)).equals(BigInteger.valueOf(0))) {
                break;
            }
        }
        if (isValidExponent(resultNum)) {
            return resultNum;
        } else {
            throw new RuntimeException("Not found open exponent!");
        }
    }

    private int setSecretExponent() {
        int resultNum;
        //noinspection ConstantConditions
        for (resultNum = 3; resultNum < euler.longValue(); resultNum++) {
            if ((BigInteger.valueOf(resultNum * openExponent)).mod(euler).equals(BigInteger.ONE)) {
                break;
            }
        }
        if (isValidExponent(resultNum)) {
            return resultNum;
        } else {
            throw new RuntimeException("Not found secret exponent!");
        }

    }

    private boolean isValidExponent(int exponent) {
        return !BigInteger.valueOf(exponent).equals(euler);
    }

    public BigInteger encrypt(Key publicKey, BigInteger msg) {
        return (raiseToPower(msg, publicKey.getExponent()).mod(publicKey.getMultiplication()));
    }

    public long[] encrypt(Key publicKey, String msg) {
        byte[] bytesMsg = msg.getBytes();
        long[] encryptMsg = new long[bytesMsg.length];
        for (int index = 0; index < bytesMsg.length; index++) {
            encryptMsg[index] = (encrypt(publicKey, BigInteger.valueOf(bytesMsg[index]))).longValue();
        }
        return encryptMsg;
    }

    public BigInteger decrypt(Key privateKey, BigInteger encryptMsg) {
        return (raiseToPower(encryptMsg, privateKey.getExponent()).mod(privateKey.getMultiplication()));
    }

    public String decrypt(Key publicKey, long[] encryptMsg) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < encryptMsg.length; index++) {
            builder.append((char) (decrypt(privateKey, BigInteger.valueOf(encryptMsg[index]))).byteValue());
        }
        return builder.toString();
    }

    private BigInteger raiseToPower(BigInteger number, int power) {
        BigInteger resultNum = BigInteger.ONE;
        while (power > 0) {
            if (power % 2 == 1) {
                resultNum = resultNum.multiply(number);
            }
            number = number.multiply(number);
            power /= 2;
        }
        return resultNum;
    }

    private boolean isPrime(int number) {
        if (number % 2 == 0) {
            return false;
        } else {
            for (int divider = 3; divider <= number / 2; divider++) {
                if (number % divider == 0) {
                    return false;
                }
            }
            return true;
        }
    }

    @Override
    public String toString() {
        return "FirstNum: " + firstNum + "\n" +
                "SecondNum: " + secondNum + "\n" +
                "Multiplication: " + multiplication + "\n" +
                "Euler: " + euler + "\n" +
                "OpenExponent: " + openExponent + "\n" +
                "SecretExponent: " + secretExponent + "\n" +
                "PublicKey: " + publicKey.getExponent() + " " + publicKey.getMultiplication() + "\n" +
                "PrivateKey: " + privateKey.getExponent() + " " + privateKey.getMultiplication();

    }
}
