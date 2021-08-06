package jarekjal;

public class Equation {

    private String equation;
    private String rightSide;
    private String leftSide;

    public Equation(String equation) {
        if (isValidEquation(equation)) {
            this.equation = equation;
            String[] equationSides = equation.split("=");
            this.leftSide = equationSides[0];
            this.rightSide = equationSides[1];
        } else {
            throw new IllegalArgumentException();
        }
    }

    private boolean isValidEquation(String equation) {
        if (!equation.contains("=")) return false;
        var equationParts = equation.split("=");
        if (equationParts.length != 2) return false;
        var name = equationParts[0];
        var value = equationParts[1];
        return areNameAndValueNotEmpty(name, value);
    }

    private boolean areNameAndValueNotEmpty(String name, String value) {
        return !name.isEmpty() && !value.isEmpty();
    }

    public String getEquation() {
        return equation;
    }

    public String getRightSide() {
        return rightSide;
    }

    public String getLeftSide() {
        return leftSide;
    }
}
