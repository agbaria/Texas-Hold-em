package communicationLayer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.security.KeyStore;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Hashtable;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManagerFactory;

import com.sun.net.httpserver.*;

import serviceLayer.serviceLayer;
import serviceLayer.serviceLayerInterface;

public class HTTPsServer {
	private int port;
	private serviceLayerInterface service;
	private Hashtable<String, String> tokens;

	public HTTPsServer(int port) {
		this.port = port;
		this.service = serviceLayer.getInstance();
		this.tokens = new Hashtable<>();
	}

	public void run() throws Exception {
		String keystoreFilename = System.getProperty("user.dir") + File.separator + "keystore.jks";
		char[] pass = "mypassword".toCharArray();
		// String alias = "selfsigned";
		FileInputStream fIn = new FileInputStream(keystoreFilename);
		KeyStore keystore = KeyStore.getInstance("JKS");
		keystore.load(fIn, pass);
		// Certificate cert = keystore.getCertificate(alias);
		// System.out.println(cert);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");
		kmf.init(keystore, pass);
		TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
		tmf.init(keystore);

		SSLContext sc = SSLContext.getInstance("TLS");
		sc.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

		HttpsServer server = HttpsServer.create(new InetSocketAddress(port), 0);

		server.setHttpsConfigurator(new HttpsConfigurator(sc) {
			public void configure(HttpsParameters params) {
				try {
					// Initialize the SSL context
					SSLContext c = SSLContext.getDefault();
					SSLEngine engine = c.createSSLEngine();
					params.setNeedClientAuth(false);
					params.setCipherSuites(engine.getEnabledCipherSuites());
					params.setProtocols(engine.getEnabledProtocols());
					// get the default parameters
					SSLParameters defaultSSLParameters = c.getDefaultSSLParameters();
					params.setSSLParameters(defaultSSLParameters);
				} catch (Exception ex) {
					ex.printStackTrace();
					System.out.println("Failed to create HTTPS server");
				}
			}
		});

		server.createContext("/", new IndexHandler());
		server.createContext("/styles.css", new StylesHandler());
		server.createContext("/scripts.js", new ScriptsHandler());
		server.createContext("/image", new ImagesHandler());
		server.createContext("/login", new LoginHandler());
		server.createContext("/signup", new SignupHandler());
		server.createContext("/signout", new SignoutHandler());
		server.createContext("/leaderboard", new LeaderboardHandler());
		server.createContext("/stats", new StatsHandler());
		// server.createContext("/", new handler());
		server.setExecutor(null);
		server.start();
	}

