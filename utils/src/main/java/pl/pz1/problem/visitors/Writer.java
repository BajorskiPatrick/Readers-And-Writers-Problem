package pl.pz1.problem.visitors;

import pl.pz1.problem.Library;

import java.security.SecureRandom;
import org.jetbrains.annotations.TestOnly;
import pl.pz1.problem.visitors.identifier.Identifier;

import static pl.pz1.problem.visitors.identifier.Identifier.WRITER;

/**
 * Represents a Writer in the Readers and Writers problem.
 * Each writer attempts to write to the library and follows the constraints of exclusive access.
 */
public class Writer extends Thread {
    private static int counter = 0;

    private final Library library;
    private final Identifier writerIdentifier;
    private final SecureRandom random;

    /**
     * Tracks if writer is currently in writing state or not.
     * Used only for testing purposes to verify class behaviour.
     */
    private boolean isWriting;

    /**
     * Tracks how many times writer has entered and then exited library.
     * Used only for testing purposes to verify class behaviour.
     */
    private int fullEntriesCounter;

    /**
     * Constructs a Writer with a reference to the shared Library.
     *
     * @param library The shared Library instance.
     */
    public Writer(Library library) {
        this.library = library;
        counter++;
        this.writerIdentifier = new Identifier(counter, WRITER);
        isWriting = false;
        random = new SecureRandom();
        fullEntriesCounter = 0;
    }

    /**
     * Simulates the behavior of the writer.
     * The writer alternates between writing and resting, following synchronization rules.
     */
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                library.startWriting(this.writerIdentifier);
                isWriting = true;
                sleep(random.nextInt(501) + (long)2000);

                library.stopWriting(this.writerIdentifier);
                isWriting = false;
                fullEntriesCounter++;
                sleep(random.nextInt(501) + (long)2000);
            }
            catch (InterruptedException e) {
                System.out.println(e.getMessage());
                Thread.currentThread().interrupt();
            }
            finally {
                if (isWriting) {
                    library.stopWriting(this.writerIdentifier);
                    isWriting = false;
                }
            }
        }
        System.out.println("Pisarz " + writerIdentifier + " zakończył działanie!");
    }

    /**
     * Getter of attribute isWriting
     *
     * @return attribute isWriting
     */
    @TestOnly
    public boolean isWriting() {
        return isWriting;
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
     * Method which allows to get writers ID number.
     *
     * @return writers ID number.
     */
    @TestOnly
    public int getIdNumber() {
        return writerIdentifier.getId();
    }
}
