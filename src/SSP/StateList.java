package SSP;

import java.util.Iterator;
import java.util.LinkedList;

public class StateList {
	private LinkedList<State> list;
	
	/* recv:
	 * 	contains: check if rcvdStates contains the old/num
	 * 	insert: insert the state to the sorted rcvdStates
	 * 	applyDiff: find the last state in the rcvdStates, find the referenced state(old state), apply
	 */
	public boolean contains(long num){
		for(State i : list){
			if(i.num == num) return true;
		}
		return false;
	}
	public void insert(State s){ //sorted
		if(list.isEmpty() || s.num > list.getLast().num){  //if empty or the num is the the largest
			list.add(s);
			return;
		}
		for(int i = 0; i < list.size(); i++){
			if(s.num < list.get(i).num){
				list.add(i, s);
			}
		}
	}
	public String applyDiff(){
		State last = list.getLast();
		for(State ref : list){ //find the ref state
			if(ref.num == last.old){
				String newText = last.diffFrom(ref.text);
				last.setText(newText);
				return newText;
			}
		}
		return null; //exception
	}
	
	/* send: 
	 * 	compare diff from the last and the first state in the list,
	 * 	return true if it has diff from the last state in the list.
	 */
	public boolean append(String current, long ack, boolean forced_resend){
		State last = list.getLast();
		Diff proposed_diff = Diff.find(last.text, current);
		if(proposed_diff.isEmpty()) return false;
		
		State first = list.getFirst();
		Diff resend_diff = Diff.find(first.text, current);
		
		Diff diff = proposed_diff;
		long ref_num = last.num;
		
		if(forced_resend || resend_diff.isEmpty() || resend_diff.size() < proposed_diff.size()){
			diff = resend_diff;
			ref_num = first.num;
			//is resend
			LinkedList<State> newList = new LinkedList<State>();
			newList.add(list.getFirst());
			list = newList;
		}
		
		list.add(new State(diff, current, ref_num, ack, first.num));
		
		return true;
	}
	public void initClient(String text){
		State exist = list.getFirst();
		exist.text = text;
	}
//	public boolean isResend(){
//		for(State i : list){
//			if(i.num - i.old > 1)
//				return true;
//		}
//		return false;
//	}
	
	public State getLast(){
		return list.getLast();
	}
	public State getFirst(){
		return list.getFirst();
	}
	public LinkedList<State> getAll(){
		return list;
	}
	
	StateList(){
		State.lastNum = 0; //reconnect
		list = new LinkedList<State>();
	}
	
	
	public void process_acknowledgment_through(long ack){
		for(State i : list){
			if(i.num == ack){ //find the state need to ack
				Iterator<State> it = list.iterator();
				while(it.hasNext()){
				    State x = it.next();
				    if(x.num < ack){
				        it.remove();
				    }
				}
				return;
			}
		}
	}
	public void process_throwaway_until(long throwaway){
		Iterator<State> it = list.iterator();
		while(it.hasNext()){
		    State x = it.next();
		    if(x.num < throwaway){
		        it.remove();
		    }
		}
	}
	
	public String toString(){
		return list.toString();
	}
}

class State {
	static long lastNum = 0;
	
	long timestamp;
	
	long old;
	long num;
	long ack;
	long throwaway;
	Diff diff = new Diff(); //diff from last state
	String text;
	
	State(String t){ //init 
		num = ++lastNum;
		timestamp = System.currentTimeMillis();
		
		old = 0;
		ack = 0;
		throwaway = 0;
		diff.appendix = t;
		diff.index = 0 ; //= new Diff(t, 0);
		text = t;
	}
	State(Diff d, String current, long o, long a, long t){ //send the state
		num = ++lastNum;
		timestamp = System.currentTimeMillis();
		
		old = o;
		diff = d;
		ack = a;
		throwaway = t;
		text = current;
	}
	State(Diff d, long o, long a, long t, long n){ //recv some state
		num = n;
		timestamp = System.currentTimeMillis();
		
		old = o;
		diff = d;
		ack = a;
		throwaway = t;
	}
	
	public String diffFrom(String text){
		if(diff.isEmpty()) return text;
		if(text.length()<diff.index) {
			System.out.println("error:"+ diff + ":" + text);
			return text;
		}
		return text.substring(0, diff.index) + diff.appendix;
	}
	
	public String toString(){
		return old + "->" + num + ":" + diff;//+ "/" + timestamp + "/" + diff;//+ diff.index + ":" + diff.appendix;
//		return text + "/" + diff;
	}
	public void setText(String t) {
		text = t;
		
	}
}

class Diff {
	String appendix = "";
	int index;
	static Diff find(String last, String current){
		char[] l = last.toCharArray();
		char[] c = current.toCharArray();
		String a;
		int i;
		for(i = 0; i < c.length && i < l.length; i++)
			if(l[i] != c[i])
				break;
		a = current.substring(i);
		if(last.length() == current.length() && a.length() == 0)
			return new Diff();
		return new Diff(a, i);
	}
	Diff(String a, int i){
		appendix = a;
		index = i;
	}
	Diff(){ //init
		appendix = "";
		index = -1;
	}
	
	public boolean isEmpty(){
		if(index == -1) return true;
		else return false;
	}
	public int size(){
		return appendix.length();
	}
	
	public String toString(){
		return index + ":" + appendix;
	}
}