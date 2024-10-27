import java.awt.*;
import exe.ex3.game.Game;
import exe.ex3.game.GhostCL;
import exe.ex3.game.PacManAlgo;
import exe.ex3.game.PacmanGame;

/**
 * This is the major algorithmic class for Ex3 - the PacMan game:
 *
 * This code is a very simple example (random-walk algorithm).
 * Your task is to implement (here) your PacMan algorithm.
 */
public class Ex3Algo implements PacManAlgo{
	private int _count;

	public Ex3Algo() {_count=0;}

	@Override
	/**
	 *  Add a short description for the algorithm as a String.
	 */
	public String getInfo() {
		return null;
	}

	@Override
	/**
	 * This ia the main method - that you should design, implement and test.
	 * This function determines the movement direction for the Pacman character
	 * in the Pacman game based on the current game state.
	 * It considers the
	 * position of Pacman, the positions of ghosts, the game board, and various
	 * game conditions such as ghost status and remaining time to eat ghosts.
	 *
	 */

	public int move(PacmanGame game) {
		if(_count==0 || _count==300) {
			int code = 0;
			int[][] board = game.getGame(0);
			printBoard(board);
			int blue = Game.getIntColor(Color.BLUE, code);
			int pink = Game.getIntColor(Color.PINK, code);
			int black = Game.getIntColor(Color.BLACK, code);
			int green = Game.getIntColor(Color.GREEN, code);
			System.out.println("Blue=" + blue + ", Pink=" + pink + ", Black=" + black + ", Green=" + green);
			//pacman location in string.
			String pos = game.getPos(code).toString();
			System.out.println("Pacman coordinate: "+pos);
			GhostCL[] ghosts = game.getGhosts(code);
			printGhosts(ghosts);
			int up = Game.UP, left = Game.LEFT, down = Game.DOWN, right = Game.RIGHT;
		}
		GhostCL[] ghosts = game.getGhosts(0);
		Pixel2D pacman = stringToIndex(game.getPos(0).toString());
		Map map = new Map(game.getGame(0));
		Pixel2D neerghost= stringToIndex(ghosts[indexghost(game,pacman)].getPos(0));
		int minDist = map.shortestPath(pacman,neerghost,1).length; //the distance between the nearest ghost
		if(ghosts[indexghost(game,pacman)].getStatus()==0){ //If the ghosts are inactive
			minDist =4 ;
		}
		int dir;
		System.out.println(minDist);
		if(ghosts[0].remainTimeAsEatable(0)>0 /*&& ghosts[indexghost(game,pacman)].getStatus()!=0*/ ) {//the time we have to eat the ghost
			if (pacman.equals(new Index2D(11,14))){//the ghosts exit point
				return Game.DOWN;
			}
				return eatghosts(map,pacman);

		} else if(minDist >=4){
			dir = eatP(game);
			System.out.println("eat");
		}
		else {
			System.out.println("run");
			dir = run(map,pacman,neerghost);
		}
		_count++;
		return dir;
	}