	class IndexHandler extends HTTPsHandler {
		public void handle(HttpExchange t) {
			try {
				printRequest(t);
				renderFile(t, "text/html", "index.html");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class StylesHandler extends HTTPsHandler {
		public void handle(HttpExchange t) {
			try {
				printRequest(t);
				String fileId = t.getRequestURI().getPath();
				renderFile(t, "text/css", fileId.substring(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ScriptsHandler extends HTTPsHandler {
		public void handle(HttpExchange t) {
			try {
				printRequest(t);
				String fileId = t.getRequestURI().getPath();
				renderFile(t, "text/javascript", fileId.substring(1));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class ImagesHandler extends HTTPsHandler {

		@Override
		public void handle(HttpExchange t) {
			try {
				printRequest(t);
				String[] q = t.getRequestURI().getRawQuery().split("=");
				if (q.length == 2 && "id".equals(q[0])) {
					String path = "images" + File.separator + q[1] + ".png";
					renderFile(t, "image/png", path);
				} else
					sendBadRequest(t);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class SignupHandler extends HTTPsHandler {

		@Override
		public void handle(HttpExchange t) {
			try {
				// printRequest(t);
				String reqBody = readRequestBody(t.getRequestBody());
				String[] q = reqBody.split("\\&");
				if (q.length != 4) {
					sendBadRequest(t);
				} else {
					String[] u = q[0].split("=");
					String[] p = q[1].split("=");
					String[] n = q[2].split("=");
					String[] e = q[3].split("=");
					if (!"username".equals(u[0]) || !"password".equals(p[0]) || !"name".equals(n[0])
							|| !"email".equals(e[0])) {
						sendBadRequest(t);
						return;
					}
					System.out.println("email: " + e[1]);
					String res = service.register(
							"REG " + u[1] + " " + p[1] + " " + n[1] + " " + e[1].replace("%40", "@") + " *AVATAR*");
					t.sendResponseHeaders(200, res.length());
					OutputStream os = t.getResponseBody();
					os.write(res.getBytes());
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class LoginHandler extends HTTPsHandler {

		@Override
		public void handle(HttpExchange t) {
			try {
				// printRequest(t);
				String reqBody = readRequestBody(t.getRequestBody());
				String[] q = reqBody.split("\\&");
				if (q.length != 2) {
					sendBadRequest(t);
				} else {
					String[] u = q[0].split("=");
					String[] p = q[1].split("=");
					if (!"username".equals(u[0]) || !"password".equals(p[0])) {
						sendBadRequest(t);
						return;
					}

					String response;
					int code = 200;
					String res = service.login("LOGIN " + u[1] + " " + p[1], null);
					String[] r = res.split(" ");
					if (r[1].equals("FAILED"))
						response = res;
					else {
						String name = r[3];
						String token = sha1(r[2] + (Math.random() * 1000));
						if (token == null) {
							response = "Internal Server Error";
							code = 500;
						}
						else {
							response = "{\"name\": \"" + name + "\", \"token\": \"" + token + "\"}";
							tokens.put(r[2], token);
							System.out.println("login  un: " + r[2] + ", to: " + token);
						}
					}
					t.sendResponseHeaders(code, response.length());
					OutputStream os = t.getResponseBody();
					os.write(response.getBytes());
					os.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class SignoutHandler extends HTTPsHandler {

		@Override
		public void handle(HttpExchange t) {
			try {
				// printRequest(t);
				String reqBody = readRequestBody(t.getRequestBody());
				String[] q = reqBody.split("\\&");
				if (q.length != 2) {
					sendBadRequest(t);
				} else {
					String[] un = q[0].split("=");
					String[] to = q[1].split("=");
					if (!"username".equals(un[0]) || !"token".equals(to[0])) {
						sendBadRequest(t);
					} else if (!to[1].equals(tokens.get(un[1]))) {
						sendBadRequest(t);
					} else {
						String res = service.logout("LOGOUT " + un[1]);
						t.sendResponseHeaders(200, res.length());
						OutputStream os = t.getResponseBody();
						os.write(res.getBytes());
						os.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class LeaderboardHandler extends HTTPsHandler {

		@Override
		public void handle(HttpExchange t) {
			try {
				//printRequest(t);
				String reqBody = readRequestBody(t.getRequestBody());
				System.out.println(reqBody);
				String[] q = reqBody.split("\\&");
				if (q.length != 3) {
					sendBadRequest(t);
				} else {
					String[] id = q[0].split("=");
					String[] un = q[1].split("=");
					String[] to = q[2].split("=");
					if (!"id".equals(id[0]) || !"username".equals(un[0]) || !"token".equals(to[0])) {
						sendBadRequest(t);
					} else if (!to[1].equals(tokens.get(un[1]))) {
						sendBadRequest(t);
					} else {
						String res = service.getLeadersboard("LEADERBOARD " + id[1]);
						t.sendResponseHeaders(200, res.length());
						OutputStream os = t.getResponseBody();
						os.write(res.getBytes());
						os.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	class StatsHandler extends HTTPsHandler {
		
		@Override
		public void handle(HttpExchange t) {
			try {
				//printRequest(t);
				String reqBody = readRequestBody(t.getRequestBody());
				System.out.println(reqBody);
				String[] q = reqBody.split("\\&");
				if (q.length != 4) {
					sendBadRequest(t);
				} else {
					String[] id = q[0].split("=");
					String[] us = q[1].split("=");
					String[] un = q[2].split("=");
					String[] to = q[3].split("=");
					if (!"id".equals(id[0]) || !"userstats".equals(us[0]) || !"username".equals(un[0]) || !"token".equals(to[0])) {
						sendBadRequest(t);
					} else if (!to[1].equals(tokens.get(un[1]))) {
						sendBadRequest(t);
					} else {
						String res = service.getUserStats("STATS " + id[1] + " " + us[1]);
						int code = 200;
						if (res.equals("Bad Request"))
							code = 400;
						t.sendResponseHeaders(code, res.length());
						OutputStream os = t.getResponseBody();
						os.write(res.getBytes());
						os.close();
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	class handler extends HTTPsHandler {

		@Override
		public void handle(HttpExchange t) {

			try {
				printRequest(t);

				String response = "Not Found";
				t.sendResponseHeaders(404, response.length());
				OutputStream os = t.getResponseBody();
				os.write(response.getBytes());
				os.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	private String sha1(String input) {
		MessageDigest mDigest;
		try {
			mDigest = MessageDigest.getInstance("SHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		byte[] result = mDigest.digest(input.getBytes());
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < result.length; i++) {
			sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
		}

		return sb.toString();
	}
}
