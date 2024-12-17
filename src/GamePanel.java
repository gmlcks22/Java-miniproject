import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;

public class GamePanel extends JPanel{
	private Vector<JPanel> fallingPanels = new Vector<JPanel>(); // 내려오는 단어, 우주선

	private ScorePanel scorePanel = null;
	private TextSource textSource = null;
	private GameStage gameStage;
	private GameGroundPanel ground;
	private InputPanel input;
	private FallingThread fThread;
	private Player player;

	public GamePanel(ScorePanel scorePanel, TextSource textSource) {
		this.scorePanel = scorePanel;
		this.textSource = textSource;

		gameStage = new GameStage(); // GameStage 형식의 객체 생성
		ground= new GameGroundPanel();
		input = new InputPanel();
		fThread = new FallingThread();
		player = new Player(); // Player 객체 생성

		setLayout(new BorderLayout());
		add(ground, BorderLayout.CENTER);
		add(input, BorderLayout.SOUTH);
	}

	// 쓰레드 시작
	public void startGame() {
		fThread.start();
	}

	// <적 우주선> 글자와 이미지를 포함하는 패널 생성
	synchronized private JPanel createWordPanel() {
		JPanel wordPanel = new JPanel();
		wordPanel.setLayout(new BorderLayout()); // 글자와 이미지를 위아래로 배치하기 위함

		// 이미지 라벨, 적 우주선 여러개
		int index = (int)(Math.random()*3); // 0~2 랜덤
		switch (index){
			case 0:
				JLabel imageLabel1 = new JLabel(new ImageIcon("images/tie-fighter.png")); // 이미지 설정
				wordPanel.add(imageLabel1, BorderLayout.CENTER);
				break;
			case 1:
				JLabel imageLabel2 = new JLabel(new ImageIcon("images/tie-advanced.png")); // 이미지 설정
				wordPanel.add(imageLabel2, BorderLayout.CENTER);
				break;
			case 2:
				JLabel imageLabel3 = new JLabel(new ImageIcon("images/tie-interceptor.png")); // 이미지 설정
				wordPanel.add(imageLabel3, BorderLayout.CENTER);
				break;
		}

		// 글자 라벨
		JLabel textLabel = new JLabel("", JLabel.CENTER); // 텍스트 라벨 생성
		textLabel.setText(textSource.get()); // 단어 랜덤 설정
		wordPanel.add(textLabel, BorderLayout.SOUTH);

		return wordPanel;
	}

	// 패널 위치 초기화 스테이지마다 단어의 밀도가 달라짐
	synchronized private void resetLocation(JPanel wordPanel){
		switch (gameStage.getCurrentStage()){
			case 1:
				wordPanel.setLocation(((int)(Math.random() * 470)), ((int)(-Math.random()*1100)-100)); // 컨테이너 초기 위치 랜덤 설정
				break;
			case 2:
				wordPanel.setLocation(((int)(Math.random() * 470)), ((int)(-Math.random()*900)-100)); // 컨테이너 초기 위치 랜덤 설정
				break;
			case 3:
				wordPanel.setLocation(((int)(Math.random() * 470)), ((int)(-Math.random()*700)-100)); // 컨테이너 초기 위치 랜덤 설정
				break;
		}
	}

	synchronized private void fallWords() {
		for (int i=0; i<fallingPanels.size(); i++) {
			JPanel wordPanel = fallingPanels.get(i); // i번째 단어 패널 가져오기

			// 단어가 화면 하단에 도달했는지 확인 & 높이 초기화
			if (wordPanel.getY() > 580) {
				System.out.println("Word reached bottom: " + wordPanel.getY() + wordPanel.getComponent(1));
				player.hitted(); // 플레이어 HP 감소
				resetLocation(wordPanel); // 패널 위치 초기화
			}

			wordPanel.setLocation(wordPanel.getX(), wordPanel.getY() + gameStage.getWordSpeed()); // y좌표로 내리기
		}
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
		private ImageIcon icon1 = new ImageIcon("images/deathstar_space.jpg"); // 1단계 배경
		private ImageIcon icon2 = new ImageIcon("images/trench_outside.jpg"); // 2단계 배경
		private ImageIcon icon3 = new ImageIcon("images/trench_inside.jpg"); // 3단계 배경
		private Image img1 = icon1.getImage(); // 1단계 이미지 객체
		private Image img2 = icon2.getImage(); // 2단계 이미지 객체
		private Image img3 = icon3.getImage(); // 2단계 이미지 객체

		public GameGroundPanel() {
			setLayout(null);
			setGroundPanel();
		}
		// 단어 패널 생성, 초기 위치 설정
		public void setGroundPanel() {
			// 패널 크기, 처음 위치 설정
			for(int i=0; i<textSource.getSourceSize(); i++) {
				JPanel wordPanel = createWordPanel(); // 벡터의 요소 개수만큼 새로운 단어 패널 생성
				wordPanel.setSize(80, 100); // 패널 크기(80, 100)
				resetLocation(wordPanel); // 패널 위치 초기화
				wordPanel.setVisible(true); // 보이게 설정
				fallingPanels.add(wordPanel); // fallingPanels 벡터에 추가
				System.out.println("Falling Panels Size: " + fallingPanels.size());
				add(wordPanel); // 화면에 추가
			}
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			g.setClip(0, 0, 550, 600); // GameGroundPanel 만 클리핑
			switch (gameStage.getCurrentStage()){
				case 1:
					g.drawImage(img1, 0, 0, getWidth(), getHeight(), this);
					break;
				case 2:
					g.drawImage(img2, 0, 0, getWidth(), getHeight(), this);
					break;
				case 3:
					g.drawImage(img3, 0, 0, getWidth(), getHeight(), this);
					break;
			}
		}
	}
	
