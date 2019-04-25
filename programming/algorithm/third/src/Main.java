import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.IntStream;

public class Main {

    public static void main(String[] args) throws IOException {
        int[] numbers = readNumbers();
        int lgN = 0;
        while ((1 << lgN) < numbers.length)  ++lgN;
        for (int i=0; i<numbers.length; ++i) {
            int reversed = rev(i, lgN);
            if (i < reversed) {
                int t = numbers[i];
                numbers[i] = numbers[reversed];
                numbers[reversed] = t;
            }
        }
        System.out.println(Arrays.toString(numbers));
    }

    private static int[] readNumbers() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        return Arrays.stream(br.readLine().split(" ")).flatMapToInt(s -> IntStream.of(Integer.parseInt(s))).toArray();
    }

    private static int rev(int num, int lgN) {
        int res = 0;
        for (int i = 0; i < lgN; ++i)
            if ((num & (1 << i)) != 0)
                res |= 1 << (lgN - 1 - i);
        return res;
    }

}
