import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class GameFrame extends JFrame{
    private JPanel mainPanel; // CardLayout 으로 화면 전환을 제어할 메인 패널
    private CardLayout cardLayout; // 화면 전환을 위한 CardLayout 사용

    private MenuPanel menuPanel; // MenuPanel 객체를 맴버 변수로 선언:(다른 Panel에서 MenuPanel 객체의 메소드를 사용할 수 있게 하기 위함)

//    private GameLayoutPanel gameLayoutPanel = new GameLayoutPanel(cardLayout, mainPanel);
//    private ScoreBoardPanel scoreBoardPanel = new ScoreBoardPanel(cardLayout, mainPanel);
//    private TextEditPanel textEditPanel = new TextEditPanel(cardLayout, mainPanel);

    public GameFrame() {
        setTitle("스타워즈 단어 게임");
        setSize(800, 600);
        setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // CardLayout 및 mainPanel, menuPanel 설정
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        menuPanel = new MenuPanel();

        // 각 화면의 패널을 mainPanel에 추가
        mainPanel.add(menuPanel, "MenuScreen");
        mainPanel.add(new GameLayoutPanel(cardLayout, mainPanel), "GameLayoutScreen"); // GameLayout에 cardLayout과 mainPanel 전달
        mainPanel.add(new ScoreBoardPanel(cardLayout, mainPanel), "ScoreScreen"); // ScoreBoard에 cardLayout과 mainPanel 전달
        mainPanel.add(new TextEditPanel(cardLayout, mainPanel), "TextEditScreen"); // TextEdit에 cardLayout과 mainPanel 전달

        // mainPanel을 프레임에 추가
        setContentPane(mainPanel);

        setVisible(true);
    }

    // 처음에 보여지는 시작(메뉴) 화면
    class MenuPanel extends JPanel{
        ImageIcon icon = new ImageIcon("images/Star_Wars_Logo.png"); //로고 이미지 로딩
        Image img = icon.getImage(); // 로고 이미지 객체
        private Clip clip; // 오디오 재생을 위한 Clip

        public MenuPanel() {
            this.setBackground(Color.black);
            this.setLayout(null);

            loadAudio("audio/Star-Wars.aiff");

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
                    clip.stop(); // 오디오 재생 멈춤

                    GameLayoutPanel newGameLayoutPanel = new GameLayoutPanel(cardLayout, mainPanel); // 새로운 newGameLayoutPanel 만듦
                    mainPanel.add(newGameLayoutPanel, "GameLayoutScreen"); // GameLayout에 cardLayout과 mainPanel 전달
                    System.out.println("newGameLayoutScreen 패널 생성 완료");

                    cardLayout.show(mainPanel, "GameLayoutScreen");
                }
            });
            // 지금까지 저장된 점수를 보는 패널로 넘어가는 액션
            scoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "ScoreScreen"); // ScoreLayoutScreen 이름의 컴포넌트로 뒤집음(flip)
                    clip.stop();
                }
            });
            // 게임에 나올 텍스트를 수정하는 패널로 넘어가는 액션
            textButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    cardLayout.show(mainPanel, "TextEditScreen"); // TextEditScreen 이름의 컴포넌트로 뒤집음(flip)
                    clip.stop();
                }
            });

            clip.setFramePosition(0); // 재생 위치를 첫 프레임으로 변경
            clip.start(); // 오디오 초기 재생
        }

        @Override
        public void paintComponent(Graphics g) { // 로고 그림
            super.paintComponent(g);
            g.drawImage(img, 187, 30, 426, 258,this); // 해당 위치에 해당 해상도로 그리기
        }

        private void loadAudio(String pathName) {
            try {
                clip = AudioSystem.getClip(); // 비어있는 오디오 클립 만들기
                File audioFile = new File(pathName); // 오디오 파일의 경료명
                AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile); // 오디오 파일로부터
                clip.open(audioStream); // 재생할 오디오 스트림 열기
            }
            catch (LineUnavailableException e) { e.printStackTrace(); }
            catch (UnsupportedAudioFileException e) { e.printStackTrace(); }
            catch (IOException e) { e.printStackTrace(); }
        }

        // 오디오 재생
        public void playAudio() {
            clip.setFramePosition(0); // 처음부터 재생
            clip.start();
        }

        // 오디오 중지
        public void stopAudio() {
            clip.stop();
        }
    }

    // menuPanel 반환
    public MenuPanel getMenuPanel() {
        return menuPanel;
    }

    public static void main(String[] args) {
        new GameFrame();
    }
}