	// 단어가 떨어지게끔 하는 Thread
	class FallingThread extends Thread {
		int n = 1;
		@Override
		public void run() {
			gameStage.setStage(n);

			// 단어가 아래로 떨어지는 동작
			while (true) {
				checkStop(); // 멈춤 상태인지 확인
				fallWords(); // 패널(단어) 이동 & 맨 밑까지 내려왔을 때 패널 위치 초기화
				repaint();
				try {
					Thread.sleep(300); // 단어 내려오는 속도 0.3초 sleep
				} catch (InterruptedException e) {
					return;
				}
				// 일정 점수 이상이면
				if(scorePanel.getScore() > gameStage.getScoreStandard()){
					if (n > 2) { // 마지막 스테이지(3)에 진입했을 때
						continue;
					}
					else{
						System.out.println("Stage" + n + " Clear!");
						ground.repaint(); // 스테이지 변경 --> 새로 세팅
						n++;
						gameStage.setStage(n); // 다음 스테이지로
					}
				}
			}
		}
	}


	// 사용자 입력을 받는 창
	class InputPanel extends JPanel {
		private JTextField txtfield = new JTextField(10); // 창의 열 개수 10

		public InputPanel() {
			this.setBackground(Color.lightGray);
			add(txtfield); // 텍스트 필드 패널에 추가

			// 사용자가 텍스트 입력 후 엔터키를 누를 때 동작
			txtfield.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String text = t.getText().strip(); // text: 사용자가 작성한 텍스트

					// 벡터를 순회하며 입력 덱스트와 일치하는 단어 찾기
					for(int i=0; i<fallingPanels.size(); i++) {
						JPanel wordPanel = fallingPanels.get(i); // 현재 단어 패널 가져오기
						JLabel textLabel = (JLabel) wordPanel.getComponent(1); // 패널 안의 단어 라벨 가져오기

						if(text.length() == 0) // 사용자가 아무것도 입력하지 않았으면
							return; // 리턴

						// 입력한 텍스트와 단어가 일치 && gamePanel 안에(보이는 상태) 있으면
						if (textLabel.getText().equals(text) && wordPanel.getY()>=(-80) && wordPanel.getY()<600) {
							// todo debugging
							System.out.println("Removing word: " + textLabel.getText());
							System.out.println("Remaining falling panels: " + fallingPanels.size());
							scorePanel.increase(); // 점수 증가
							t.setText(""); // 입력 창
							resetLocation(wordPanel);
						}
						// 입력한 텍스트가 불일치하면
						else if(textLabel.getText().equals(text) && wordPanel.getY()>=(-80) && wordPanel.getY()<600){
							return;
						}
					}
				}
			});
		}
	}

	// 스테이지 관리
	class GameStage {
		private int currentStage = 1; // 현재 스테이지(3스테이지까지)
		private int wordSpeed = 3; // 단어가 떨어지는 속도(단어가 한 번에 이동하는 간격)
		private int scoreStandard; // 점수 기준

		// 스테이지 설정
		synchronized public void setStage(int n) {
			switch (n){
				case 1:
					scoreStandard = 100;
					currentStage = 1;
					wordSpeed = 3;
					break;
				case 2:
					scoreStandard = 300;
					currentStage = 2;
					wordSpeed = 4;
					break;
				case 3:
					currentStage = 3;
					wordSpeed = 5;
					break;
				default:
					System.out.println("올바른 스테이지 접근이 아닙니다");
			}
		}
		synchronized public int getScoreStandard() {return scoreStandard;}
		synchronized public int getCurrentStage() {return currentStage;}
		synchronized public int getWordSpeed() {return wordSpeed;}
	}

	class Player {
		private int HP = 5; // 플레이어 hp 5;

		// 끝까지 타이핑 못했을 때
		synchronized public void hitted() {
			HP--;
			System.out.println("hitted!");
			if(HP==0){
				System.out.println("HP 0");
				stopFalling();
				fThread.interrupt(); // 쓰레드 종료
				repaint();
				JOptionPane.showMessageDialog(null, "HP is 0, Exit 버튼을 눌러주세요.", "Message", JOptionPane.ERROR_MESSAGE);
				stopFlag = true; // 게임 상태를 종료로 설정
			}
		}
	}
}
