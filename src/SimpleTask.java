import java.io.Serializable;

public class SimpleTask implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String word;
	private News article;
	
	public SimpleTask(String word, News article) {
		this.word = word;
		this.article = article;
	}
	
	public String getWord() {
		return word;
	}
	
	public News getArticle(){
		return article;
	}

	@Override
	public String toString() {
		return "SimpleTask [word=" + word + ", article=" + article + "]";
	}
	
}
