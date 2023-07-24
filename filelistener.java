import java.io.IOException;
import java.nio.file.*;
import java.util.List;

public class XmlFileProcessor {

    private static final Path FOLDER_PATH = Paths.get("path/to/your/folder");
    private static final Path PROCESSED_FOLDER_PATH = Paths.get("path/to/processed/folder");

    public static void main(String[] args) {
        // Process existing files in the folder before starting the WatchService
        System.out.println("Performing initial scan of the folder...");
        processExistingFiles();

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
                            processFile(filePath);
                        }
                    }
                }
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void processExistingFiles() {
        try {
            List<Path> files = Files.list(FOLDER_PATH).filter(path -> path.toString().endsWith(".xml")).toList();
            for (Path filePath : files) {
                System.out.println("Processing existing file: " + filePath);
                processFile(filePath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processFile(Path filePath) {
        // Your XML processing logic goes here

        // Move the processed file to the processed folder
        try {
            Files.move(filePath, PROCESSED_FOLDER_PATH.resolve(filePath.getFileName()), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("File moved to processed folder: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle any file moving errors here
        }
    }
}
