import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class WeaponPanel extends JPanel{
    int currentWeapon = 10;
    JLabel imageLabel = new JLabel();
    ImageIcon blueWeapon = new ImageIcon("images/blue.png");
    ImageIcon greenWeapon = new ImageIcon("images/green.png");
    ImageIcon purpleWeapon = new ImageIcon("images/purple.png");

    public WeaponPanel() {
        imageLabel.setIcon(blueWeapon);
        this.setBackground(new Color(76, 144, 235));
        JButton upgradeButton = new JButton("Weapon Upgrade");
        upgradeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                upgradeWeapon();
            }
        });

        add(upgradeButton);
        add(imageLabel);
    }

    public int getIncrease() {
        return currentWeapon;
    }

    public void upgradeWeapon(){
        if(imageLabel.getIcon() != null && imageLabel.getIcon() instanceof ImageIcon){
            ImageIcon currentIcon = (ImageIcon) imageLabel.getIcon();
            if(currentIcon.getDescription().equals("images/blue.png")){
                ImageIcon newIcon = greenWeapon;
                imageLabel.setIcon(newIcon);
                currentWeapon += 10;
            }
            else if (currentIcon.getDescription().equals("images/green.png")){
                ImageIcon newIcon = purpleWeapon;
                imageLabel.setIcon(newIcon);
                currentWeapon += 10;
            }
            else{
                ImageIcon newIcon = blueWeapon;
                imageLabel.setIcon(newIcon);
                currentWeapon = 10;
            }
        }
    }
}
