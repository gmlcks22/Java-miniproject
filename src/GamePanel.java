import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GamePanel extends JPanel{
	private JLabel fallingLabel = new JLabel("Hello"); //fallingLabel이 groundPanel보다 먼저 와야함 생성자에 fallingLabel이 사용되기 때문
	private GameGroundPanel ground = new GameGroundPanel();
	private InputPanel input = new InputPanel();
	private ScorePanel scorePanel = null;
	private TextSource textSource = null;
	private FallingThread fThread = new FallingThread();
	
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
		fThread.start(); //start() 는 쓰레드 클래스 안에 이미 있음
	}
	private void newWord() {
		//textSource한테서 다음 단어를 받아서 fallingLabel에 설정
		fallingLabel.setText(textSource.get());
		fallingLabel.setLocation(200, 50);
	}
	
	class GameGroundPanel extends JPanel {
		public GameGroundPanel() {
			setLayout(null);
			fallingLabel.setSize(50, 20);
			fallingLabel.setLocation(200, 50);
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
				fallingLabel.setLocation(fallingLabel.getX(), fallingLabel.getY()+10);
				try {
					sleep(400); //0.4초마다 sleep (400 mili sec)
				} catch (InterruptedException e) {
					return;
				} //0.4초간 잠자기
			}
		}
	}
	
	class InputPanel extends JPanel {
		private JTextField tf = new JTextField(10);
		
		public InputPanel() {
			this.setBackground(Color.lightGray);
			add(tf);
			tf.addActionListener(new ActionListener() {
				//actionListener를 상속받아서 만들기
				
				@Override
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField)e.getSource();
					String text = t.getText();
					if(text.length() == 0)
						return;
					
					if(text.equals(fallingLabel.getText())){
						scorePanel.increase();
						newWord();
						t.setText(""); // 현재 입력된 내용 지우기
					}
				}
			});
		}
	}
}
