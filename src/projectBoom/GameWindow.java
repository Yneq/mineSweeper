package projectBoom;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class GameWindow extends JFrame implements Printable {

	private static final long serialVersionUID = 1L;
	private JPanel contentPane;

	private Board board = new Board();
	private JButton[][] buttons = new JButton[9][9];
	
	private boolean[][] myFlags = new boolean[9][9];
	private boolean[][] myRevealed = new boolean[9][9]; 
	
	private JLabel flagLabel; 
	private JLabel timerLabel; 
	private Timer gameTimer; 
	
	private int remainingFlags = 10; 
	private int secondsPassed = 0; 
	private boolean isGameOver = false; 
	
	private String gameStatusResult = "遊戲進行中"; 

	private ImageIcon flagIcon;
	private ImageIcon bombIcon;

	public static void main(String[] args) {
		// 🎸 遊戲視窗一打開，立刻在後台無限循環播放背景音樂！
		SoundManager.playBackgroundMusic("background.wav");
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GameWindow frame = new GameWindow();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	public GameWindow() {
		loadImages();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 520, 580); 
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 10));
		
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new GridLayout(1, 4, 5, 0)); 
		topPanel.setBackground(Color.DARK_GRAY);
		topPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		flagLabel = new JLabel(" 剩餘旗子: 10", JLabel.CENTER);
		flagLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13)); 
		flagLabel.setForeground(Color.YELLOW);
		
		timerLabel = new JLabel("時間: 0 秒 ", JLabel.CENTER);
		timerLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 13)); 
		timerLabel.setForeground(Color.CYAN);
		
		JButton btnReset = new JButton("重新開始");
		btnReset.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		

		btnReset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				resetGame();
			}
		});
		
		JButton btnPrint = new JButton("列印");
		btnPrint.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 12));
		btnPrint.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean WAS_RUNNING = gameTimer.isRunning();
				if (WAS_RUNNING) {
					gameTimer.stop();
				}

				PrinterJob job = PrinterJob.getPrinterJob();
				job.setPrintable(GameWindow.this);
				
				if (job.printDialog()) {
					try {
						job.print(); 
					} catch (PrinterException e1) {
						e1.printStackTrace();
					}
				}

				if (WAS_RUNNING == true && isGameOver == false) {
					gameTimer.start();
				}
			}
		});
		
		topPanel.add(flagLabel);
		topPanel.add(btnReset);
		topPanel.add(btnPrint); 
		topPanel.add(timerLabel);
		contentPane.add(topPanel, BorderLayout.NORTH); 
		
		JPanel gameGridPanel = new JPanel();
		gameGridPanel.setLayout(new GridLayout(9, 9, 1, 1)); 
		contentPane.add(gameGridPanel, BorderLayout.CENTER); 
		
		board.generateBooms();
		board.calculateAdjacentBooms();


		gameTimer = new Timer(1000, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				secondsPassed++;
				timerLabel.setText("時間: " + secondsPassed + " 秒 ");
			}
		});

		for (int x = 0; x < 9; x++) {
		    for (int y = 0; y < 9; y++) {
		        
		        buttons[x][y] = new JButton();
		        buttons[x][y].setOpaque(true);
		        buttons[x][y].setBorderPainted(false); 
		        buttons[x][y].setBorder(new LineBorder(Color.GRAY, 1)); 
		        buttons[x][y].setBackground(new Color(210, 210, 210)); 
		        buttons[x][y].setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18)); 
		        gameGridPanel.add(buttons[x][y]);
		        
		        final int currentX = x;
		        final int currentY = y;
		        
		        buttons[x][y].addMouseListener(new java.awt.event.MouseAdapter() {
		            @Override
		            public void mousePressed(MouseEvent e) {

		            	if (isGameOver == true) {
		            		return;
		            	}
		            	
		            	if (gameTimer.isRunning() == false && secondsPassed == 0) {
		            		gameTimer.start();
		            	}
		            	
		            	if (myRevealed[currentX][currentY] == true) {
		            		return;
		            	}


		                if (e.getButton() == MouseEvent.BUTTON3) {
		                    myFlags[currentX][currentY] = !myFlags[currentX][currentY];
		                    
			            	//這裡MVC不同步→修正 按右鍵只有view顯示，必須再傳回Board裡面Cell
		                    Cell[][] currentMap = board.getMap();
		                    Cell cell=currentMap[currentX][currentY];
		                    cell.setFlagged(myFlags[currentX][currentY]);
		                    

		                    if (myFlags[currentX][currentY] == true) {
		                        if (flagIcon != null) {
		                        	buttons[currentX][currentY].setIcon(flagIcon);
		                        	SoundManager.playSound("flag.wav");
		                        } else {
		                        	buttons[currentX][currentY].setText("F"); 
		                        	buttons[currentX][currentY].setForeground(Color.RED);
		                        }
		                        buttons[currentX][currentY].setBackground(new Color(255, 230, 200)); 
		                        remainingFlags--; 
		                    } else {
		                        buttons[currentX][currentY].setIcon(null); 
		                        buttons[currentX][currentY].setText(""); 
		                        buttons[currentX][currentY].setBackground(new Color(210, 210, 210)); 
		                        remainingFlags++; 
		                    }
		                    flagLabel.setText(" 剩餘旗子: " + remainingFlags);
		                } 
		                else if (e.getButton() == MouseEvent.BUTTON1) {
		                    if (myFlags[currentX][currentY] == false) {
		                    	SoundManager.playSound("click.wav");
		                        handleButtonClick(currentX, currentY);
		                        
		                    }
		                }
		            }
		        });
		    }
		}
	} 
	
	private void handleButtonClick(int x, int y) {
	    boolean isAlive = board.clickCell(x, y);
	    myRevealed[x][y] = true;
	    
	    if (isAlive == false) {
	    	gameTimer.stop();
	    	isGameOver = true;
	    	
	    	gameStatusResult = "挑戰失敗（花費時間：" + secondsPassed + "秒）";
	    	
	        if (bombIcon != null) {
	        	buttons[x][y].setIcon(bombIcon);
	        } else {
	        	buttons[x][y].setText("X");
	        	buttons[x][y].setForeground(Color.BLACK);
	        }
	        buttons[x][y].setBackground(Color.RED); 
	        
	        revealAllBooms();
	        SoundManager.playSound("lose.wav");
	        JOptionPane.showMessageDialog(this, "你踩到雷了！遊戲結束！\n總共撐了 " + secondsPassed + " 秒。");
	        
	    } else {
	        refreshAllButtons(); 
	        
	        if (board.checkWin() == true) {
	        	gameTimer.stop(); 
	        	isGameOver = true; 
	        	
	        	SoundManager.playSound("win.wav");
	        	gameStatusResult = "恭喜破關（花費時間：" + secondsPassed + "秒）";
	        	
	        	JOptionPane.showMessageDialog(this, "恭喜破關！！！\n花費時間: " + secondsPassed + " 秒！");
	        }
	    }
	}

	private void refreshAllButtons() {
	    for (int x = 0; x < 9; x++) {
	        for (int y = 0; y < 9; y++) {
	            Cell cell = board.getMap()[x][y];
	            if (cell.isRevealed() == true) {
	            	myRevealed[x][y] = true; 
	                buttons[x][y].setIcon(null); 
	                buttons[x][y].setBackground(Color.WHITE); 
	                
	                int boomCount = cell.getAdjacentBoom();
	                if (boomCount > 0) {
	                    buttons[x][y].setText(String.valueOf(boomCount));
	                    if (boomCount == 1) {
	                    	buttons[x][y].setForeground(Color.BLUE);
	                    } else if (boomCount == 2) {
	                    	buttons[x][y].setForeground(new Color(0, 128, 0));
	                    } else {
	                    	buttons[x][y].setForeground(Color.RED);
	                    }
	                } else {
	                    buttons[x][y].setText(""); 
	                }
	            }
	        }
	    }
	}

	private void revealAllBooms() {
		for (int x = 0; x < 9; x++) {
	        for (int y = 0; y < 9; y++) {
	        	Cell cell = board.getMap()[x][y];
	        	if (cell.isBoom() == true) {
	        		if (bombIcon != null) {
	        			buttons[x][y].setIcon(bombIcon);
	        		} else {
	        			buttons[x][y].setText("X");
	        		}
	        		if (buttons[x][y].getBackground() != Color.RED) {
	        			buttons[x][y].setBackground(new Color(255, 180, 180)); 
	        		}
	        	}
	        }
		}
	}

	private void resetGame() {
	    gameTimer.stop();
	    secondsPassed = 0;
	    remainingFlags = 10;
	    isGameOver = false;
	    gameStatusResult = "遊戲進行中";
	    
	    timerLabel.setText("時間: 0 秒 ");
	    flagLabel.setText(" 剩餘旗子: 10");
	    
	    board = new Board();
	    board.generateBooms();
	    board.calculateAdjacentBooms();
	    
	    for (int x = 0; x < 9; x++) {
	        for (int y = 0; y < 9; y++) {
	            myFlags[x][y] = false;
	            myRevealed[x][y] = false;
	            
	            buttons[x][y].setIcon(null);
	            buttons[x][y].setText("");
	            buttons[x][y].setForeground(Color.BLACK); 
	            
	            buttons[x][y].setBackground(new Color(210, 210, 210));
	            buttons[x][y].setBorderPainted(false);
	        }
	    }
	    
	    // 清除殘影/重繪畫面
	    contentPane.revalidate();
	    contentPane.repaint();
	}

	private void loadImages() {
		try {
			java.net.URL flagUrl = getClass().getResource("flag.png");
			java.net.URL bombUrl = getClass().getResource("bomb.png");
			
			ImageIcon rawFlag = null;
			ImageIcon rawBomb = null;
			
			if (flagUrl != null) {
				rawFlag = new ImageIcon(flagUrl);
			}
			if (bombUrl != null) {
				rawBomb = new ImageIcon(bombUrl);
			}
			
			if (rawFlag == null && new File("src/projectBoom/flag.png").exists()) {
				rawFlag = new ImageIcon("src/projectBoom/flag.png");
			}
			if (rawBomb == null && new File("src/projectBoom/bomb.png").exists()) {
				rawBomb = new ImageIcon("src/projectBoom/bomb.png");
			}
			
			if (rawFlag != null) {
				Image scaledFlag = rawFlag.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
				flagIcon = new ImageIcon(scaledFlag);
			}
			if (rawBomb != null) {
				Image scaledBomb = rawBomb.getImage().getScaledInstance(60, 60, Image.SCALE_SMOOTH);
				bombIcon = new ImageIcon(scaledBomb);
			}
			
		} catch (Exception e) {
			System.out.println("圖片載入與縮放發生異常。");
		}
	}

	@Override
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
		if (pageIndex > 0) {
			return Printable.NO_SUCH_PAGE;
		}
		Graphics2D g2d = (Graphics2D) graphics;
		
		g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
		
		g2d.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
		g2d.setColor(Color.BLACK);
		g2d.drawString("【踩地雷】", 30, 30);
		
		g2d.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
		String printStatus;
		
		
		if (isGameOver == true) {
			printStatus = "目前狀態：" + gameStatusResult + " | 剩餘旗子：" + remainingFlags;
		} else {
			printStatus = "Currently status：" + gameStatusResult + " | 剩餘旗子：" + remainingFlags + " | 時間：" + secondsPassed + " 秒";
		}
		g2d.drawString(printStatus, 30, 55);
		
		String oldFlagText = flagLabel.getText();
		String oldTimerText = timerLabel.getText();
		flagLabel.setText("");
		timerLabel.setText("");
		
		g2d.translate(0, 80);
		contentPane.printAll(g2d);
		
		flagLabel.setText(oldFlagText);
		timerLabel.setText(oldTimerText);
		
		return Printable.PAGE_EXISTS;
	}
}