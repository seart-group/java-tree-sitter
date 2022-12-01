package ai.serenade.treesitter;

public class TreeCursorNode {
  private final String type;
  private final String name;
  private final int startByte;
  private final int endByte;

  public TreeCursorNode(String type, String name, int startByte, int endByte) {
    this.type = type;
    this.name = name;
    this.startByte = startByte;
    this.endByte = endByte;
  }

  public String getType() {
    return type;
  }

  public String getName() {
    return name;
  }

  public int getStartByte() {
    return startByte;
  }

  public int getEndByte() {
    return endByte;
  }
}
