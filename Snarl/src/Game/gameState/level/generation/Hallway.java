package Game.gameState.level.generation;

import Game.gameState.posn.Posn;

/**
 * Class representing a hallway A hallway connects two doors together, AKA two points thus "a" to
 * "b"
 */
public class Hallway {

  Posn from;
  Posn to;
  Posn[] waypoints;

  public Hallway(Posn from, Posn to) {
    if (from.getCol() != to.getCol() && from.getRow() != to.getRow()) {
      throw new IllegalArgumentException("a and b not on same x or y plane. Need waypoint");
    }
    this.from = from;
    this.to = to;
    waypoints = new Posn[0];
  }

  public Hallway(Posn from, Posn to, Posn[] waypoints) {
    if (from.equals(to)) {
      throw new IllegalArgumentException("Must have two distinct points");
    }
    this.from = from;
    this.to = to;
    this.waypoints = waypoints;
  }

  /**
   * Get the start point of the hall way
   *
   * @return From position
   */
  public Posn getFrom() {
    return this.from;
  }

  /**
   * Get the end point of the hall way
   *
   * @return To position
   */
  public Posn getTo() {
    return this.to;
  }

  /**
   * Get the waypoints of hallways
   *
   * @return the array of way point positions
   */
  public Posn[] getWaypoints() {
    return this.waypoints;
  }

  /**
   * Get list of walkable posns in this hallway
   *
   * @return Posn[] represnting all walkable positions in this hallway
   */
  public Posn[] getHallTilePosns() {
    Posn[] result = new Posn[0];
    Posn previous = from;
    for (Posn waypoint : waypoints) {
      Posn next = waypoint;
      result = append(result, getLineSegment(previous, next));
      result = append(result, next);
      previous = waypoint;
    }
    Posn next = to;
    result = append(result, getLineSegment(previous, next));
    return result;
  }

  /**
   * Generate the Pons[] along the line the posns are in axis of DO NOT include either previous or
   * next posn in the returned list of posns
   *
   * @param previous
   * @param next
   * @return posn[] representing list of steppable positions in this hallway
   */
  public Posn[] getLineSegment(Posn previous, Posn next) {
    if (previous.getRow() == next.getRow()) {
      return generateRowLine(Math.min(previous.getCol(), next.getCol()),
          Math.max(previous.getCol(), next.getCol()), previous.getRow());
    } else if (previous.getCol() == next.getCol()) {
      return generateColLine(Math.min(previous.getRow(), next.getRow()),
          Math.max(previous.getRow(), next.getRow()), previous.getCol());
    } else {
      throw new IllegalArgumentException("Must be straight line segment");
    }
  }

  public Posn[] append(Posn[] a, Posn b) {
    Posn[] result = new Posn[a.length + 1];
    int index = 0;
    while (index < a.length) {
      result[index] = a[index];
      index++;
    }
    result[index] = b;

    return result;
  }

  public Posn[] append(Posn[] a, Posn[] b) {
    Posn[] result = new Posn[a.length + b.length];
    for (int i = 0; i < a.length; i++) {
      result[i] = a[i];
    }
    int offset = a.length;
    for (int i = 0; i < b.length; i++) {
      result[i + offset] = b[i];
    }
    return result;
  }

  private Posn[] generateColLine(int bottomRow, int topRow, int col) {
    int i = 1;
    int resultIndex = 0;
    Posn[] result = new Posn[topRow - bottomRow - 1];
    while (bottomRow + i < topRow) {
      result[resultIndex] = new Posn(bottomRow + i, col);
      i++;
      resultIndex++;
    }
    return result;
  }

  private Posn[] generateRowLine(int leftCol, int rightCol, int row) {
    int i = 1;
    int resultIndex = 0;
    Posn[] result = new Posn[rightCol - leftCol - 1];
    while (leftCol + i < rightCol) {
      result[resultIndex] = new Posn(row, leftCol + i);
      i++;
      resultIndex++;
    }
    return result;
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Hallway) {
      Hallway other = (Hallway) o;
      if (this.from.equals(other.from) && this.to.equals(other.to)
          && this.waypoints.length == other.waypoints.length) {
        for (int i = 0; i < this.waypoints.length; i++) {
          if (!(this.waypoints[i].equals(other.waypoints[i]))) {
            return false;
          }
        }
        return true;

      } else {
        return false;
      }
    } else {
      return false;
    }
  }
}