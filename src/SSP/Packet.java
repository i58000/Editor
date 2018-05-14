package SSP;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.google.protobuf.InvalidProtocolBufferException;

enum Type {
	NOP, NEW, INI, END
}

public class Packet{
	/**
	 * 
	 */
	
	Type TYPE;

	//payload
	long num;
	long old;
	long ack;
	long throwaway;
	Diff diff = new Diff();
	
	Packet(State s){
		TYPE = Type.NEW;
		num = s.num;
		old = s.old;
		ack = s.ack;
		throwaway = s.throwaway;
		diff = s.diff;
	}
	Packet(long a, long t){
		TYPE = Type.NOP;
		ack = a;
		throwaway = t;
	}
	Packet(String t){
		TYPE = Type.INI;
		diff.appendix = t;
		diff.index = 0;// new Diff(t, 0);
		num = 1;
		old = 0;
		ack = 0;
		throwaway = 0;
	}
	Packet(){
		TYPE = Type.END;
	}
	public State unpack(){
		return new State(diff, old, ack, throwaway, num);
	}
	
	public byte[] toByteArray(String key){
		
		Proto.packet.Builder builder = Proto.packet.newBuilder();
		builder.setTYPE(TYPE.ordinal());
		builder.setNum(num);
		builder.setAck(ack);
		builder.setOld(old);
		builder.setThrowaway(throwaway);
		builder.setDiffAppendix(diff.appendix);
		builder.setDiffIndex(diff.index);
		
        Proto.packet pp = builder.build();
        
        return ZLibUtils.compress(
        		AESUtils.encrypt(
        				pp.toByteArray(), key));
	}
	
	static public Packet parseFrom(byte[] arr, String key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidProtocolBufferException{

		Proto.packet pp = Proto.packet.parseFrom(
				AESUtils.decrypt(
						ZLibUtils.decompress(arr), key));
		Packet p = new Packet();
		p.TYPE = Type.values()[ pp.getTYPE() ];
		p.ack = pp.getAck();
		p.num = pp.getNum();
		p.old = pp.getOld();
		p.throwaway = pp.getThrowaway();
		p.diff = new Diff(
				pp.getDiffAppendix(),  
				pp.getDiffIndex()
				);
		return p;
	}
	
}
