import java.util.ArrayList;

public class BlockingQueue<T> {

	private ArrayList<T> tasks;
	
	public BlockingQueue() {
		tasks = new ArrayList<>();
	}
	
	
	public synchronized T getNextTask() {
		while(tasks.isEmpty())
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		
		return tasks.remove(0);
	}
	
	public synchronized void addTask(T task) {
		tasks.add(task);
		notifyAll();
	}

}
