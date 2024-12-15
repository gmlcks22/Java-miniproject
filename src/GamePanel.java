import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

public class GamePanel extends JPanel{
	private JLabel fallingLabel = new JLabel(""); // 내려오는 라벨
	private GameGroundPanel ground = new GameGroundPanel();
	private InputPanel input = new InputPanel();
	private ScorePanel scorePanel = null;
	private TextSource textSource = null;
	private FallingThread fThread = new FallingThread();
	private GameStage gameStage = new GameStage(); // GameStage 형식의 객체 생성

	// 사용자 우주선 이미지 로딩 - 객체만들기
	private ImageIcon xWingIcon = new ImageIcon("images/x-wing.png"); // 사용자 우주선 이미지 로딩
	private Image xWingImg = xWingIcon.getImage(); // 이미지 객체

	// 1단계 적 우주선 이미지 로딩 - 객체 만들기
	private ImageIcon tieFighterIcon = new ImageIcon("Images/tie-fighter.png");
	private Image tieFighterImg = tieFighterIcon.getImage();

	// 2단계 적 우주선 이미지 로딩 - 객체 만들기
	private ImageIcon tieAdvancedIcon = new ImageIcon("Images/tie-advanced.png");
	private Image tieAdvancedImg = tieAdvancedIcon.getImage();

	// 3단계 적 우주선 이미지 로딩 - 객체 만들기
	private ImageIcon tieInterceptorIcon = new ImageIcon("Images/tie-interceptor.png");
	private Image tieInterceptorImg = tieInterceptorIcon.getImage();
	
	public GamePanel(ScorePanel scorePanel, TextSource textSource) {
		this.scorePanel = scorePanel;
		this.textSource = textSource;
		
		setLayout(new BorderLayout());
		add(ground, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
	}

	//game 시작하면 단어를 보이게 하고 newWord()
	public void startGame() {
		fallingLabel.setVisible(true);
		newWord();
		fThread.start(); // FallingThread 실행, start()
	}
	private void newWord() {
		//textSource한테서 다음 단어를 받아서 fallingLabel에 설정
		fallingLabel.setText(textSource.get());
		fallingLabel.setLocation((int)(Math.random()*470), 5); // 라벨 위치 설정 (0~470, 5)
	}

	private boolean stopFlag = false; // 단어 멈춤 여부 표시. false: stop아님

	// 게임 멈춤
	synchronized public void stopFalling() {
		stopFlag = true;
	}

	// 게임 계속(resume)
	synchronized public void resumeFalling() {
		stopFlag = false;
		this.notify(); // 해당 객체에서 wait의 목록에 저장된 쓰레드 중 하나를 꺠움 ->
	}

	// 멈춤 상태인지 확인
	synchronized public void checkStop() {
		if(stopFlag == true){ // 멈춤 상태이면
			try{
				this.wait();  // CPU의 스케쥴링 중단 => 더 이상 실행하지 않음, this는 Object의 wait을 부름. object의 wait 목록에 저장함
			} catch (InterruptedException e){
				return;
			}
		}
	}

	// 라벨(떨어지는 글자)를 붙이는 바탕 패널
	class GameGroundPanel extends JPanel {
		public GameGroundPanel() {
			setLayout(null);
			fallingLabel.setSize(80, 20); // 라벨 크기 설정
			fallingLabel.setLocation((int)(Math.random()*470), 5); // 라벨 처음 위치 설정
			add(fallingLabel);
			
			fallingLabel.setVisible(false); //fallingLabel을 안보이게 처리
		}
	}
	
	// 단어가 떨어지게끔 하는 Thread
	class FallingThread extends Thread{
		@Override
		// run()은 Virtual Machine이 호출하여 동작하는 함수이다.
		// 짠 코드 어딘가에서 부르는 것이 아님(return 불가하기 때문(무한루프라서))
		public void run() { //run()은 반드시 만들어야 함, 독립되서 동작함
			while(true) {
				checkStop(); // 멈춤 상태인지 확인
				fallingLabel.setLocation(fallingLabel.getX(), fallingLabel.getY()+5); // 단어 내려옮
				try {
					sleep(gameStage.getWordSpeed()); //1단계: 0.4초마다 sleep (400 mili sec)
				} catch (InterruptedException e) {
					return;
				} //0.4초간 잠자기
			}
		}
	}

	// 사용자 입력을 받는 창
	class InputPanel extends JPanel {
		private JTextField txtfield = new JTextField(10); // 창의 열 개수 10
		
		public InputPanel() {
			this.setBackground(Color.lightGray);
			add(txtfield); // 텍스트 필드 패널에 추가
			txtfield.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String text = t.getText(); // text: 사용자가 작성한 텍스트
					if(text.length() == 0) // 사용자가 아무것도 입력하지 않았으면
						return; // 리턴
					
					if(text.equals(fallingLabel.getText())){ // 사용자가 입력한 단어가 떨어지는 단어와 같은 단어이면
						scorePanel.increase(); // 점수 증가
						newWord(); // 새로운 단어 불러오기
						t.setText(""); // 현재 입력된 내용 지우기
					}
				}
			});
		}
	}

	// 스테이지 관리
	class GameStage {
		private int currentStage = 1; // 현재 스테이지(3스테이지까지)
		private int fallingWords = 1; // 떨어지는 단어 개수
		private int wordSpeed = 300; // 단어가 떨어지는 속도(Thread에서 sleep하는 간격)(ms)

		// 다음 스테이지
		public void nextStage() {
			currentStage++; // 스테이지 번호 증가
			fallingWords += 2; // 떨어지는 단어 개수 2개 증가
			wordSpeed -= 80; // 단어가 떨어지는 속도 증가
		}

		public int getCurrentStage() {
			return currentStage;
		}
		public int getFallingWords() {
			return fallingWords;
		}
		public int getWordSpeed() {
			return wordSpeed;
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(xWingImg, 225, 600, 100, 100, this); // 사용자 우주선 그리기
	}
}
