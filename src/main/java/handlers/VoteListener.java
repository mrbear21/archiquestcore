package handlers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import brain.BrainBungee;

public class VoteListener {

	  private BrainBungee plugin;
	  
	  public VoteListener(BrainBungee plugin) {
		  this.plugin = plugin;
	  }
	
    @SuppressWarnings("unused")
	private boolean running = false;

    public void start() {
        running = true;

        if (plugin.getConfig().get("votifier.port") == null) {
        	plugin.getConfig().set("votifier.enabled", false);
        	plugin.getConfig().set("votifier.port", 8192);
        }

        int port = plugin.getConfig().get("votifier.port") != null ? plugin.getConfig().getInt("votifier.port") : 8192;
        
        if (plugin.getConfig().get("votifier.enabled") != null && plugin.getConfig().getBoolean("votifier.enabled")) {
        
	        try {
	            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
	            server.createContext("/", new VoteHandler());
	            server.setExecutor(null);
	            server.start();
	            System.out.println("Vote listener started on port " + port);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        
        }
    }

    public void stop() {
        running = false;
    }

    private class VoteHandler implements HttpHandler {
        @SuppressWarnings("unused")
		@Override
        public void handle(HttpExchange exchange) throws IOException {
            System.out.print("1VOTE!!!! ");
            // Get the request body as an InputStream
            InputStream requestBody = exchange.getRequestBody();

            System.out.print("1VOTE!!!! > "+requestBody);
            // Read the request body into a String
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
            StringBuilder voteDataBuilder = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                voteDataBuilder.append(line);

                System.out.print("1VOTE!!!! line > "+line);
            }

            String voteData = voteDataBuilder.toString();
            
            System.out.print("1VOTE!!!! > "+voteData);
            
            // Parse the vote data
            String[] voteFields = voteData.split(" ");
            if (voteFields.length != 4) {
                // The vote data is malformed
                exchange.sendResponseHeaders(400, 0); // Bad request
                exchange.getResponseBody().close();
                return;
            }
            
            System.out.print("VOTE!!!! > "+voteData);
            
            String serviceName = voteFields[0];
            String username = voteFields[1];
            String address = voteFields[2];
            String timestamp = voteFields[3];
            

            System.out.print(serviceName+" > "+username);
            
            // Send a response
            exchange.sendResponseHeaders(200, 0);
            exchange.getResponseBody().close();
        }


    }

}

