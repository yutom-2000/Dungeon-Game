package Game.gameState.interaction;

/**
 * Represent the result of a move
 *
 *      -OK: no interaction
 *      -KEY: player collected key/unlocked the exit
 *      -EXIT: player exited the level
 *      -EJECT: player was killed
 *      -INVALID: invalid move
 */
public enum MoveResult {
    OK, KEY, EXIT, EJECT, INVALID
}
