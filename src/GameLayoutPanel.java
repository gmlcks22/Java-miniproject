import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameLayoutPanel extends JPanel {
    private CardLayout cardLayout; // 메인 화면으로 돌아가기 위해 메개 변수로 가져온 cardLayout 사용 위함
    private JPanel mainPanel; // 메인 화면으로 돌아가기 위해 메개 변수로 가져온 mainPanel 사용 위함

    private TextSource textSource = new TextSource();
    private ScorePanel scorePanel = new ScorePanel();
    private GamePanel gamePanel = new GamePanel(scorePanel, textSource);

    public GameLayoutPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        setLayout(new BorderLayout());
        JSplitPane hPane = new JSplitPane();
        hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        hPane.setDividerLocation(550);

        JSplitPane vPane = new JSplitPane();
        vPane.setOrientation(JSplitPane.VERTICAL_SPLIT);

        hPane.setRightComponent(vPane);
        hPane.setLeftComponent(gamePanel);

        vPane.setDividerLocation(250);
        vPane.setTopComponent(scorePanel); // scorePanel TopComponent에 추가
        //vPane.setBottomComponent(editPanel);

        add(hPane, BorderLayout.CENTER);

        // 툴바 생성
        makeToolBar();

        gamePanel.startGame(); // 게임 시작
    }

    public void makeToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false); // toolBar 움직이지 않게 함.
        this.add(toolbar, BorderLayout.NORTH);

        JButton exitButton = new JButton("Exit"); // 게임 종료
        JButton pauseButton = new JButton("Pause");
        toolbar.add(exitButton);
        toolbar.add(pauseButton);

        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainPanel.remove(mainPanel.getComponent(1));
                System.out.println("기존 GameLayoutScreen 패널 삭제 완료");

                JFrame topFrame = (JFrame) SwingUtilities.getWindowAncestor(mainPanel); // 최상위 프레임 topFrame
                GameFrame gameFrame = (GameFrame) topFrame; // JFrame -> GameFrame 다운캐스팅
                GameFrame.MenuPanel menuPanel = gameFrame.getMenuPanel(); //
                menuPanel.playAudio(); // 오디오 재생

                cardLayout.show(mainPanel, "MenuScreen"); // MenuScreen 이름의 컴포넌트로 뒤집음(flip)
                System.out.println("메인 화면으로 이동");
            }
        });

        pauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // todo 게임 멈춤
            }
        });
    }
}