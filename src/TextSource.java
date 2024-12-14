import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	
	//8장의 openChallenge 참고
	public TextSource() {
		v.add("Kitae");
		v.add("JiYun");
		v.add("Hansung");
		v.add("apple");
		v.add("Gold");
	}
	
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}
	
	public void add(String word) {
		v.add(word);
	}
}
