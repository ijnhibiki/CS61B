package gh2;
import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;
import deque.ArrayDeque;

public class GuitarHero {
    public static void main(String[] args) {
        String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
        ArrayDeque<GuitarString> keyArray = new ArrayDeque<>();
        double sound;
        for (int i = 0; i < keyboard.length(); i += 1) {
            sound = 440.0 * Math.pow(2, (i - 24) / 12.0);
            GuitarString stringSound = new GuitarString(sound);
            keyArray.addLast(stringSound);
        }

        while (true) {

            /* check if the user has typed a key; if so, process it */
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);
                if (index >= 0 && index < 37) {
                    keyArray.get(index).pluck();
                }
            }


            double sample = 0.0;
            for (int i = 0; i < keyboard.length(); i++) {
                sample += keyArray.get(i).sample();
            }


            StdAudio.play(sample);
            for (int i = 0; i < keyboard.length(); i++) {
                keyArray.get(i).tic();
            }
        }

    }
}
