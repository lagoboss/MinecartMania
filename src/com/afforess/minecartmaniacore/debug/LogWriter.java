package com.afforess.minecartmaniacore.debug;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.Iterator;
import java.util.LinkedList;

import com.afforess.minecartmaniacore.MinecartManiaCore;

public class LogWriter extends Thread {
    private final LinkedList<String> queued;
    
    public LogWriter(final LinkedList<String> queued) {
        this.queued = queued;
    }
    
    @Override
    public void run() {
        try {
            final File logger = new File(MinecartManiaCore.getDataDirectoryRelativePath() + File.separator + "MinecartMania.log");
            if (logger.exists() && (logger.length() > 3100000L)) {
                logger.delete(); //clear log if > 3MB
            }
            final BufferedWriter output = new BufferedWriter(new FileWriter(MinecartManiaCore.getDataDirectoryRelativePath() + File.separator + "MinecartMania.log", true));
            final Iterator<String> i = queued.iterator();
            while (i.hasNext()) {
                final String log = i.next();
                output.write(log + '\n');
            }
            output.close();
        } catch (final Exception e) {
            MinecartManiaLogger.getInstance().info("Failed to update log!");
            return;
        }
        
    }
    
}
