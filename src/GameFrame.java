import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;

public class GameFrame extends JFrame {
	// 계속해서 접근할 것이기 때문에 여기에서 선언-생성
	private TextSource textSource = new TextSource();
	private ScorePanel scorePanel = new ScorePanel(); 
	private EditPanel editPanel = new EditPanel(textSource);
	private GamePanel gamePanel = new GamePanel(scorePanel, textSource);
	
	public GameFrame() { //topFrame이기 때문에 이 frame 은 어디에 따로 붙을 필요는 없다
		setTitle("게임");
		setSize(800, 600);
		setResizable(false); //this. 써도 안써도 됨
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		makeMenu(); // 14-1에 나옴
		makeToolBar();
		makeSplit();
		setVisible(true);
	}
	
	private void makeSplit() {
		JSplitPane hPane = new JSplitPane();
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(550);
		//hPane.set //Divider 위치를 고정시키는 메소드
		getContentPane().add(hPane, BorderLayout.CENTER);
		
		JSplitPane vPane = new JSplitPane();
		vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		hPane.setRightComponent(vPane);
		hPane.setLeftComponent(gamePanel);
		vPane.setDividerLocation(250);
		vPane.setTopComponent(scorePanel); // scorePanel TopComponent에 추가
		vPane.setBottomComponent(editPanel);
	}
	
	private void makeToolBar() {
		JToolBar tBar = new JToolBar();
		tBar.setFloatable(false); // toolBar 움직이지 않게 함.
		getContentPane().add(tBar, BorderLayout.NORTH);
		JButton startBtn = new JButton("Start");
		tBar.add(startBtn);
		
		//start 버튼을 눌러야 단어가 보이게 동작
		startBtn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.startGame();
			}
			
		});
	}
	
	//14-1에 나오는 menu 만들기
	private void makeMenu() {
		JMenuBar mb = new JMenuBar();
		this.setJMenuBar(mb); // menuBar는 한 윈도우에 한 개이기 떄문에 set~ 형식의 메소드임
		
		JMenu fileMenu = new JMenu("File");
		mb.add(fileMenu);
		
		JMenu editMenu = new JMenu("Edit");
		mb.add(editMenu);
		
		//start 버튼을 눌러야 단어가 보이게 동작
		JMenuItem startItem = new JMenuItem("Start");
		fileMenu.add(startItem);
		startItem.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				gamePanel.startGame();
			}
			
		});
		
		JMenuItem stopItem = new JMenuItem("Stop");
		fileMenu.add(stopItem);
		JMenuItem exitItem = new JMenuItem("Exit");
		fileMenu.add(exitItem);
	}
	
	public static void main(String[] args) {
		new GameFrame();
	}
}
