package webserver;

import java.net.*;
import java.io.*;
import java.util.StringTokenizer;

public class WebServer extends Thread {
	protected Socket clientSocket;
	private static final String INDEX = "C:\\Users\\CRW_GARAJ\\Desktop\\index.html";
	private static final String ERROR = "C:\\Users\\CRW_GARAJ\\Desktop\\eroare.html";
	private static final String STOPPED = "C:\\Users\\CRW_GARAJ\\Desktop\\stop.html";
	private static final String MAINTENANCE = "C:\\Users\\CRW_GARAJ\\Desktop\\mentenanta.html";
	private static int status = 1;

	public static int getStatus() {
		return status;
	}

	public Socket getClientSocket() {
		return clientSocket;
	}

	public static String getERROR() {
		return ERROR;
	}

	public static String getSTOPPED() {
		return STOPPED;
	}

	public static String getMAINTENANCE() {
		return MAINTENANCE;
	}

	public static String getINDEX() {
		return INDEX;
	}

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = null;

		try {
			serverSocket = new ServerSocket(10008);
			System.out.println("Connection Socket Created");
			try {
				while (true) {
					System.out.println("Waiting for Connection");
					new WebServer(serverSocket.accept());
				}
			} catch (IOException e) {
				System.err.println("Accept failed.");
				System.exit(1);
			}
		} catch (IOException e) {
			System.err.println("Could not listen on port: 10008.");
			System.exit(1);
		} finally {
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.err.println("Could not close port: 10008.");
				System.exit(1);
			}
		}
	}

	public WebServer(Socket clientSoc) {
		clientSocket = clientSoc;
		start();
	}

	public void run() {
		System.out.println("New Communication Thread Started");
		try {
			PrintWriter out = new PrintWriter(clientSocket.getOutputStream(),
					true);
			BufferedReader in = new BufferedReader(new InputStreamReader(
					clientSocket.getInputStream()));

			//declar inputLine cu scopul de a salva prima linie din request-ul pe care il face clientul la server
			String inputLine = in.readLine();

			//declar StringTokenizer pentru a parcurge raspunsul de la server
			StringTokenizer parse = new StringTokenizer(inputLine);

			//declar method in care salvez tipul request-ului, in cazul nostru GET
			String method = parse.nextToken().toUpperCase();

			//fisierul de request
			String requestedFile = parse.nextToken().toLowerCase();

			if(status == 1) {
				Running(out, method, requestedFile);
			}

			if(status == 2){
				Stopped(out);
			}

			if(status == 3){
				Maintenance(out);
			}

			out.close();
			in.close();
			clientSocket.close();

		} catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}
	private void Running(PrintWriter out, String method, String requestedFile){
		try{
			if(method.equals("GET")){
				if(requestedFile.endsWith("/") || requestedFile.endsWith("index.html") ){
					File file = new File(INDEX);
					//creez vectorul pentru a stoca datele din fisier
					byte[] fileData = new byte[(int)file.length()];
					FileInputStream inFile = new FileInputStream(file);

					inFile.read(fileData);
					out.println("HTTP/1.0 200 OK");
					out.println("Content-Type: text/html");
					out.println("\r\n");
					out.flush();
					inFile.close();

					String fileContent = new String(fileData);
					out.println(fileContent);
					out.close();
				}
				else{
					File file = new File(ERROR);
					//creez vectorul pentru a stoca datele din fisier
					byte[] fileData = new byte[(int)file.length()];
					FileInputStream inFile = new FileInputStream(file);
					inFile.read(fileData);

					out.println("HTTP/1.1 404 File Not Found");
					out.println("Content-Type: text/html");
					out.println("\r\n");
					out.flush();
					inFile.close();

					String responseFileContent = new String(fileData);
					out.println(responseFileContent);
					out.close();
				}
			}
		}catch (IOException e) {
			System.err.println("Problem with Communication Server");
			System.exit(1);
		}
	}

	private void Maintenance(PrintWriter out){
		try{
			File file = new File(MAINTENANCE);
			//creez vectorul pentru a stoca datele din fisier
			byte[] fileData = new byte[(int)file.length()];
			FileInputStream inFile = new FileInputStream(file);
			inFile.read(fileData);

			out.println("HTTP/1.1 200 OK");
			out.println("Content-Type: text/html");
			out.println("\r\n");
			out.flush();
			inFile.close();

			String responseFileContent = new String(fileData);
			out.println(responseFileContent);
			out.close();

		}catch(IOException e){
			System.err.println("Problem with Communication Server");
			System.exit(2);
		}
	}

	private void Stopped(PrintWriter out){
		try{
			File file = new File(STOPPED);
			//creez vectorul pentru a stoca datele din fisier
			byte[] fileData = new byte[(int)file.length()];
			FileInputStream inFile = new FileInputStream(file);
			inFile.read(fileData);

			out.println("HTTP/1.1 522 Connection Timeout");
			out.println("Content-Type: text/html");
			out.println("\r\n");
			out.flush();
			inFile.close();

			String responseFileContent = new String(fileData);
			out.println(responseFileContent);
			out.close();

		}catch(IOException e){
			System.err.println("Problem with Communication Server");
			System.exit(2);
		}
	}
}