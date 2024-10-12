import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class StartPanel extends JPanel implements ActionListener {
    GameFrame gameFrame;
    JLabel pingpongIcon = new JLabel();
    JLabel redplayer = new JLabel("for player 1 use up/down");
    JLabel blueplayer = new JLabel("for player 2 use W/S");
    JButton play = new JButton("Play");
    JButton Audio = new JButton("Music on");
    JButton Player1 = new JButton("1 Player");
    JButton Player2 = new JButton("2 Player");
    Clip clip;
    public StartPanel(GameFrame gameFrame){
        this.gameFrame = gameFrame;
        setLayout(null);
        setBackground(Color.BLACK);
        setBounds(0,0,1030,610);
        pingpongIcon.setBounds(330,40,350,350);
        ImageIcon psicone = new ImageIcon("D:\\intellij projects\\PingPong\\src\\main\\resources\\pingpongImage.png");
        Image psimg = psicone.getImage();
        Image psimgScale = psimg.getScaledInstance(pingpongIcon.getWidth(), pingpongIcon.getHeight(), Image.SCALE_SMOOTH);
        psicone = new ImageIcon(psimgScale);
        pingpongIcon.setIcon(psicone);
        add(pingpongIcon);
        play.setBounds(450,380,110,40);
        play.setBackground(Color.decode("#4ba600"));
        play.setFont(new Font("a",Font.BOLD,17));
        play.setCursor(new Cursor(Cursor.HAND_CURSOR));
        play.setFocusable(false);
        play.addActionListener(this);
        play.setVisible(true);
        add(play);
        Audio.setBounds(450,440,110,40);
        Audio.setBackground(Color.decode("#4ba600"));
        Audio.setFont(new Font("a",Font.BOLD,17));
        Audio.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Audio.setFocusable(false);
        Audio.addActionListener(this);
        Audio.setVisible(true);
        add(Audio);
        Player1.setBounds(450,380,110,40);
        Player1.setBackground(Color.decode("#4ba600"));
        Player1.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Player1.setFont(new Font("a",Font.BOLD,17));
        Player1.setFocusable(false);
        Player1.addActionListener(this);
        Player1.setVisible(false);
        add(Player1);
        Player2.setBounds(450,460,110,40);
        Player2.setBackground(Color.decode("#4ba600"));
        Player2.setCursor(new Cursor(Cursor.HAND_CURSOR));
        Player2.setFont(new Font("a",Font.BOLD,17));
        Player2.setFocusable(false);
        Player2.addActionListener(this);
        Player2.setVisible(false);
        add(Player2);
        redplayer.setBounds(100,250,230,50);
        redplayer.setFont(new Font("a", Font.BOLD,18));
        redplayer.setForeground(Color.red);
        redplayer.setVisible(false);
        add(redplayer);
        blueplayer.setBounds(100,320,200,50);
        blueplayer.setFont(new Font("a", Font.BOLD,18));
        blueplayer.setForeground(Color.blue);
        blueplayer.setVisible(false);
        add(blueplayer);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:\\intellij projects\\PingPong\\src\\main\\resources\\music.wav").getAbsoluteFile());
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == play) {
            play.setVisible(false);
            Audio.setVisible(false);
            Player1.setVisible(true);
            Player2.setVisible(true);
            redplayer.setVisible(true);
            blueplayer.setVisible(true);
        }
        if (e.getSource() == Audio) {
            if (clip.isRunning()){
                Audio.setText("Music off");
                clip.stop();
            } else if (!clip.isRunning()) {
                Audio.setText("Music on");
                clip.start();
            }
        }
        if (e.getSource() == Player1) {
            GamePanel gamePanel = new GamePanel(gameFrame);
            gamePanel.computerTurn = true;
            setVisible(false);
            gamePanel.setVisible(true);
            gamePanel.setFocusable(true);
            switchToGamePanel(gamePanel);
            clip.stop();
        }
        if (e.getSource() == Player2) {
            GamePanel gamePanel = new GamePanel(gameFrame);
            gamePanel.computerTurn = false;
            setVisible(false);
            gamePanel.setVisible(true);
            gamePanel.setFocusable(true);
            switchToGamePanel(gamePanel);
            clip.stop();
        }
    }
    public void switchToGamePanel(GamePanel gamePanel){

        gameFrame.getContentPane().removeAll();
        gameFrame.add(gamePanel);
        gameFrame.revalidate();
        gameFrame.repaint();
    }
}
