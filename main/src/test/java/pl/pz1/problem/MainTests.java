package pl.pz1.problem;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import pl.pz1.problem.visitors.Reader;
import pl.pz1.problem.visitors.Writer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;

class MainTests {
    @Test
    void askForParametersTest() {
        String input = "1\n1\n1\n";
        String[] args = new String[0];
        ByteArrayInputStream bais = new ByteArrayInputStream(input.getBytes());
        System.setIn(bais);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(baos));

        int[] parameters = Main.askForParameters(args);
        assertEquals(3, parameters.length);
        assertEquals(1, parameters[0]);
        assertEquals(1, parameters[1]);
        assertEquals(1, parameters[2]);

        String[] args2 = input.split("\n");
        int[] parameters2 = Main.askForParameters(args2);
        assertEquals(3, parameters2.length);
        assertEquals(1, parameters2[0]);
        assertEquals(1, parameters2[1]);
        assertEquals(1, parameters2[2]);

        System.setIn(System.in);
        System.setOut(System.out);
    }

    @Test
    void generateWritersTest() {
        Writer[] writers = Main.generateWriters(5, new Library(1));
        assertEquals(5, writers.length);
    }

    @Test
    void generateReadersTest() {
        Reader[] readers = Main.generateReaders(5, new Library(1));
        assertEquals(5, readers.length);
    }

    @Test
    void initializeTest() {
        Writer[] writers = new Writer[5];
        Reader[] readers = new Reader[5];

        for (int i = 0; i < 5; i++) {
            writers[i] = mock(Writer.class);
        }

        for (int i = 0; i < 5; i++) {
            readers[i] = mock(Reader.class);
        }

        Main.initialize(writers, readers);

        for (Writer writer : writers) {
            verify(writer).start();
        }

        for (Reader reader : readers) {
            verify(reader).start();
        }
    }
}
