package logic;

import java.util.Arrays;

public class TextFile {
	private final String title;
	private final String[] contents;

	public TextFile(String title, String[] contents) {
		this.title = title;
		this.contents = contents;
	}

	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @return a copy of the length of this array
	 */
	public String[] getContents() {
		if (contents == null) {
			return null;
		}
		return Arrays.copyOf(contents, contents.length);
	}
}
