import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TextEditPanel extends JPanel {
    private CardLayout cardLayout; // 메인 화면으로 돌아가기 위해 메개 변수로 가져온 cardLayout 사용 위함
    private JPanel mainPanel; // 메인 화면으로 돌아가기 위해 메개 변수로 가져온 mainPanel 사용 위함
    private JTextField tf = new JTextField(10);
    private TextSource textSource = null;

    public TextEditPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;

        makeToolBar();

        //setLayout(new BorderLayout()); // 레이아웃 설정

        this.textSource = textSource;
        this.setBackground(Color.cyan);
        add(tf);
        JButton btn = new JButton("추가");
        add(btn);


        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String word = tf.getText();
                if(word.length() == 0)
                    return;

                textSource.add(word);
                //파일에 추가한 단어를 저장?
            }
        });
    }

    public void makeToolBar() {
        JToolBar toolbar = new JToolBar();
        toolbar.setFloatable(false); // toolBar 움직이지 않게 함.
        this.add(toolbar, BorderLayout.NORTH);

        JButton exitButton = new JButton("Exit"); // 게임 종료
        toolbar.add(exitButton);

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
    }
}