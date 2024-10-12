import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameFrame extends JFrame {
    StartPanel startPanel = new StartPanel(this);
    JLabel intro = new JLabel();
    public GameFrame()  {
        setSize(1030, 610);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(false);
        setResizable(false);
        setLocation(220, 20);
        setTitle("Ping Pong");

    }
    public void startIntro() {
        intro.setBounds(0, 0, 1030, 610);
        ImageIcon introIcon = new ImageIcon("D:\\intellij projects\\PingPong\\src\\main\\resources\\intro.gif");
        Image introimg = introIcon.getImage();
        Image introimgScale = introimg.getScaledInstance(intro.getWidth(), intro.getHeight(), Image.SCALE_DEFAULT);
        introIcon = new ImageIcon(introimgScale);
        intro.setIcon(introIcon);
        add(intro);
        startPanel.setVisible(false);
        intro.setVisible(true);
        Timer timer = new Timer(6000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intro.setVisible(false);
                startPanel.setVisible(true);
                add(startPanel);
            }
        });
        timer.setRepeats(false);
        timer.start();
    }
}
