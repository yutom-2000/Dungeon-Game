package Game.controllers;

/**
 * Class representing a single playerScoreList entry
 */
public class PlayerScore {

  public String name;
  public int score;

  /**
   * Constructor of the PlayerScore
   *
   * @param name  the name of the player
   * @param score the score got by player
   */
  public PlayerScore(String name, int score) {
    this.name = name;
    this.score = score;
  }
}
