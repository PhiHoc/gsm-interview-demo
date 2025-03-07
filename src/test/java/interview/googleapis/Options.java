package interview.googleapis;

public enum Options {
    RAW("RAW"),
    USER_ENTERED("USER_ENTERED");

    private String text;

    public String toString() {
        return String.valueOf(this.text);
    }

    Options(String text) {
        this.text = text;
    }
}
