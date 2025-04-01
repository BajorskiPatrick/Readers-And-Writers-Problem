package pl.pz1.problem.visitors;

import pl.pz1.problem.Library;

import java.security.SecureRandom;
import org.jetbrains.annotations.TestOnly;
import pl.pz1.problem.visitors.identifier.Identifier;

import static pl.pz1.problem.visitors.identifier.Identifier.READER;

/**
 * Represents a Reader in the Readers and Writers problem.
 * Each reader attempts to read from the library and follows the constraints of shared access.
 */
public class Reader extends Thread {
    private static int counter = 0;

    private final Library library;
    private final Identifier readerIdentifier;
    private final SecureRandom random;

    /**
     * Tracks if reader is currently in reading state or not.
     * Used only for testing purposes to verify class behaviour.
     */
    private boolean isReading;

    /**
     * Tracks how many times reader has entered and then exited library.
     * Used only for testing purposes to verify class behaviour.
     */
    private int fullEntriesCounter;

    /**
     * Constructs a Reader with a reference to the shared Library.
     *
     * @param library The shared Library instance.
     */
    public Reader(Library library) {
        this.library = library;
        counter++;
        this.readerIdentifier = new Identifier(counter, READER);
        isReading = false;
        random = new SecureRandom();
        fullEntriesCounter = 0;
    }

    /**
     * Simulates the behavior of the reader.
     * The reader alternates between reading and resting, following synchronization rules.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                library.startReading(this.readerIdentifier);
                isReading = true;
                sleep(random.nextInt(501) + (long)1000);

                library.stopReading(this.readerIdentifier);
                isReading = false;
                fullEntriesCounter++;
                sleep(random.nextInt(501) + (long)1000);
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
            finally {
                if (isReading) {
                    library.stopReading(this.readerIdentifier);
                    isReading = false;
                }
            }
        }
        System.out.println("Czytelnik " + readerIdentifier + " zakończył działanie!");
    }

    /**
     * Getter of attribute isReading
     *
     * @return attribute isReading
     */
    @TestOnly
    public boolean isReading() {
        return isReading;
    }

    /**
     * Getter of attribute fullEntriesCounter
     *
     * @return attribute fullEntriesCounter
     */
    @TestOnly
    public int getFullEntriesCounter() {
        return fullEntriesCounter;
    }

    /**
     * Method which allows to get readers ID number.
     *
     * @return readers ID number.
     */
    @TestOnly
    public int getIdNumber() {
        return readerIdentifier.getId();
    }
}
