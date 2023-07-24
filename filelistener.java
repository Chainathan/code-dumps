import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class XmlFileProcessor {

    private static final Path FOLDER_PATH = Paths.get("path/to/your/folder");

    public static void main(String[] args) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            FOLDER_PATH.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
            executorService.scheduleAtFixedRate(XmlFileProcessor::processFiles, 0, 1, TimeUnit.MINUTES);

            System.out.println("XmlFileProcessor is running. Press Ctrl+C to stop.");

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path filePath = FOLDER_PATH.resolve((Path) event.context());
                        System.out.println("New file detected: " + filePath);
                        // You can process the file immediately here if you want,
                        // but to follow the creation time logic, we'll process it
                        // in the scheduled method.
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void processFiles() {
        try {
            Files.walkFileTree(FOLDER_PATH, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (file.toString().endsWith(".xml")) {
                        long creationTime = attrs.creationTime().toMillis();
                        long currentTime = System.currentTimeMillis();
                        long timeElapsed = currentTime - creationTime;
                        long timeThreshold = TimeUnit.MINUTES.toMillis(1); // 1 minute threshold

                        if (timeElapsed >= timeThreshold) {
                            // Process the file here according to the creation time logic
                            System.out.println("Processing file: " + file);
                            // Your XML processing logic goes here
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
