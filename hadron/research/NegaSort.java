package hadron.research;

import java.util.ArrayList;

import hadron.board.Board;
import hadron.heuristic.Heuristic;

public class NegaSort implements Research {
	public Heuristic h;
	
	// Lista che contiene i figli per quel turno, serve per poter utilizzare il valore euristico che è
	// "risalito" dall'itarazione precedente
	static ArrayList<Node> sons;


	public Node research(Board board, int d, byte col, double alpha, double beta) {
		if(d==1) {
			sons = board.getSons(col); // col solo per test
		}
		
		Node result = null;
		double best = -100000;
		double n = beta;
		double cur;
		
		sons.sort(sons.get(0).getComparator(col));
		for(Node s:sons) {
			
			cur = -negaScoutSearch(s,d-1, (byte) (1-col), -n, -alpha);
			s.setValue(cur);
			
			if(best<cur || result==null) {
				if(n==beta || d<=2) {
					best = cur;
					result = s;
				}
				else {
					best = -negaScoutSearch(s,d-1, (byte) (1-col),-beta,-cur);
					s.setValue(best);
					result = s;
				}
			}
			
			if(best > alpha)
				alpha = best;
			
			if(alpha >= beta) {
				return result;
			}
			
			n = alpha+1;
		}

		return result;
	}//hadron.research
	
	
	/**
	 * Implementazione della ricerca NegaScout
	 * 
	 * @param currentConfiguration configurazione corrente
	 * @param d profondità
	 * @param col colore per cui voglio trovare i figli
	 * @param alpha valore di alpha
	 * @param beta valore di beta
	 * @return valore migliore
	 * 
	 */
	private double negaScoutSearch(Node currentConfiguration, int d, byte col, double alpha, double beta) {
		if(d==0 || currentConfiguration.getBoard().isFinal()) {
			currentConfiguration.setValue(h.evaluate(currentConfiguration.getBoard(), col));
			return currentConfiguration.getValue();
		}
		
		double best = -1000000D;
		double cur;
		double n = beta;
		
		ArrayList<Node> child = currentConfiguration.getBoard().getSons(col);
		
		for(Node s:child) {
			
			cur = -negaScoutSearch(s,d-1, (byte) (1-col),-n,-alpha);
			s.setValue(cur);
			
			if(cur>best) {
				if(n==beta || d<=2) {
					best = cur;
				}
				else {
					best = -negaScoutSearch(s,d-1, (byte) (1-col),-beta,-cur);
					s.setValue(best);
				}
			}
			
			if(best > alpha)
				alpha = best;
			
			if(alpha >= beta) {
				return alpha;
			}
			
			n = alpha+1;
		}

		return best;
	}

	@Override
	// TODO eliminare dopo test
	public void setHeuristic(Heuristic h) {
		this.h = h;
	}
}
