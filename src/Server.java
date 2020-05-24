import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {

	public static final int PORTO = 8080;
	private ServerSocket serverSocket;
	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private ArrayList<DealWithClient> activeClients;
	private ArrayList<DealWithWorker> activeWorkers;
	private BlockingQueue<Task> blockingQueue;
	private File directory;
	private ArrayList<News> news = new ArrayList<>();
 	
	public Server(String file_path) throws ClassNotFoundException, IOException {
		activeClients = new ArrayList<>();
		activeWorkers = new ArrayList<>();
		blockingQueue = new BlockingQueue<>();
		loadFiles(file_path);
		startServer();
	}
	
	private void startServer() throws IOException, ClassNotFoundException {
		serverSocket = new ServerSocket(PORTO);
		
		while (true) {
		
			socket = serverSocket.accept();
			in = new ObjectInputStream(socket.getInputStream());
			out = new ObjectOutputStream(socket.getOutputStream());
			
			Object object = in.readObject();
			String st = (String) object;
			
			if (st.equals("Client")) {
				DealWithClient client = new DealWithClient(socket, out, in, this);
				client.start();
				addClient(client);
				
			}else {
				DealWithWorker worker = new DealWithWorker(socket, out, in, this);
				worker.start();
				addWorker(worker);
			}
		}
	}
	
	public void loadFiles(String file_path) throws FileNotFoundException{
		File file = new File(file_path);
		directory = file;
		File[] directoryListing = directory.listFiles();
  
		if (directoryListing != null) {
			for (File f : directoryListing) {
				
				News n = new News(f);
				if(n.getContent() != null) {
					news.add(n);
				}
				n.setId(news.indexOf(n));
			}
		} 
	}
	
	public ArrayList<News> getNews() {
		return news;
	}
	
	public BlockingQueue<Task> getBlockingQueue() {
		return blockingQueue;
	}
	
	private synchronized void addClient(DealWithClient client) {
		activeClients.add(client);
		System.out.println("Deal with Client added");
	}
	private synchronized void addWorker(DealWithWorker worker) {
		activeWorkers.add(worker);
		System.out.println("Deal with Worker added");
	}
	public synchronized void removeClient(DealWithClient client) {
		activeClients.remove(client);
		System.out.println("Deal with Client removed");
	}
	public synchronized void removeWorker(DealWithWorker worker) {
		activeWorkers.remove(worker);
		System.out.println("Deal with Worker removed");
	}
	
	public void closeServerSocket() throws IOException {
		System.out.println("Socket will close.");
		serverSocket.close();
	}
	
	public static void main(String[] args) {
			try {
				new Server("news");
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
}
