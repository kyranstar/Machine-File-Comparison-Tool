package logic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MachineFile {
	private String[] lines;

	public MachineFile(String[] fileContents) {
		this.lines = Arrays.copyOf(fileContents, fileContents.length);
	}

	public List<Integer> getDifferentLines(MachineFile other) {
		List<Integer> differentLines = new ArrayList<>();
		int end = Math.min(getLineCount(), other.getLineCount());
		for (int i = 0; i < end; i++) {
			if (!other.getLine(i).equals(getLine(i))) {
				differentLines.add(i);
			}
		}
		// add unmatched lines
		for (int i = end; i < getLineCount(); i++) {
			differentLines.add(i);
		}
		for (int i = end; i < other.getLineCount(); i++) {
			differentLines.add(i);
		}

		return differentLines;
	}

	public String getLine(int lineNumber) {
		return lines[lineNumber];
	}

	public void setLine(int lineNumber, String line) {
		lines[lineNumber] = line;
	}

	public int getLineCount() {
		return lines.length;
	}

}
