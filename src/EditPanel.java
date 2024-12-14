import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EditPanel extends JPanel{
	private JTextField tf = new JTextField(10);
	private TextSource textSource = null;
	//만들어질 때 textSource 정보를 받음
	public EditPanel(TextSource textSource) {
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
}
