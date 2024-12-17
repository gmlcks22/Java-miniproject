import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class ScorePanel extends JPanel{
	private WeaponPanel weaponPanel;

	private int score = 0;
	private JLabel scoreLabel = new JLabel(Integer.toString(score));
	public ScorePanel() {
		weaponPanel = new WeaponPanel();
		this.setBackground(new Color(209, 66, 42));
		add(new JLabel("점수"));
		add(scoreLabel);
	}

	public int getScore() {return score;}
	
	public void increase() {
		score += weaponPanel.getIncrease();
		scoreLabel.setText(Integer.toString(score));
	}
}
