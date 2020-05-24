import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Comparator;

public class DealWithClient extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;
	private ArrayList<Result> results = new ArrayList<>();
	
	public DealWithClient(Socket socket, ObjectOutputStream out, ObjectInputStream in, Server server) {
		this.socket = socket;
		this.in = in;
		this.out = out;
		this.server = server;
	}
	
	public void sendMessage(Object ob) {
		try {
			out.writeObject(ob);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			
			while (true) {
				handleClient();
			}
		} catch (Exception e) {
			server.removeClient(this);
		}finally {
			closeSocket();
		}
	}

	private void handleClient() throws IOException, ClassNotFoundException {
		System.out.println("Handling with client");
		String wordReceived = (String) in.readObject();
		handleSearch(wordReceived);
	}

	private synchronized void handleSearch(String word) {
		try {
			results.clear();
			ArrayList<Task> tasks = new ArrayList<>();
			for (int i = 0; i < server.getNews().size(); i++) {
				Task task = new Task(this, word, server.getNews().get(i));
				tasks.add(task);
			}
	
			for (Task task : tasks) {
				server.getBlockingQueue().addTask(task);
			}
	
			while(results.size() < tasks.size()) {
	
				try {
					wait();
					System.out.println(results.size() + "/" + tasks.size());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			organizeResults();
			sendMessage(results);
		}catch (Exception e) {
			System.out.println("Client left");
		}
			
	}
	
	private void organizeResults() {
		results.sort(new Comparator<Result>() {
			@Override
	        public int compare(Result r1, Result r2) {
				return r2.getNumberOfOccurrences()-r1.getNumberOfOccurrences();
	        }
		});
	}
	
	private void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void addResult(Result result) {
		results.add(result);
		notify();
	}
}