	/**
	 * The next function is a ghost escape function.
	 * This function uses shortestpath to find the shortest path to which the pacman should escape.
	 * If the ghost is close, the Pacman will run to one of the edges defined at the beginning of the function,
	 * these are the corners of the map where the distance from the ghost is greater.
	 * The function has two states, if it is circular and if it is not.
	 * @param map
	 * @param pacman
	 * @param ghost
	 * @return
	 */
	public static int run(Map map,Pixel2D pacman, Pixel2D ghost){
		//Pixel2D firstpoint= map.point(ghost,1);//the closest ghost point
		Pixel2D LD = new Index2D(1,1);// index of 4 corners
		Pixel2D LU = new Index2D(1,20);//left up
		Pixel2D RD = new Index2D(20,1);//right down
		Pixel2D RU = new Index2D(20,20);//right up
		Pixel2D [] arr = {LD,LU,RD,RU};
		int max=0;
		int index=0;
		for (int i = 0; i < arr.length ; i++) {
			Pixel2D [] road = map.shortestPath(arr[i],ghost,1);
			if(road.length>max){//finding the farthest corner
				max=road.length;
				index = i;
			}
		}
		Pixel2D move = map.shortestPath(arr[index],pacman,1)[1];//the next step to the farthest corner
		if (!map.isCyclic()){
			if(move.getX()==pacman.getX()){
				if (move.getY()<pacman.getY()){
					return Game.DOWN;
				} else return Game.UP;
			} else  {
				if (move.getX()<pacman.getX()){
					return Game.LEFT;
				} else return Game.RIGHT;
			}
		}
		else {
			if(move.getX()==pacman.getX()){
				if (move.getY()<pacman.getY()){
					if(move.getY()==0 && pacman.getY()==map.getHeight()-1){
						return Game.UP;
					}
					return Game.DOWN;
				} else {
					if(move.getY()==map.getHeight()-1 &&pacman.getY()==0){
						return Game.DOWN;
					}
					return Game.UP;
				}
			} else  {
				if (move.getX()<pacman.getX()){
					if(move.getX()==0 && pacman.getX()==map.getWidth()-1){
						return Game.RIGHT;
					}
					return Game.LEFT;
				} else {
					if(move.getX()==map.getWidth()-1&&pacman.getX()==0){
						return Game.LEFT;
					}
					return Game.RIGHT;
				}
			}
		}
	}

	/**
	 * This function determines the movement direction for Pacman when it needs to eat ghosts.
	 * It calculates the shortest path from a designated starting point (ghosts exit point)
	 * to Pacman using the given map.
	 * It then determines the next step Pacman should take based on the shortest path.
	 * @param map
	 * @param pacman
	 * @return
	 */
	public static int eatghosts(Map map,Pixel2D pacman){
		Pixel2D start = new Index2D(11,14);//the ghosts exit point
		Pixel2D [] road = map.shortestPath(start,pacman,1);//the shortest way from the point to Pacman
		Pixel2D move = road[1]; //the next step
		if (!map.isCyclic()) {
			if (move.getX() == pacman.getX()) { //where to go
				if (move.getY() < pacman.getY()) {
					return Game.DOWN;
				} else return Game.UP;
			} else {
				if (move.getX() < pacman.getX()) {
					return Game.LEFT;
				} else return Game.RIGHT;
			}
		}
		else {// if cyclic
			if(move.getX()==pacman.getX()){
				if (move.getY()<pacman.getY()){
					if(move.getY()==0 && pacman.getY()==map.getHeight()-1){
						return Game.UP;
					}
					return Game.DOWN;
				} else {
					if(move.getY()==map.getHeight()-1 &&pacman.getY()==0){
						return Game.DOWN;
					}
					return Game.UP;
				}
			} else  {
				if (move.getX()<pacman.getX()){
					if(move.getX()==0&&pacman.getX()==map.getWidth()-1){
						return Game.RIGHT;
					}
					return Game.LEFT;
				} else {
					if(move.getX()==map.getWidth()-1&&pacman.getX()==0){
						return Game.LEFT;
					}
					return Game.RIGHT;
				}
			}
		}

	}

	/**
	 * This function converts a string representation of a coordinate into a Pixel2D object.
	 * It takes a string in the format "x,y" and splits it to extract the x and y coordinates.
	 * @param p
	 * @return
	 */
	public static Pixel2D stringToIndex(String p){
		String []pos = p.split(","); //"13,4" --> [0]=13,[1]=4/
		int x =Integer.parseInt(pos[0]);
		int y =Integer.parseInt(pos[1]);
		Pixel2D point = new Index2D(x,y);
		return point;
	}
	/**
	 * This function determines the index of the closest ghost to the Pacman based on their positions.
	 * It iterates over all the ghosts in the game, calculates the shortest path from the Pacman
	 * to each ghost using the map's shortestPath() method, and keeps track of the ghost with the
	 * minimum path length. Finally, it returns the index of the closest ghost.
	 * @param game
	 * @return
	 */
	public static int indexghost(PacmanGame game,Pixel2D pacman){
		GhostCL[] ghosts = game.getGhosts(0);
		Map map = new Map(game.getGame(0));
		int index =0;
		int min =Integer.MAX_VALUE;
		for (int i = 0; i < ghosts.length; i++) {
			String ghostPos1 = ghosts[i].getPos(0);
			Pixel2D ghost = stringToIndex(ghostPos1);//return (x,y)
			Pixel2D [] shortestPathroad = map.shortestPath(pacman,ghost,1);//the shortest way from the pacman to the ghost
			if(min > shortestPathroad.length) {///    1 [6]  2[5]   .. 5[8]
				min = shortestPathroad.length;
				index = i;
			}
		}
		return  index;// return the closest ghost
	}

