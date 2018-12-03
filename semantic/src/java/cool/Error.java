package cool;

public class Error {
	public String filename;
	public int lineNo;
	public String err;
	Error(String f, int l, String message) {
		filename = new String(f);
		lineNo = l;
		err = new String(message);
	}
}