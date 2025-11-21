package privat.test.billing.executor;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class WriteOffRunner {

    static void main(String[] args) throws IOException, InterruptedException {
        var paymentServiceApiUrl = System.getenv("PAYMENT_SERVICE_URL");
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        try (var httpClient = HttpClient.newBuilder().build()) {
            HttpRequest getPaymentsRequest = HttpRequest.newBuilder()
                    .uri(URI.create(paymentServiceApiUrl + "/regular-payment"))
                    .GET()
                    .build();

            HttpResponse<byte[]> paymentsResponse = httpClient.send(
                    getPaymentsRequest,
                    HttpResponse.BodyHandlers.ofByteArray());

            var regularPayments = objectMapper.readValue(
                    paymentsResponse.body(),
                    new TypeReference<List<RegularPaymentDto>>() {});

            regularPayments.stream().filter(regularPaymentDto -> {
                System.out.println("Start checking if regular payment with id: " + regularPaymentDto.id() + " pending");
                HttpRequest checkPendingRequest = HttpRequest.newBuilder()
                        .uri(URI.create(paymentServiceApiUrl + "/regular-payment/" + regularPaymentDto.id() + "/pending"))
                        .GET()
                        .build();

                HttpResponse<String> pendingResponse;
                try {
                    pendingResponse = httpClient.send(
                            checkPendingRequest,
                            HttpResponse.BodyHandlers.ofString());
                } catch (IOException | InterruptedException e) {
                    System.out.println("Failed to send pending request to payment service for regular payment with id: "
                            + regularPaymentDto.id() + ". Exception occurred with message: " + e.getMessage());
                    return false;
                }

                return Boolean.parseBoolean(pendingResponse.body());
            }).forEach(regularPaymentDto -> {
                HttpRequest postTransactionRequest = HttpRequest.newBuilder()
                        .uri(URI.create(paymentServiceApiUrl + "/payment-transaction?regularPaymentId="
                                + regularPaymentDto.id()))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                try {
                    httpClient.send(
                            postTransactionRequest,
                            HttpResponse.BodyHandlers.discarding()
                    );
                } catch (IOException | InterruptedException e) {
                    System.out.println("Failed to send payment transaction creation request to payment service for regular payment with id: "
                            + regularPaymentDto.id() + ". Exception occurred with message: " + e.getMessage());
                }
            });
        }
    }
}