package Game.gameManager.Updates;


/**
 * Represents a notice of an ended level
 */
public class NotifyEndLevel {
    public String keyHolderName;
    public String[] exitedNames;
    public String[] ejectedNames;

    public NotifyEndLevel(String keyHolderName, String[] exitedNames, String[] ejectedNames) {
        this.keyHolderName = keyHolderName;
        this.exitedNames = exitedNames;
        this.ejectedNames = ejectedNames;
    }

    public String removeFinalDelim(String s) {
        if (s.length() > 2) {
            if (s.substring(s.length() - 2).equals(", ")) {
                return s.substring(0, s.length() - 2);
            }
        }
        return s;
    }

    public String arrayToString(String[] array) {
        String result = "[";
        for (String s : array) {
            result += s;
            result += ", ";
        }
        result = removeFinalDelim(result);
        result += "]";
        return result;
    }

    @Override
    public String toString() {
        String result = "";
        result += "keyholder: " + keyHolderName + "\n";
        result += "Exited: " + arrayToString(exitedNames) + "\n";
        result += "Ejected: " + arrayToString(ejectedNames) + "\n";
        return result;
    }
}
