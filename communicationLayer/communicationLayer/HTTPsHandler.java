package communicationLayer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URI;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class HTTPsHandler implements HttpHandler {

	protected String readRequestBody(InputStream is) throws IOException {
		String newLine = System.getProperty("line.separator");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder result = new StringBuilder();
		String line;
		boolean flag = false;
		while ((line = reader.readLine()) != null) {
			result.append(flag ? newLine : "").append(line);
			flag = true;
		}
		return result.toString();
	}

	protected void printRequest(HttpExchange t) throws IOException {
		URI reqUri = t.getRequestURI();
		String reqMethod = t.getRequestMethod();
		// Headers reqHeaders = t.getRequestHeaders();
		String reqBody = readRequestBody(t.getRequestBody());

		System.out.println("Request:");
		System.out.println("Request URL: " + reqUri.toString());
		System.out.println("Request Method: " + reqMethod);
		// print headers ..
		System.out.println("Request Body:");
		System.out.println(reqBody);
		System.out.println("****************************\n");
	}

	protected void renderFile(HttpExchange t, String mime, String fileId) throws IOException {
		String path = System.getProperty("user.dir") + File.separator + "webClient";
		File file = new File(path + File.separator + fileId).getCanonicalFile();

		Headers h = t.getResponseHeaders();

		h.set("Content-Type", mime);

		t.sendResponseHeaders(200, 0);

		OutputStream os = t.getResponseBody();
		FileInputStream fs = new FileInputStream(file);
		final byte[] buffer = new byte[0x10000];
		int count = 0;
		while ((count = fs.read(buffer)) >= 0) {
			os.write(buffer, 0, count);
		}
		fs.close();
		os.close();
	}
	
	protected void sendBadRequest(HttpExchange t) throws IOException {
		String response = "Bad Request";
		t.sendResponseHeaders(400, response.length());
		OutputStream os = t.getResponseBody();
		os.write(response.getBytes());
		os.close();
	}
}
