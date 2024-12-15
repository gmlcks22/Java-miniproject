import javax.swing.*;
import java.awt.*;

public class TextEditPanel extends JPanel {
    private CardLayout cardLayout; // 메인 화면으로 돌아가기 위해 메개 변수로 가져온 cardLayout 사용 위함
    private JPanel mainPanel; // 메인 화면으로 돌아가기 위해 메개 변수로 가져온 mainPanel 사용 위함

    public TextEditPanel(CardLayout cardLayout, JPanel mainPanel) {
        this.cardLayout = cardLayout;
        this.mainPanel = mainPanel;


    }
}
