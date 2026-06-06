package projectBoom;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {
    	Scanner sc=new Scanner(System.in);
    	
        System.out.println("---踩地雷測試開局---");
        
        Board gameBoard = new Board();
        
        gameBoard.generateBooms();
        
        gameBoard.calculateAdjacentBooms(); 
        
        gameBoard.show();
        
        boolean isAlive = true;
        while(isAlive) {
        	System.out.print("請輸入X座標(0-8)：");
        	int x=sc.nextInt();
        	System.out.print("請輸入Y座標(0-8)：");
        	int y=sc.nextInt();
        	
        	isAlive = gameBoard.clickCell(x, y);
        	
        	System.out.println("\n---最新戰況---");
            gameBoard.show();
            
        	if (isAlive == false) {
                System.out.println("\n 碰！！！你踩到雷了！遊戲結束！");
                gameBoard.show();
        	}
        }
        sc.close();
    }
}