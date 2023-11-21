package hu.deniszbecskei;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Tuple<T> {
    private List<T> elements = new ArrayList<>();

    @SafeVarargs
    public Tuple(T ... elements) {
        this.elements = Arrays.asList(elements);
    }

    @MethodClarification(
            methodName = "toString",
            parameters = "void",
            returnType = "String",
            additionalInfo = "display list as Tuple"
    )
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder("(");
        for (T a: this.elements) {
            output.append(a.toString()).append(", ");
        }
        output.append(")");
        return output.toString();
    }

    public T get(int i) {
        return this.elements.get(i);
    }
}