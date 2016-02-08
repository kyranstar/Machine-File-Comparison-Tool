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
			if (!linesEqual(getLine(i), other.getLine(i))) {
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

	/**
	 * Checks whether two lines are "equal." It must account for slightly
	 * different spacing.
	 * 
	 * @param line
	 * @param line2
	 * @return
	 */
	private boolean linesEqual(String line, String line2) {
		String[] first = line.split("\\s+");
		String[] second = line2.split("\\s+");
		if (first.length != second.length) {
			return false;
		}
		for(int i = 0; i < first.length; i++){
			if(!first[i].trim().equals(second[i].trim())){
				return false;
			}
		}
		
		return true;

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
