import java.io.Serializable;

public class Result implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private int newsId;
	private String word;
	private int numberOfOccurrences;
	private String content;
	private String title;
	
	public Result(int newsId, String word, int numberOfOccurrences,String title, String content){
		this.newsId = newsId;
		this.word = word;
		this.numberOfOccurrences = numberOfOccurrences;
		this.content = content;
		this.title = title;
	}
	
	public int getNewsId() {
		return newsId;
	}
	
	public String getWord() {
		return word;
	}

	public int getNumberOfOccurrences() {
		return numberOfOccurrences;
	}
	
	public String getContent() {
		return content;
	}
	
	public String getTitle() {
		return title;
	}

	@Override
	public String toString() {
		return "Result [word=" + word + ", numberOfOccurrences=" + numberOfOccurrences
				+ ", content=" + content + "]";
	}
}
