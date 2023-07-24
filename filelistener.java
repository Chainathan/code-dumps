import java.io.IOException;
import java.nio.file.*;

public class XmlFileProcessor {

    private static final Path FOLDER_PATH = Paths.get("path/to/your/folder");

    public static void main(String[] args) {
        try {
            WatchService watchService = FileSystems.getDefault().newWatchService();
            FOLDER_PATH.register(watchService, StandardWatchEventKinds.ENTRY_CREATE);

            System.out.println("XmlFileProcessor is running. Press Ctrl+C to stop.");

            while (true) {
                WatchKey key = watchService.take();
                for (WatchEvent<?> event : key.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_CREATE) {
                        Path filePath = FOLDER_PATH.resolve((Path) event.context());
                        System.out.println("New file detected: " + filePath);
                        if (filePath.toString().endsWith(".xml")) {
                            // Process the file here immediately
                            System.out.println("Processing file: " + filePath);
                            // Your XML processing logic goes here
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
