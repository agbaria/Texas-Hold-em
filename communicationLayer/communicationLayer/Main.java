package communicationLayer;

import java.io.IOException;

import Game.Enum.CardType;

public class Main {
	public static void main(String[] args) throws IOException {
		System.out.println(CardType.CLUBS);
		// runThreadPerClient(args);
		runHttpsServer(args);
	}

	private static void runThreadPerClient(String[] args) {
		if (args != null && args.length != 1) {
			System.err.println("Usage: java Thread per Client <port> ");
			System.exit(1);

		}
		int port = Integer.parseInt(args[0]);

		ThreadPerClientServer server = new ThreadPerClientServer(port);
		Thread serverThread = new Thread(server);
		serverThread.start();
		try {
			serverThread.join();
		} catch (InterruptedException e) {
			System.out.println("Server stopped");
		}
	}

	private static void runHttpsServer(String[] args) {
		int port = args.length < 2 ? 9000 : Integer.parseInt(args[1]);
		HTTPsServer webServer = new HTTPsServer(port);
		try {
			webServer.run();
		} catch (Exception e) {
			System.out.println("Error running web server on port " + port);
			e.printStackTrace();
		}
	}
}
