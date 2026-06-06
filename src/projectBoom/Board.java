package projectBoom;
import java.lang.Math;

public class Board{
	private Cell[][] map;
	private int rows=9;
	private int columns=9;
	
	public Board(){
		map=new Cell[rows][columns];
		for(int x=0;x<9;x++) {
			for(int y=0;y<9;y++) {
				map[x][y]=new Cell();
			}
		}
	}
	
	public Cell[][] getMap() {
	    return map;
	}
	
	public void generateBooms(){
		int boomCount=0;
		while(boomCount<10) {
			int randomX=(int)(Math.random()*9);
			int randomY=(int)(Math.random()*9);
			if(map[randomX][randomY].isBoom()==false) {
				map[randomX][randomY].setBoom(true);
				boomCount++;
			}
			
		}
	}
	
	public void calculateAdjacentBooms() {
	    for (int x = 0; x < rows; x++) {
	        for (int y = 0; y < columns; y++) {
	            if (map[x][y].isBoom() == true) {
	                continue; 
	            }

	            int count = 0;
	            for (int i = -1; i <= 1; i++) {
	                for (int j = -1; j <= 1; j++) {
	                	if (i == 0 && j == 0) {
	                        continue; 
	                    }
	                    int neighborX = x + i;
	                    int neighborY = y + j;

	                    if (neighborX >= 0 && neighborX < rows && neighborY >= 0 && neighborY < columns) {
	                        if (map[neighborX][neighborY].isBoom() == true) {
	                            count++;
	                        }
	                    }
	                    
	                }
	            }
	            // 存進這格 Cell 裡
	            map[x][y].setAdjacentBoom(count);
	        }
	    }
	}
	
	public boolean clickCell(int x, int y) {
	    //防呆超出邊界
	    if (x < 0 || x >= rows || y < 0 || y >= columns) {
	        return true; 
	    }
	    Cell target = map[x][y];
	    // 防呆翻開/插旗了跳過
	    if (target.isRevealed() == true || target.isFlagged() == true) {
	        return true;
	    }
	    
	    target.setRevealed(true);
	    if (target.isBoom() == true) {
	        return false; 
	    } 
	    
	    //連鎖擴散，鄰居是白就繼續擴散
	    if (target.getAdjacentBoom() == 0) {
	        for (int i = -1; i <= 1; i++) {
	            for (int j = -1; j <= 1; j++) {
	                if (i == 0 && j == 0) {
	                    continue;
	                }
	                //遞迴
	                clickCell(x + i, y + j);
	            }
	        }
	    }
	    return true;
	}
	
	

	public boolean checkWin() {
	    for (int x = 0; x < rows; x++) {
	        for (int y = 0; y < columns; y++) {
	            if (map[x][y].isBoom() == false && map[x][y].isRevealed() == false) {
	                return false;
	            }
	        }
	    }
	    return true; // 所有安全格子都翻開 win
	}
	
	
	
	
	
	public void show() {
	    for (int x = 0; x < rows; x++) {
	        for (int y = 0; y < columns; y++) {
	            if (map[x][y].isRevealed() == false) {
	                System.out.print("■ ");
	            } else {
	            	if (map[x][y].isBoom() == true) {
	                    System.out.print("X ");
	            	} else {
	            		System.out.print(map[x][y].getAdjacentBoom() + " ");
	            	}   
	            }
	        }
	        System.out.println(); 
	    }	
	}
	
	
}	