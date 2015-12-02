package logic;

public class MachineFile {
	private String[] lines;

	public MachineFile(String fileContents) {

	}

	public int[] getDifferentLines(MachineFile other) {
		return null;
	}

	public String getLine(int lineNumber) {
		return lines[lineNumber];
	}

	public void setLine(int lineNumber, String line) {
		lines[lineNumber] = line;
	}
	public int getLineCount(){
		return lines.length;
	}

}
