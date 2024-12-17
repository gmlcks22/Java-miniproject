import java.util.Vector;

public class TextSource {
	private Vector<String> v = new Vector<String>();
	private Vector<String> using = new Vector<String>(); // 사용할 단어

	private int usingIndex = 0;

	public TextSource() {
		v.add("starwars");
		v.add("스타워즈");
		v.add("DarthVader");
		v.add("Yoda");
		v.add("다스베이더");
		v.add("요다");
		v.add("father");
		v.add("광선검");
		v.add("deathStar");
		v.add("조지루카스");
		v.add("존윌리엄스");
		v.add("GeorgeLucas");
		v.add("JohnWilliams");
		v.add("aNewHope");
		v.add("movie");
		v.add("새로운희망");
		v.add("영화");
		v.add("자바");
		v.add("java");
		v.add("제다이");
		v.add("디즈니");
		v.add("fox");

		reset();
	}

	public String get() {
		String word = using.get(usingIndex);
		usingIndex++;
		return word;
	}

	public void reset() {
		usingIndex = 0;
		// v에 저장된 단어를 using에 랜덤하게 배열
		while (true) {
			// 단어를 모두 using에 저장한 경우 작업 완료
			if (using.size() == v.size()) {
				break;
			}

			// 랜덤한 인덱스 생성
			int index = (int)(Math.random() * v.size());
			String word = v.get(index);

			// 아직 using에 추가하지 않은 단어라면 추가
			if (!using.contains(word)) {
				using.add(word); // 사용할 단어 벡터에 추가
			}
		}
	}

	// using 벡터의 크기를 리턴
	public int getSourceSize() {
		return using.size();
	}

	// 새로운 word를 v에 추가
	public void add(String word) {
		v.add(word);
	}
}
