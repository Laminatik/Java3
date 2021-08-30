package lesson4.homework;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ABC {

    public static final int ITERATIONS_NUMBER = 3;

    private enum Letter {
        A('A'),
        B('B'),
        C('C');

        private char letter;

        Letter(char letter) {
            this.letter = letter;
        }

        public char getLetter() {
            return letter;
        }

        public Letter getNextLetter() {
            int ordinal = this.ordinal() + 1;
            int nextLetterOrdinal = ordinal % Letter.values().length;
            for (Letter value : values()) {
                if (value.ordinal() == nextLetterOrdinal) {
                    return value;
                }
            }

            throw new RuntimeException("Failed to found next letter for " + this);
        }
    }

    private volatile Letter currentLetter = Letter.A;

    public synchronized void printLetter(Letter letter) {
        for (int i = 0; i < ITERATIONS_NUMBER; i++) {
            try {
                while (letter != currentLetter) {
                    wait();
                }
                System.out.print(letter.getLetter());
                currentLetter = currentLetter.getNextLetter();
                notifyAll();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        ABC abcPrinter = new ABC();
        ExecutorService executorService = Executors.newFixedThreadPool(Letter.values().length);
        for (Letter letter : Letter.values()) {
            executorService.execute(() -> abcPrinter.printLetter(letter));
        }
        executorService.shutdown();
    }
}
