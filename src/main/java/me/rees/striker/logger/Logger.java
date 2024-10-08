package me.rees.striker.logger;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.locks.ReentrantLock;

public class Logger {
    private final ReentrantLock logMutex = new ReentrantLock();
    private final String name;
    private final String directory;
    private String subdirectory;

    private FileWriter simulationFile;
    private FileWriter debugFile;

    public Logger(String name, boolean debugFlag) {
        this.name = name;
        this.directory = System.getenv("STRIKER_SIMULATIONS");
        getSubdirectory();

        // Ensure the directory and subdirectory exist
        File dir = new File(directory + "/" + subdirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        // Open simulation file
        try {
            simulationFile = new FileWriter(directory + "/" + subdirectory + "/" + name + "_simulation.txt", true);

            // Open debug file if debugFlag is true
            if (debugFlag) {
                debugFile = new FileWriter(directory + "/" + subdirectory + "/" + name + "_debug.txt", true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Method to close the files when done
    public void close() {
        try {
            if (simulationFile != null) {
                simulationFile.close();
            }
            if (debugFile != null) {
                debugFile.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Log a message to the simulation file
    public void simulation(String message) {
        logMutex.lock();
        try {
            if (simulationFile != null) {
                simulationFile.write(message);
                simulationFile.flush(); // Ensure the message is written immediately
                System.out.print(message); // Print to console too
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logMutex.unlock();
        }
    }

    // Log a message to the debug file
    public void debug(String message) {
        logMutex.lock();
        try {
            if (debugFile != null) {
                debugFile.write(message);
                debugFile.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logMutex.unlock();
        }
    }

    // Log a message to an insert file
    public void insert(String message) {
        logMutex.lock();
        try (FileWriter insertFile = new FileWriter(directory + "/" + subdirectory + "/" + name + "_insert.txt", true)) {
            insertFile.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logMutex.unlock();
        }
    }

    // Create subdirectory based on the current date
    private void getSubdirectory() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd");
        subdirectory = sdf.format(new Date());
    }
}

