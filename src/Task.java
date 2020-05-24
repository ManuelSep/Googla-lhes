import java.io.Serializable;

public class Task implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private DealWithClient dealWithClient;
	private String word;
	private News article;

	public Task(DealWithClient dealWithClient, String word, News article) {
		this.dealWithClient = dealWithClient;
		this.word = word;
		this.article = article;
	}
	
	public DealWithClient getDealWithClient() {
		return dealWithClient;
	}
	
	public String getWord() {
		return word;
	}
	
	public News getArticle(){
		return article;
	}
	
	public SimpleTask getSimpleTask() {
		return new SimpleTask(word, article);
	}
	
	@Override
	public String toString() {
		return "Word: " + word + ", article: " + article + "\n";
	}
}
