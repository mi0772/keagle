package it.mi0772.keagle.server;

import io.github.cdimascio.dotenv.Dotenv;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;
import it.mi0772.keagle.command.PutCommand;
import it.mi0772.keagle.filesystem.ExpireRecordCleaner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KServer {
    private static final Logger logger = LoggerFactory.getLogger(KServer.class);
    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        var serverPort = Integer.parseInt(dotenv.get("SERVER_PORT"));
        var serverAddress = dotenv.get("SERVER_ADDRESS");
        Undertow server = Undertow.builder()
                .addHttpListener(serverPort, serverAddress)
                .setHandler(new HttpHandler() {
                    @Override
                    public void handleRequest(HttpServerExchange exchange) throws Exception {
                        exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "text/plain");
                        exchange.getResponseSender().send("Ciao, questo è un server Undertow!");
                    }
                })
                .build();

        server.start();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            logger.info("start clean daemon");
            new ExpireRecordCleaner().clean();
        }, 5, 60 , TimeUnit.SECONDS);
    }

}
