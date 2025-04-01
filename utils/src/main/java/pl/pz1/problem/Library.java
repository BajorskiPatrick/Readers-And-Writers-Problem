package pl.pz1.problem;

import pl.pz1.problem.visitors.identifier.Identifier;

import java.util.*;
import org.jetbrains.annotations.TestOnly;
import org.jetbrains.annotations.VisibleForTesting;


/**
 * Represents the shared Library in the Readers and Writers problem.
 * Controls access for readers and writers using semaphores to ensure proper synchronization.
 */
public class Library {
    private final Queue<Identifier> queue = new LinkedList<>();
    private final List<Identifier> thoseInside = new ArrayList<>();
    private int occupiedPlacesAmount = 0;
    private final int capacity;

    /**
     * Constructs a Library with the specified capacity.
     *
     * @param capacity The maximum number of readers allowed at the same time.
     */
    public Library(int capacity) {
        this.capacity = capacity;
    }

    /**
     * Allows a writer to start writing in the library.
     * Writers have exclusive access, so no other writers or readers are allowed simultaneously.
     *
     * @param identifier The unique ID of the writer.
     * @throws InterruptedException If the thread is interrupted while waiting for access.
     */
    public synchronized void startWriting(Identifier identifier) throws InterruptedException {
        try {
            queue.add(identifier);
            System.out.println(identifier.getName() + " " + identifier.getId() + " stanął w kolejce i czeka na wejście. "
                    + printInfo());

            while (queue.peek() != identifier || occupiedPlacesAmount > 0) {
                wait();
            }

            occupiedPlacesAmount = capacity;
            thoseInside.add(identifier);
            queue.poll();
            System.out.println(identifier.getName() + " " + identifier.getId() + " wszedł i pisze... "
                    + printInfo());
        }
        catch (InterruptedException e) {
            queue.remove(identifier);
            System.out.println(e.getMessage());
            throw new InterruptedException();
        }
    }

    /**
     * Allows a writer to stop writing and releases the library for others.
     *
     * @param identifier The unique ID of the writer.
     */
    public synchronized void stopWriting(Identifier identifier) {
        if (!thoseInside.contains(identifier)) {
            return;
        }
        occupiedPlacesAmount = 0;
        thoseInside.remove(identifier);
        System.out.println(identifier.getName() + " " + identifier.getId() + " opuścił bibliotekę. "
                + printInfo());
        notifyAll();
    }

    /**
     * Allows a reader to start reading in the library.
     * Multiple readers can read simultaneously, up to the specified capacity.
     *
     * @param identifier The unique ID of the reader.
     * @throws InterruptedException If the thread is interrupted while waiting for access.
     */
    public synchronized void startReading(Identifier identifier) throws InterruptedException {
        try {
            queue.add(identifier);
            System.out.println(identifier.getName() + " " + identifier.getId() + " stanął w kolejce i czeka na wejście. "
                                + printInfo());

            while (queue.peek() != identifier || occupiedPlacesAmount == capacity) {
                wait();
            }

            occupiedPlacesAmount++;
            thoseInside.add(identifier);
            queue.poll();
            System.out.println(identifier.getName() + " " + identifier.getId() + " wszedł i czyta... "
                    + printInfo());
            notifyAll();
        }
        catch (InterruptedException e) {
            queue.remove(identifier);
            System.out.println(e.getMessage());
            throw new InterruptedException();
        }
    }

    /**
     * Allows a reader to stop reading and releases the library for others.
     *
     * @param identifier The unique ID of the reader.
     */
    public synchronized void stopReading(Identifier identifier) {
        if (!thoseInside.contains(identifier)) {
            return;
        }
        occupiedPlacesAmount--;
        thoseInside.remove(identifier);
        System.out.println(identifier.getName() + " " + identifier.getId() + " opuścił bibliotekę. "
                + printInfo());
        notifyAll();
    }

    /**
     * Method which construct String representing the Library information.
     * This String consists of information who is in the queue and who is inside the Library
     *
     * @return constructed String
     */
    @VisibleForTesting
    synchronized String printInfo() {
        return "W kolejce: " + print(queue) + ". W bibliotece: " + print(thoseInside) + "\n";
    }

    /**
     * Method which converts Collection of Identifiers to String and return this String.
     * Constructed String is in format like: X-Y, where X is short name of Writer or Reader, and Y is ID number
     *
     * @param ids Collection to convert
     * @return constructed String
     */
    private synchronized String print(Collection<Identifier> ids) {
        StringBuilder sb = new StringBuilder();
        if(ids.isEmpty()) {
            return "";
        }

        for (Identifier id : ids) {
            sb.append(id.getShortName()).append("-").append(id.getId()).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length());
        return sb.toString();
    }

    /**
     * Getter of attribute occupiedPlacesAmount.
     *
     * @return attribute occupiedPlacesAmount.
     */
    @TestOnly
    public synchronized int getOccupiedPlacesAmount() {
        return occupiedPlacesAmount;
    }

    /**
     * Allows to check size of the queue.
     *
     * @return size of queue
     */
    @TestOnly
    public synchronized int getQueueSize() {
        return queue.size();
    }

    /**
     * Allows to check if given Identifier is on the list of thoseInside.
     *
     * @param identifier Identifier of person whose existence in Library we want to check.
     * @return boolean answer - True if person is in Library.
     */
    @TestOnly
    public synchronized boolean isInside(Identifier identifier) {
        return thoseInside.contains(identifier);
    }
}