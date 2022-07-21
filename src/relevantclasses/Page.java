package relevantclasses;

import java.util.LinkedList;

public class Page {
	public String link;
	public LinkedList<Page> arriving = new LinkedList<>();
	public LinkedList<Page> leaving = new LinkedList<>();

	public Page(String link) {
		this.link = link;
	}

	@Override
	public String toString() {
		return link;
	}

}
