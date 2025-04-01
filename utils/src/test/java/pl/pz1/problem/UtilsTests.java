package pl.pz1.problem;

import static org.awaitility.Awaitility.await;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.pz1.problem.visitors.identifier.Identifier;
import pl.pz1.problem.visitors.Reader;
import pl.pz1.problem.visitors.Writer;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;

class UtilsTests {
    private Library library;
    private ByteArrayOutputStream bos;

    @BeforeEach
    void setUp() {
        bos = new ByteArrayOutputStream();
        System.setOut(new PrintStream(bos));
        library = new Library(5);
    }

    @AfterEach
    void tearDown() {
        System.setOut(System.out);
    }


    @Test
    void testStartThenStopReading() throws InterruptedException {
        Identifier identifier = new Identifier(1, Identifier.READER);
        library.startReading(identifier);
        assertEquals(1, library.getOccupiedPlacesAmount());
        assertEquals(0, library.getQueueSize());
        assertTrue(bos.toString().contains("wszedł i czyta"));
        library.stopReading(identifier);
        assertEquals(0, library.getOccupiedPlacesAmount());
        assertTrue(bos.toString().contains("opuścił bibliotekę"));
    }

    @Test
    void testStartThenStopWriting() throws InterruptedException {
        Identifier identifier = new Identifier(1, Identifier.READER);
        library.startWriting(identifier);
        assertEquals(5, library.getOccupiedPlacesAmount());
        assertEquals(0, library.getQueueSize());
        assertTrue(bos.toString().contains("wszedł i pisze"));
        library.stopWriting(identifier);
        assertEquals(0, library.getOccupiedPlacesAmount());
        assertTrue(bos.toString().contains("opuścił bibliotekę"));
    }

    @Test
    void testWriterRequestsWhileReaderInside() throws InterruptedException {
        Identifier identifier1 = new Identifier(1, Identifier.READER);
        Identifier identifier2 = new Identifier(1, Identifier.WRITER);
        Thread testThread = new Thread(() -> {
            try {

                library.startReading(identifier1);
                library.startWriting(identifier2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        testThread.start();

        await().pollDelay(500, TimeUnit.MILLISECONDS).until(() -> true);

        assertEquals(Thread.State.WAITING, testThread.getState());

        testThread.interrupt();
        testThread.join();


        assertTrue(bos.toString().contains("Pisarz 1 stanął w kolejce i czeka na wejście."));
        assertFalse(bos.toString().contains("Pisarz 1 wszedł i pisze..."));
        assertEquals(0, library.getQueueSize());
        assertEquals(1, library.getOccupiedPlacesAmount());
        assertTrue(library.isInside(identifier1));
    }

    @Test
    void testReaderRequestWhileWriterInside() throws InterruptedException {
        Identifier identifier1 = new Identifier(1, Identifier.WRITER);
        Identifier identifier2 = new Identifier(1, Identifier.READER);
        Thread testThread = new Thread(() -> {
            try {
                library.startWriting(identifier1);
                library.startReading(identifier2);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        testThread.start();

        await().pollDelay(500, TimeUnit.MILLISECONDS).until(() -> true);

        assertEquals(Thread.State.WAITING, testThread.getState());

        testThread.interrupt();
        testThread.join();


        assertTrue(bos.toString().contains("Czytelnik 1 stanął w kolejce i czeka na wejście"));
        assertFalse(bos.toString().contains("Czytelnik 1 wszedł i czyta..."));
        assertEquals(0, library.getQueueSize());
        assertEquals(5, library.getOccupiedPlacesAmount());
        assertTrue(library.isInside(identifier1));
    }


    @Test
    void writerInterruptingWhileWritingTest() throws InterruptedException {
        Writer writer = new Writer(library);
        writer.start();
        await().until(writer::isWriting);

        writer.interrupt();
        writer.join();

        assertFalse(writer.isWriting());
        assertEquals(0, library.getOccupiedPlacesAmount());
        assertEquals(0, library.getQueueSize());
    }


    @Test
    void writerInterruptingAfterOneFullEntry() throws InterruptedException {
        Writer writer = new Writer(library);
        writer.start();
        await().until(writer::getFullEntriesCounter, equalTo(1));

        assertEquals(Thread.State.TIMED_WAITING, writer.getState());

        writer.interrupt();
        writer.join();

        assertFalse(writer.isWriting());
        assertEquals(0, library.getOccupiedPlacesAmount());
        assertEquals(0, library.getQueueSize());
    }


    @Test
    void readerInterruptingWhileReadingTest() throws InterruptedException {
        Reader reader = new Reader(library);
        reader.start();
        await().until(reader::isReading);

        reader.interrupt();
        reader.join();

        assertFalse(reader.isReading());
        assertEquals(0, library.getOccupiedPlacesAmount());
        assertEquals(0, library.getQueueSize());
    }

    @Test
    void readerInterruptingAfterOneFullEntry() throws InterruptedException {
        Reader reader = new Reader(library);
        reader.start();
        await().until(reader::getFullEntriesCounter, equalTo(1));

        assertEquals(Thread.State.TIMED_WAITING, reader.getState());

        reader.interrupt();
        reader.join();

        assertFalse(reader.isReading());
        assertEquals(0, library.getOccupiedPlacesAmount());
        assertEquals(0, library.getQueueSize());
    }

    @Test
    void readerStopsReadingWhileNotInsideLibrary() throws InterruptedException {
        Identifier identifier1 = new Identifier(1, Identifier.READER);
        Identifier identifier2 = new Identifier(2, Identifier.READER);
        Identifier identifier3 = new Identifier(3, Identifier.READER);
        library.startReading(identifier1);
        library.startReading(identifier2);
        library.stopReading(identifier3);
        assertEquals(2, library.getOccupiedPlacesAmount());
        assertTrue(library.isInside(identifier1));
        assertTrue(library.isInside(identifier2));
    }

    @Test
    void writerStopsWritingWhileNotInsideLibrary() throws InterruptedException {
        Identifier identifier1 = new Identifier(1, Identifier.WRITER);
        Identifier identifier2 = new Identifier(2, Identifier.WRITER);
        library.startWriting(identifier1);
        library.stopWriting(identifier2);
        assertEquals(5, library.getOccupiedPlacesAmount());
        assertTrue(library.isInside(identifier1));
    }

    @Test
    void printingLibrariesInfoTest() throws InterruptedException {
        Writer writer = new Writer(library);
        writer.start();

        Reader reader = new Reader(library);
        reader.start();

        await().until(() -> reader.getState().equals(Thread.State.WAITING));

        assertEquals("W kolejce: R-" + reader.getIdNumber() + ". W bibliotece: W-"
                + writer.getIdNumber() + "\n", library.printInfo());

        reader.interrupt();
        writer.interrupt();
        reader.join();
        writer.join();
    }
}