	/**
	 * This function determines the next move for the Pacman to eat the closest food on the map.
	 * It uses the map's point() method to find the closest food to the Pacman, and then calculates
	 * the shortest path from the food to the Pacman using the map's shortestPath() method.
	 * The function returns the direction in which the Pacman should move to reach the food.
	 * @param game
	 * @return
	 */
	public static int eatP(PacmanGame game){
		Map map = new Map(game.getGame(0));
//		String pos1= game.getPos(0).toString();//"13,4"
//		String []pos = pos1.split(","); //"13,4" --> [0]=13,[1]=4/
//		int x =Integer.parseInt(pos[0]);
//		int y =Integer.parseInt(pos[1]);
//		Pixel2D pacman= new Index2D(x,y);//המיקום של הפקמן
		Pixel2D pacman = stringToIndex(game.getPos(0).toString());// the index of the pacman
		Pixel2D firstpoint= map.point(pacman,1);//the closest food
		Pixel2D [] road = map.shortestPath(firstpoint,pacman,1);// the shortestpath from the pacman to the food
		Pixel2D move=road[1];
		if (!game.isCyclic())
			if(move.getX()==pacman.getX()){ //where to go
				if (move.getY()<pacman.getY()){
					return Game.DOWN;
				} else return Game.UP;
			} else  {
				if (move.getX()<pacman.getX()){
					return Game.LEFT;
				} else return Game.RIGHT;
			}
		else { // if is cyclic
			if(move.getX()==pacman.getX()){
				if (move.getY()<pacman.getY()){
					if(move.getY()==0 && pacman.getY()==map.getHeight()-1){
						return Game.UP;
					}
					return Game.DOWN;
				} else {
					if(move.getY()==map.getHeight()-1 &&pacman.getY()==0){
						return Game.DOWN;
					}
					return Game.UP;
				}
			} else  {
				if (move.getX()<pacman.getX()){
					if(move.getX()==0&&pacman.getX()==map.getWidth()-1){
						return Game.RIGHT;
					}
					return Game.LEFT;
				} else {
					if(move.getX()==map.getWidth()-1&&pacman.getX()==0){
						return Game.LEFT;
					}
					return Game.RIGHT;
				}
			}
		}
	}

	/**
	 * This function prints the contents of a 2D board represented by a 2D array.
	 * It iterates over the rows and columns of the array and prints the value of each element.
	 * @param b
	 */
	private static void printBoard(int[][] b) {
		for(int y =0;y<b[0].length;y++){
			for(int x =0;x<b.length;x++){
				int v = b[x][y];
				System.out.print(v+"\t");
			}
			System.out.println();
		}
	}

	/**
	 * This function prints the information of each ghost in the provided array.
	 * It iterates over the array of GhostCL objects and prints the status,
	 * type, position, and remaining time as eatable for each ghost.
	 * @param gs
	 */
	private static void printGhosts(GhostCL[] gs) {
		for(int i=0; i<gs.length;i++){
			GhostCL g = gs[i];
			System.out.println(i+") status: "+g.getStatus()+",  type: "+g.getType()+",  pos: "+g.getPos(0)+",  time: "+g.remainTimeAsEatable(0));
		}
	}

	private static int randomDir() {
		int[] dirs = {Game.UP, Game.LEFT, Game.DOWN, Game.RIGHT};
		int ind = (int)(Math.random()*dirs.length);
		return dirs[ind];
	}
}