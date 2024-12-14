import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame{
    private JPanel mainPanel; // CardLayout 으로 화면 전환을 제어할 메인 패널
    private CardLayout cardLayout; // 화면 전환을 위한 CardLayout 사용

    private GameLayoutPanel gameLayoutPanel = new GameLayoutPanel(cardLayout, mainPanel);

    //private EditPanel editPanel = new EditPanel(textSource);

    public GameFrame() {
        setTitle("스타워즈 단어 게임");
        setSize(800, 600);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CardLayout 및 mainPanel 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 화면의 패널을 mainPanel에 추가
        mainPanel.add(new MenuPanel(), "MenuScreen");
        mainPanel.add(new GameLayoutPanel(cardLayout, mainPanel), "GameLayoutScreen"); // GameLayout에 cardLayout과 mainPanel 전달
        //mainPanel.add(new ScoreBoardPanel(), "ScoreScreen");
        //mainPanel.add(new TextEditPanel(), "TextEditScreen");

        // mainPanel을 프레임에 추가
        setContentPane(mainPanel);

        setVisible(true);
    }

    // 처음에 보여지는 시작(메뉴) 화면
    class MenuPanel extends JPanel{
        public MenuPanel() {
            this.setBackground(Color.black);
            this.setLayout(null);

            JButton gameButton = new JButton("게임 시작"); // 게임 시작 버툰
            JButton scoreButton = new JButton("점수 보기"); // 점수를 보는 버튼
            JButton textButton = new JButton("단어 수정"); // 게임에 나올 텍스트를 수정하는 버튼

            // 버튼의 위치와 크기 조정
            gameButton.setLocation(250, 300);
            scoreButton.setLocation(250, 360);
            textButton.setLocation(250, 420);
            gameButton.setSize(300, 50);
            scoreButton.setSize(300, 50);
            textButton.setSize(300, 50);

            // 버튼을 패널에 부착
            this.add(gameButton);
            this.add(scoreButton);
            this.add(textButton);


            // 게임 실행 패널로 넘어가는 액션
            gameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "GameLayoutScreen"); // GameLayoutScreen 이름의 컴포넌트로 뒤집음(flip)
                }
            });
            // 지금까지 저장된 점수를 보는 패널로 넘어가는 액션
            scoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "ScoreScreen"); // ScoreLayoutScreen 이름의 컴포넌트로 뒤집음(flip)
                }
            });
            // 게임에 나올 텍스트를 수정하는 패널로 넘어가는 액션
            textButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "TextEditScreen"); // TextEditScreen 이름의 컴포넌트로 뒤집음(flip)
                }
            });
        }
    }



//    class ScoreBoardPanel extends JPanel {
//
//    }
//
//    class TextEditPanel extends JPanel {
//
//    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
