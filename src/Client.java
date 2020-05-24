import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	private GUI gui;
	
	public Client() throws ClassNotFoundException, IOException {
		gui = new GUI(this);
		runClient();
	}

	public void runClient() throws IOException, ClassNotFoundException {
		try {
			socket = new Socket("localhost", 8080);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			
			out.writeObject("Client");
			while (true) {
				handleServerAnswers();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleServerAnswers() throws ClassNotFoundException, IOException {
		System.out.println("reached handleServerAswers");
		try {
			@SuppressWarnings("unchecked")
			ArrayList<Result> results = (ArrayList<Result>) in.readObject();
			gui.showResults(results);

		} catch (Exception exception) {
			exception.getStackTrace();
		}
	}
	
	public void sendObject(Object obj) {
		try {
			out.writeObject(obj);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException {
		try {
			new Client();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
