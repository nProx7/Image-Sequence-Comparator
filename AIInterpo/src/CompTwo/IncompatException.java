package CompTwo;
public class IncompatException extends Exception {
	private static final long serialVersionUID = -3579939694259071315L;
	public IncompatException(String e){super("Exception: Incompatable File\n\t"+e);}
}