import java.util.*;

public class LogicalExpression {
    private final String symbol;
    private final String connective;
    private final List<LogicalExpression> children;

    private LogicalExpression(String symbol) {
        this.symbol = symbol;
        this.connective = null;
        this.children = Collections.emptyList();
    }

    private LogicalExpression(String connective, List<LogicalExpression> children) {
        this.symbol = null;
        this.connective = connective;
        this.children = children;
    }

    public static LogicalExpression Symbol(String symbol) {
        return new LogicalExpression(symbol);
    }

    public static LogicalExpression And(List<LogicalExpression> children) {
        if (children.size() < 2) throw new IllegalArgumentException("Must be at least 2 operands");
        return new LogicalExpression("and", children);
    }

    public static LogicalExpression Or(List<LogicalExpression> children) {
        if (children.size() < 2) throw new IllegalArgumentException("Must be at least 2 operands");
        return new LogicalExpression("or", children);
    }

    public static LogicalExpression If(LogicalExpression left, LogicalExpression right) {
        return new LogicalExpression("if", Arrays.asList(left, right));
    }

    public static LogicalExpression Iff(LogicalExpression left, LogicalExpression right) {
        return new LogicalExpression("iff", Arrays.asList(left, right));
    }

    public static LogicalExpression Not(LogicalExpression child) {
        return new LogicalExpression("not", Collections.singletonList(child));
    }

    public void addChildren(LogicalExpression child) {
        children.add(child);
    }

    public List<String> extractSymbols() {
        List<String> res = new ArrayList<>();
        if (symbol != null) {
            res.add(symbol);
        } else {
            for (var child : children) {
                res.addAll(child.extractSymbols());
            }
        }
        return res;
    }

    public boolean plTrue(HashMap<String, Boolean> model) {
        if (symbol != null)
            return model.getOrDefault(symbol, false);

        assert connective != null;
        if (connective.equals("and")) {
            for (var child : children) {
                if (!child.plTrue(model)) return false;
            }
            return true;
        }

        if (connective.equals("or")) {
            for (var child : children) {
                if (child.plTrue(model)) return true;
            }
            return false;
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

    public static boolean ttEntails(LogicalExpression KB, LogicalExpression alpha) {
        var symbols = KB.extractSymbols();
        symbols.addAll(alpha.extractSymbols());
        return ttCheckAll(KB, alpha, symbols, new HashMap<>());
    }

    public static boolean ttCheckAll(LogicalExpression KB,
                                     LogicalExpression alpha,
                                     List<String> symbols,
                                     HashMap<String, Boolean> model) {
        if (symbols.isEmpty())
            return !KB.plTrue(model) || alpha.plTrue(model);
        else {
            var rest = new ArrayList<>(symbols);
            var symbol = rest.remove(0);
            return ttCheckAll(KB, alpha, rest, extend(symbol, true, model)) &&
                    ttCheckAll(KB, alpha, rest, extend(symbol, false, model));
        }
    }

    private static HashMap<String, Boolean> extend(String symbol, boolean value, HashMap<String, Boolean> model) {
        var res = new HashMap<>(model);
        model.put(symbol, value);
        return res;
    }

    public static void main(String[] args) {
        LogicalExpression KB = And(Arrays.asList(
                Iff(Symbol("B_1_1"), Or(Arrays.asList(Symbol("P_1_2"), Symbol("P_2_1")))),
                Symbol("B_1_1")));
        LogicalExpression alpha = (Symbol("B_1_1"));
        System.out.println(ttEntails(KB, alpha));
    }
}
