import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    GameFrame gameFrame;
    JPanel pausPanel = new JPanel();
    JButton resumeButton = new JButton("resume");
    JButton AudioButton = new JButton("Sound on");
    JButton QuitButton = new JButton("Quit");
    Color redScoreColor = Color.white;
    Color blueScoreColor = Color.white;
    Color ballColor = Color.WHITE;
    final int PANEEL_WIDTH = 1030;
    final int PANEEL_HEIGHT = 610;
    Timer timer;
    Rectangle redPaddle;
    Rectangle bluePaddle;
    Rectangle ball;
    int redyVelocity = 18;
    int blueyVelocity = 18;
    int redPaddlewidth = 20;
    int redPaddleheight = 100;
    int bluePaddlewidth = 20;
    int bluepaddleheight = 100;
    int ballxVelocity = 20;
    int ballyVelocity = 20;
    int ballxVelocity2 = 0;
    int ballyVelocity2 = 0;

    int redScore = 0;
    int blueScore = 0;
    JButton pauseButton = new JButton();
    private Set<Integer> pressedKeys = new HashSet<>();
    Random random = new Random();
    int countdown = 5;
    public boolean computerTurn = false;
    Clip hitSound;
    boolean soundEnabled = true;

    public GamePanel(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        setLayout(null);
        setPreferredSize(new Dimension(PANEEL_WIDTH, PANEEL_HEIGHT));
        setBackground(Color.BLACK);
        timer = new Timer(50, this);
        timer.start();
        addKeyListener(this);
        setFocusable(true);
        requestFocusInWindow();
        setFocusTraversalKeysEnabled(false);
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("D:\\intellij projects\\PingPong\\src\\main\\resources\\hitpong.wav").getAbsoluteFile());
            hitSound = AudioSystem.getClip();
            hitSound.open(audioInputStream);
        } catch (UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
        hitSound.setFramePosition(0);
        redPaddle = new Rectangle(985, 215, redPaddlewidth, redPaddleheight);
        bluePaddle = new Rectangle(5, 215, bluePaddlewidth, bluepaddleheight);
        ball = new Rectangle(496, 260, 20, 20);
        pauseButton.setBounds(486, 18, 40, 40);
        ImageIcon psicone = new ImageIcon("D:\\intellij projects\\PingPong\\src\\main\\resources\\pauseIcon.png");
        Image psimg = psicone.getImage();
        Image psimgScale = psimg.getScaledInstance(pauseButton.getWidth(), pauseButton.getHeight(), Image.SCALE_SMOOTH);
        psicone = new ImageIcon(psimgScale);
        pauseButton.setIcon(psicone);
        pauseButton.setOpaque(false);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        pauseButton.setFocusable(false);
        pauseButton.setVisible(true);
        pauseButton.addActionListener(this);
        add(pauseButton);
        pausPanel.setBounds(310,100,400,400);
        pausPanel.setLayout(null);
        pausPanel.setBackground(new Color(0,50,0,125));
        pausPanel.setVisible(false);
        add(pausPanel);
        resumeButton.setBounds(145,130,110,40);
        resumeButton.setBackground(Color.decode("#4ba600"));
        resumeButton.setFont(new Font("a",Font.BOLD,17));
        resumeButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        resumeButton.setFocusable(false);
        resumeButton.addActionListener(this);
        pausPanel.add(resumeButton);
        AudioButton.setBounds(145,180,110,40);
        AudioButton.setBackground(Color.decode("#4ba600"));
        AudioButton.setFont(new Font("a",Font.BOLD,17));
        AudioButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        AudioButton.setFocusable(false);
        AudioButton.addActionListener(this);
        pausPanel.add(AudioButton);
        QuitButton.setBounds(145,230,110,40);
        QuitButton.setBackground(Color.decode("#4ba600"));
        QuitButton.setFont(new Font("a",Font.BOLD,17));
        QuitButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        QuitButton.setFocusable(false);
        QuitButton.addActionListener(this);
        pausPanel.add(QuitButton);
        revalidate();
        repaint();
        Timer timer1 = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (countdown > 0){
                    countdown--;
                    repaint();
                }
                else{
                    ((Timer) e.getSource()).stop();
                    startGame();
                }
            }
        });
        timer1.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2D = (Graphics2D) g;
        g2D.setColor(Color.red);
        g2D.fillRect(redPaddle.x, redPaddle.y, redPaddle.width, redPaddle.height);
        g2D.setColor(Color.blue);
        g2D.fillRect(bluePaddle.x, bluePaddle.y, bluePaddle.width, bluePaddle.height);
        g2D.setColor(Color.GRAY);
        g2D.setStroke(new BasicStroke(5));
        g2D.drawLine(505, 0, 505, 580);
        g2D.setColor(redScoreColor);
        g2D.setFont(new Font("a", Font.BOLD, 60));
        g2D.drawString(String.valueOf(redScore), 555, 60);
        g2D.setColor(blueScoreColor);
        g2D.drawString(String.valueOf(blueScore), 425, 60);
        g2D.setColor(ballColor);
        g2D.fillOval(ball.x, ball.y, 20, 20);
        if (countdown > 0){
            g2D.setColor(Color.white);
            g2D.setFont(new Font("a", Font.BOLD, 88));
            g2D.drawString(String.valueOf(countdown), getWidth() / 2 - 20, getHeight() / 2);
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (e.getSource() == pauseButton) {
            if (!pausPanel.isVisible()) {
                pauseGame();
                pausPanel.setVisible(true);
            }
            else {
                resumeGame();
                pausPanel.setVisible(false);
            }
        }
        if (e.getSource() == resumeButton) {
            resumeGame();
            pausPanel.setVisible(false);
        }
        if (e.getSource() == AudioButton) {
            if (soundEnabled){
                AudioButton.setText("Sound off");
                soundEnabled = false;
            }
            else {
                AudioButton.setText("Sound on");
                soundEnabled = true;
            }
        }
        if (e.getSource() == QuitButton) {
            setVisible(false);
            switchToStartPanel();
        }
            if (pressedKeys.contains(KeyEvent.VK_UP)) {
                redPaddle.y -= redyVelocity;
                if (redPaddle.y <= 0) {
                    redPaddle.y = 0;
                }
            }
            if (pressedKeys.contains(KeyEvent.VK_DOWN)) {
                redPaddle.y += redyVelocity;
                if (redPaddle.y >= 471) {
                    redPaddle.y = 471;
                }
            }
            if (!computerTurn) {
                if (pressedKeys.contains(KeyEvent.VK_W)) {
                    bluePaddle.y -= blueyVelocity;
                    if (bluePaddle.y <= 0) {
                        bluePaddle.y = 0;
                    }
                }
                if (pressedKeys.contains(KeyEvent.VK_S)) {
                    bluePaddle.y += blueyVelocity;
                    if (bluePaddle.y >= 471) {
                        bluePaddle.y = 471;
                    }
            }
        }
        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        pressedKeys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        pressedKeys.remove(e.getKeyCode());
    }

    public void startGame() {
        int randomBallx = random.nextInt(2);
        int randomBally = random.nextInt(2);
        if (randomBallx == 0) {
            ballxVelocity = -20;
        }
        if (randomBallx == 1) {
            ballxVelocity = 20;
        }
        if (randomBally == 0) {
            ballyVelocity = -20;
        }
        if (randomBally == 1) {
            ballyVelocity = 20;
        }
        Timer timer1 = new Timer(50, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ball.x += ballxVelocity;
                ball.y += ballyVelocity;
                if (ball.y >= 547) {
                    ballyVelocity = -ballyVelocity;
                }
                if (ball.y <= 5) {
                    ballyVelocity = -ballyVelocity;
                }
                if (ball.x <= 5) {
                    redGoal();
                    ball.x = 496;
                    ball.y = 260;
                    ballColor = Color.white;
                }
                if (ball.x >= 985) {
                    blueGoal();
                    ball.x = 496;
                    ball.y = 260;
                    ballColor = Color.white;
                }
                if (ball.intersects(redPaddle)) {
                    ballxVelocity = -ballxVelocity;
                    ballColor = Color.RED;
                    if (soundEnabled) {
                        if (hitSound.isRunning()) {
                            hitSound.stop();
                        }
                        hitSound.setFramePosition(0);
                        hitSound.start();
                    }
                }
                if (ball.intersects(bluePaddle)) {
                    ballxVelocity = -ballxVelocity;
                    ballColor = Color.BLUE;
                    if (soundEnabled) {
                        if (hitSound.isRunning()) {
                            hitSound.stop();
                        }
                        hitSound.setFramePosition(0);
                        hitSound.start();
                    }

                }
                if (computerTurn){
                    blueyVelocity = 19;
                    if (ball.y < bluePaddle.y + bluePaddle.height / 2){
                        bluePaddle.y -= blueyVelocity;
                    } else if (ball.y > bluePaddle.y + bluePaddle.height / 2){
                        bluePaddle.y += blueyVelocity;
                    }
                }
                repaint();
            }
        });
        timer1.start();
    }

    public void pauseGame() {
        ballxVelocity2 = ballxVelocity;
        ballyVelocity2 = ballyVelocity;
        ballxVelocity = 0;
        ballyVelocity = 0;
        redyVelocity = 0;
        blueyVelocity = 0;
    }
    public void resumeGame(){
        ballxVelocity = ballxVelocity2;
        ballyVelocity = ballyVelocity2;
        redyVelocity = 18;
        blueyVelocity = 18;
    }
    public void redGoal(){
        redScoreColor = Color.green;
        pauseGame();
        Timer timer1 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redScoreColor = Color.white;
                resumeGame();
                redScore += 1;
                repaint();
            }
        });
        timer1.setRepeats(false);
        timer1.start();
    }
    public void blueGoal(){
        blueScoreColor = Color.green;
        pauseGame();
        Timer timer1 = new Timer(2000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                blueScoreColor = Color.white;
                resumeGame();
                blueScore += 1;
                repaint();
            }
        });
        timer1.setRepeats(false);
        timer1.start();
    }
    public void switchToStartPanel(){
        gameFrame.getContentPane().removeAll();
        StartPanel startPanel = new StartPanel(gameFrame);
        gameFrame.add(startPanel);
        startPanel.setVisible(true);
        gameFrame.revalidate();
        gameFrame.repaint();
    }
    public void addNotify(){
        super.addNotify();
        requestFocusInWindow();
    }
}
