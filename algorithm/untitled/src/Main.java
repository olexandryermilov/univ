import java.io.*;
import java.math.*;
import java.security.*;
import java.text.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.*;
import java.util.regex.*;
import java.util.stream.*;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;


class Result {

    /*
     * Complete the 'compressedString' function below.
     *
     * The function is expected to return a STRING.
     * The function accepts STRING message as parameter.
     */

    public static long minHealth(List<Integer> dungeon) {
        // Write your code here
        long initialHealth = 0;
        long health = 0;
        for (Integer delta : dungeon) {
            health += delta;
            if (health < 1) {
                initialHealth += -health + 1;
                health = 1;
            }
        }
        return initialHealth;
    }

}
