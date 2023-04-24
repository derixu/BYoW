package byow.Core.Inputs;

public class StringInputs implements Inputs {
    private String input;
    private int index;

    public StringInputs(String s) {
        index = 0;
        input = s;
    }

    public char getNextKey() {
        char returnChar = input.charAt(index);
        index += 1;
        return returnChar;
    }

    public boolean possibleNextInput() {
        return index < input.length();
    }
}
