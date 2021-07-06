import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Trie {

	private class Node {

		private char value;
		private HashMap<Character, Node> children = new HashMap<>();
		private boolean isEndOfWord;

		private Node(char value) {
			this.value = value;
		}

		public String toString() {
			return "value:" + value;
		}

		public boolean hasChild(char ch) {
			return children.containsKey(ch);
		}

		public void addChild(char ch) {
			children.put(ch, new Node(ch));
		}

		public Node getChild(char ch) {
			return children.get(ch);
		}

		public Node[] getChildren() {
			return children.values().toArray(new Node[0]);
		}
	}

	private Node root = new Node(' ');

	public void insert(String word) {
		Node current = root;
		for (char ch : word.toCharArray()) {
			if (!current.hasChild(ch))
				current.addChild(ch);

			current = current.getChild(ch);
		}

		current.isEndOfWord = true;
	}

	public List<String> autoCompletion(String prefix) {
		List<String> words = new ArrayList<>();
		Node lastNode = findLastNodeOf(prefix);
		findWords(lastNode, prefix, words);

		return words;
	}

	private Node findLastNodeOf(String prefix) {
		if (prefix == null)
			return null;

		Node current = root;
		for (char ch : prefix.toCharArray()) {
			Node child = current.getChild(ch);
			if (child == null)
				return null;
			current = child;
		}
		return current;
	}

	private void findWords(Node root, String prefix, List<String> words) {
		if (root == null)
			return;

		if (root.isEndOfWord)
			words.add(prefix);

		for (Node child : root.getChildren())
			findWords(child, prefix + child.value, words);
	}

}
