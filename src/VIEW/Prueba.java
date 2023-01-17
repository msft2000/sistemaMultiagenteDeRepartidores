package VIEW;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class Prueba {

    // This synchronized class will be used to write to the console window
    private class SynchronizedByteArrayOutputStreamWrapper
            extends OutputStream {
        // The console will be synchronized through a monitor.
        // WARNING! This could delay the code trying to write to the console!
        private final Object monitor = new Object();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        @Override
        public void write(int data) throws IOException {
            synchronized (monitor) {
                byteArrayOutputStream.write(data);
            }
        }

        public byte[] readEmpty() {
            byte[] bufferContent;  
            synchronized(monitor) {
                bufferContent = byteArrayOutputStream.toByteArray();
                byteArrayOutputStream.reset();
            }
            return bufferContent;
        }
    }

    public void guiConsoleTest() {
        System.out.println("Normal java console output");

        // Remember old output stream (optional)
        PrintStream stdout = System.out;
        stdout.println("Starting gui for console output"); // Still works
        // Stream for output to gui
        SynchronizedByteArrayOutputStreamWrapper rawout = new SynchronizedByteArrayOutputStreamWrapper();
        // Set new stream for System.out
        System.setOut(new PrintStream(rawout, true));

        // Demo gui
        JTextArea textArea = new JTextArea();
        JFrame window = new JFrame("Console test");
        window.add(new JScrollPane(textArea));
        window.setSize(500, 500);
        window.setVisible(true);

        // Console thread
        Thread consoleThread = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                String pendingConsoleOutput = new String(rawout.readEmpty());
                textArea.append(pendingConsoleOutput);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        });
        consoleThread.start();

        // Test it (for 10 sec)
        for (int i = 1; i <= 100; i++) {
            System.out.println("Printing to gui console: " + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }

        // Wait 1 sec
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
        }

        // Clean up and exit
        consoleThread.interrupt();
        try {
            consoleThread.join();
        } catch (InterruptedException e) {
        }
        window.dispose();
    }

    public static void main(String[] args) {
        new Prueba().guiConsoleTest();
    }
}