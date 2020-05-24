import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class DealWithWorker extends Thread {

	private Socket socket;
	private ObjectInputStream in;
	private ObjectOutputStream out;
	private Server server;

	public DealWithWorker(Socket socket, ObjectOutputStream out, ObjectInputStream in,
			Server server) throws ClassNotFoundException, IOException {
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
				
					System.out.println("chegou ao DWW");
					Task task = server.getBlockingQueue().getNextTask();
					SimpleTask simpleTask = task.getSimpleTask();
					sendMessage(simpleTask);
	
					System.out.println("Handling with worker");
					Result result = (Result) in.readObject();
					System.out.println(result);
					task.getDealWithClient().addResult(result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}finally {
				closeSocket();
			}
		
	}

	private void closeSocket() {
		try {
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}

