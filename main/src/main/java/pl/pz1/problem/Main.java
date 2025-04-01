package pl.pz1.problem;

import pl.pz1.problem.visitors.Reader;
import pl.pz1.problem.visitors.Writer;

import java.util.Scanner;

/**
 * Main class to simulate the Readers and Writers problem using a Library system.
 * Users specify the library capacity, the number of readers, and the number of writers.
 * Each reader and writer runs in its own thread, accessing the shared Library.
 */
public class Main {
    /**
     * Main entry point of the program.
     * Initializes the library, creates threads for readers and writers, and starts the simulation.
     *
     * @param args Command-line arguments passed by user
     */
    public static void main(String[] args) {
        int[] parameters = askForParameters(args);

        Library library = new Library(parameters[0]);
        Writer[] writers = generateWriters(parameters[1], library);
        Reader[] readers = generateReaders(parameters[2], library);

        initialize(writers, readers);
    }

    /**
     * Generates an array of Writer threads associated with the given library.
     *
     * @param writersNumber the number of writers to generate.
     * @param library the shared Library instance to be accessed by the writers.
     * @return an array of Writer objects.
     */
    public static Writer[] generateWriters(int writersNumber, Library library) {
        Writer[] writers = new Writer[writersNumber];
        for (int i = 0; i < writersNumber; i++) {
            writers[i] = new Writer(library);
        }
        return writers;
    }

    /**
     * Generates an array of Reader threads associated with the given library.
     *
     * @param readersNumber the number of readers to generate.
     * @param library the shared Library instance to be accessed by the readers.
     * @return an array of Reader objects.
     */
    public static Reader[] generateReaders(int readersNumber, Library library) {
        Reader[] readers = new Reader[readersNumber];
        for (int i = 0; i < readersNumber; i++) {
            readers[i] = new Reader(library);
        }
        return readers;
    }

    /**
     * Initializes the simulation by starting all Writer and Reader threads.
     *
     * @param writers an array of Writer objects to be started.
     * @param readers an array of Reader objects to be started.
     */
    public static void initialize(Writer[] writers, Reader[] readers) {
        for (Writer writer : writers) {
            writer.start();
        }

        for (Reader reader : readers) {
            reader.start();
        }
    }

    /**
     * Prompts the user for parameters to configure the library simulation.
     * Parameters include the library capacity, number of readers, and number of writers.
     *
     * @param args an array of String with arguments passed by user from command line
     * @return an array of three integers: [libraryCapacity, readersNumber, writersNumber].
     */
    public static int[] askForParameters(String[] args) {
        int[] parameters = new int[3];

        if (args.length == 3) {
            parameters[0] = Integer.parseInt(args[0]);
            parameters[1] = Integer.parseInt(args[1]);
            parameters[2] = Integer.parseInt(args[2]);
            return parameters;
        }

        if (args.length == 1 || args.length == 2 || args.length > 3) {
            System.out.println("Podano niepoprawną liczbę parametrów, wymagane są 3!");
        }

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Podaj pojemność biblioteki (max liczbę czytelników na raz):");
            parameters[0] = scanner.nextInt();

            System.out.println("Podaj liczbę pisarzy, których chcesz utworzyć:");
            parameters[1] = scanner.nextInt();

            System.out.println("Podaj liczbę czytelników, których chcesz utworzyć:");
            parameters[2] = scanner.nextInt();

            if (parameters[0] > 0 && parameters[1] > 0 && parameters[2] > 0) {
                System.out.println("Podano niepoprawne parametry!");
                break;
            }
        }

        return parameters;
    }
}