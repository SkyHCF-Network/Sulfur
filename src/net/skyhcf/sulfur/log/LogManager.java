package net.skyhcf.sulfur.log;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import lombok.Getter;

public class LogManager {

    @Getter
    private  Queue<Log> logQueue = new ConcurrentLinkedQueue<>();
    
    public void exportAllLogs() {
        new LogExportRunnable(null).run();
    }

}