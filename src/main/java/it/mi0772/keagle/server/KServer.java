package it.mi0772.keagle.server;

import io.undertow.Undertow;
import it.mi0772.keagle.config.KConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KServer {
    private static final Logger logger = LoggerFactory.getLogger(KServer.class);
    public static void main(String[] args) {
        var serverPort = Integer.parseInt(KConfig.getInstance().getHttpServerPort());
        var serverAddress = KConfig.getInstance().getHttpServerAddress();
        Undertow server = Undertow.builder()
                .addHttpListener(serverPort, serverAddress)
                .setHandler(exchange -> {
                    exchange.getResponseHeaders().put(io.undertow.util.Headers.CONTENT_TYPE, "text/plain");
                    exchange.getResponseSender().send("Ciao, questo è un server Undertow!");
                })
                .build();

        server.start();

        ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(() -> {
            logger.info("start clean daemon");

        }, 5, 60 , TimeUnit.SECONDS);
    }

}
