import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

public class LogicalExpression {
    private String symbol;
    private String connective;
    private ArrayList<LogicalExpression> children;

    public LogicalExpression(String symbol) {
        this.symbol = symbol;
        this.connective = null;
        this.children = new ArrayList<>();
    }

    private LogicalExpression(String connective, ArrayList<LogicalExpression> children) {
        this.symbol = null;
        this.connective = connective;
        this.children = children;
    }

    public static LogicalExpression And(ArrayList<LogicalExpression> children) {
        if (children.size() < 2) throw new IllegalArgumentException("Must be at least 2 operands");
        return new LogicalExpression("and", children);
    }

    public static LogicalExpression Or(ArrayList<LogicalExpression> children) {
        if (children.size() < 2) throw new IllegalArgumentException("Must be at least 2 operands");
        return new LogicalExpression("or", children);
    }

    public static LogicalExpression If(LogicalExpression left, LogicalExpression right) {
        var res = new ArrayList<LogicalExpression>(2);
        res.addAll(Arrays.asList(left, right));
        return new LogicalExpression("if", res);
    }

    public static LogicalExpression Iff(LogicalExpression left, LogicalExpression right) {
        var res = new ArrayList<LogicalExpression>(2);
        res.addAll(Arrays.asList(left, right));
        return new LogicalExpression("iff", res);
    }

    public static LogicalExpression Not(LogicalExpression child) {
        var res = new ArrayList<LogicalExpression>(1);
        res.addAll(Collections.singletonList(child));
        return new LogicalExpression("not", res);
    }

    public void addChildren(LogicalExpression child) {
        children.add(child);
    }

    public ArrayList<String> extractSymbols() {
        ArrayList<String> res = new ArrayList<>();
        if (symbol != null) {
            res.add(symbol);
        } else {
            for (var child : children) {
                res.addAll(child.extractSymbols());
            }
        }
        return res;
    }

    public boolean plTrue(Map<String, Boolean> model) {
        if (symbol != null)
            return model.getOrDefault(symbol, false);

        if (connective.equals("and")) {
            return children.stream().anyMatch(c -> !c.plTrue(model));
        }

        if (connective.equals("or")) {
            return children.stream().anyMatch(c -> c.plTrue(model));
        }

        if (connective.equals("if")) {
            var left = children.get(0);
            var right = children.get(1);
            return !left.plTrue(model) || right.plTrue(model);
        }

        if (connective.equals("iff")) {
            var left = children.get(0);
            var right = children.get(1);
            return left.plTrue(model) == right.plTrue(model);
        }

        if (connective.equals("not")) {
            var child = children.get(0);
            return !child.plTrue(model);
        }

        return false;
    }
}
