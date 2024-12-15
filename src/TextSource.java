import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	
	public TextSource() {
		v.add("Kitae");
		v.add("JiYun");
		v.add("Hansung");
		v.add("apple");
		v.add("Gold");
	}

	// v에 저장된 단어들 중 하나를 랜덤하게 선택해 반환
	public String get() {
		int index = (int)(Math.random()*v.size());
		return v.get(index);
	}

	// 새로운 word를 v에 추가
	public void add(String word) {
		v.add(word);
	}
}
