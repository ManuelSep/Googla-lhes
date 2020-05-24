import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class Worker implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;
	
	public Worker() {
		runWorker();
		handleServerAnswers();
	}
	
	public void runWorker() {
		try {
			socket = new Socket("localhost", 8080);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			out.writeObject("Worker");

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	private void handleServerAnswers(){
		try {
			while(true) {
				SimpleTask taskReceived = (SimpleTask) in.readObject();
				out.writeObject(searchWordInArticle(taskReceived));
			}
		} catch (ClassNotFoundException|IOException e) {
			e.printStackTrace();
		}
	}

	public Result searchWordInArticle(SimpleTask t) throws IOException {
		
		News article = t.getArticle();
		Result result = new Result(article.getId(), t.getWord(), howManyTimesWord(article, t.getWord()), article.getTitle(), article.getContent());
		System.out.println("worker result: " + result);
		return result;
	}
	
	public boolean hasWord(News article, String word) {
		
		if(article.getTitle().toLowerCase().contains(word.toLowerCase()) || article.getContent().toLowerCase().contains(word.toLowerCase()))
			return true;
		
		return false;
	}
	
	public int howManyTimesWord(News article, String word) {
		int counter = 0;
		if(hasWord(article, word)){
			counter = article.getContent().toLowerCase().split("\\b" + word.toLowerCase() + "\\b").length - 1;
			//	counter += title.toLowerCase().split("\\b" + word.toLowerCase() + "\\b").length - 1;
		}
		return counter;
	}
	
	public static void main(String[] args) {
		new Worker();
	}
}
