import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service
public class ApiService {
    private final WebClient webClient;

    @Autowired
    public ApiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.example.com").build();
    }

    public void postJsonPayload() {
        try {
            // Read the JSON data from the file
            String payloadJson = new String(Files.readAllBytes(Paths.get("path/to/payload.json")));

            // Send POST request with JSON payload
            ResponseEntity<String> responseEntity = webClient.post()
                    .uri("/endpoint")
                    .body(BodyInserters.fromValue(payloadJson))
                    .retrieve()
                    .toEntity(String.class)
                    .block();

            if (responseEntity != null && responseEntity.getStatusCode() == HttpStatus.OK) {
                String responseJson = responseEntity.getBody();

                // Save the response JSON to a file
                Files.write(Paths.get("path/to/response.json"), responseJson.getBytes());
            } else {
                // Handle the error response if needed
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
