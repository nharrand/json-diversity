package se.kth.assertteam.jsonbench;

public interface JP {
	Object parseString(String in) throws Exception;
	String print(Object in) throws Exception;
	String getName();
	boolean equivalence(Object a, Object b);
}
