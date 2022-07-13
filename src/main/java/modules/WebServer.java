package modules;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.InetSocketAddress;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

import com.BrainBungee;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;


public class WebServer implements HttpHandler {
	
	public interface WebsiteRequestHandler {
	    String handle(HttpExchange exchange, String request) throws IOException;
	}

	
    public void handle(HttpExchange exchange) throws IOException {
    	
    	String request = exchange.getRequestURI().toASCIIString().substring(1);
    	
    	this.plugin.log(request);

    	getPost(exchange);
    	
    	
        File page = getWebsitePage(request);
        List<String> lines = readFile(page);
        String response = "";
        for (String line : lines) {
            response += line;
        }
    	
    	
        
        response = "<html><head><meta charset=\"utf-8\"></head><body>[#]</body></html>";

    	//response = getRequestHandler().handle(exchange, request);
    	
        try {
            HTMLEditorKit htmlEditKit = new HTMLEditorKit();
            HTMLDocument htmlDocument = (HTMLDocument) htmlEditKit.createDefaultDocument();
            HTMLEditorKit.Parser parser = new ParserDelegator();
            parser.parse(new StringReader(response), htmlDocument.getReader(0), true);
        } catch(IOException e){
            e.printStackTrace();
        }
		exchange.sendResponseHeaders(200, response.getBytes().length);
		OutputStreamWriter writer = new OutputStreamWriter(exchange.getResponseBody());
		writer.write(response);
        writer.close();
    }
	
    private WebsiteRequestHandler requestHandler;
    
	  private BrainBungee plugin;
	  
	  public WebServer(BrainBungee plugin) {
		  this.plugin = plugin;
	  }
    
    public WebsiteRequestHandler getRequestHandler() {
        return this.requestHandler;
    }
    
    public void setRequestHandler(WebsiteRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }
    
    public void start() {
    	try {
	    	int port = plugin.getConfig().getInt("port");
	        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
	        server.createContext("/", this);
	        server.setExecutor(null);
	        server.start();
	        plugin.getLogger().info("Web server running on port: "+port);
    	} catch (Exception c) {
    		c.printStackTrace();
    	}
    }


	private Map<String, String> getPost(HttpExchange exchange) { 
	    String encoding = "UTF-8";
	    String qry = null;
	    InputStream in = exchange.getRequestBody();
	    try {
	        ByteArrayOutputStream out = new ByteArrayOutputStream();
	        byte buf[] = new byte[4096];
	        for (int n = in.read(buf); n > 0; n = in.read(buf)) {
	            out.write(buf, 0, n);
	        }
	        qry = new String(out.toByteArray(), encoding);
	    } catch (Exception c) {
	    	c.printStackTrace();
	    } finally {
	        try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
	    }
	    Map<String, String> parms = new HashMap<String, String>();
	    String defs[] = qry.split("[&]");
	    try {
		    for (String def: defs) {
		        int ix = def.indexOf('=');
		        String name;
		        String value;
		        if (ix < 0) {
		            name = URLDecoder.decode(def, encoding);
		            value = "";
		        } else {
		            name = URLDecoder.decode(def.substring(0, ix), encoding);
		            value = URLDecoder.decode(def.substring(ix+1), encoding);
		        }
		        parms.put(name, value);
		    }
	    } catch (Exception c) {
	    	c.printStackTrace();
	    }
	    return parms;
 	}
    
	
    private static final File WEBSITE_FOLDER = new File("website");


    public static File createFileIfNecessary(File file) {
        if (!file.exists()) {
            try {
                if (!file.createNewFile()) {
                    System.err.println("Could not create file " + file.getName() + "!");
                    return null;
                }
                return file;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else
            return file;
    }
    public static File createFolderIfNecessary(File folder) {
        if (!folder.exists() && !folder.mkdirs()) {
            System.err.println("Could not create folder " + folder.getName() + "!");
            return null;
        }
        return folder;
    }

    public static List<String> readFile(File file) throws IOException {
        return Files.readAllLines(file.toPath());
    }

    public static File getWebsiteFolder() {
        return createFolderIfNecessary(WEBSITE_FOLDER);
    }

    public static File getWebsiteHome() {
        return createFileIfNecessary(new File(getWebsiteFolder(), "index.html"));
    }
    
    public static File getWebsite404() {
        return createFileIfNecessary(new File(getWebsiteFolder(), "404.html"));
    }

    public static File getWebsitePage(String request) {
        if (request.endsWith("/")) // ends with folder directory
            request += "index.html";
        File file = new File(getWebsiteFolder(), request);
        if (file.exists() && file.isDirectory())
            return getWebsitePage(request + "/index.html");
        if (!file.exists())
            return getWebsite404();
        return file;
    }
 	
}