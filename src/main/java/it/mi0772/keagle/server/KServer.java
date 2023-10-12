package it.mi0772.keagle.server;

import io.github.cdimascio.dotenv.Dotenv;
import io.undertow.Undertow;
import io.undertow.server.HttpHandler;
import io.undertow.server.HttpServerExchange;

public class KServer {
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
    }
}
