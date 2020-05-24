public class FinalResult {

	private Result result;
	
	public FinalResult(Result result) {
		this.result = result;
	}
	
	public Result getResult() {
		return result;
	}
	
	@Override
	public String toString() {
		return result.getNumberOfOccurrences() + " - " + result.getTitle().toString() ;
	}
	
}
