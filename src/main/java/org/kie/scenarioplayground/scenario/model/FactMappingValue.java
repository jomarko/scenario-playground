package org.kie.scenarioplayground.scenario.model;

import java.util.Objects;
import java.util.function.BiFunction;

public class FactMappingValue {

    private final String factName;
    private final String bindingName;
    private final Object rawValue;
    private Operator operator = Operator.equals;

    public FactMappingValue(String factName, String bindingName, Object rawValue) {
        this.factName = factName;
        this.bindingName = bindingName;
        this.rawValue = rawValue;
    }

    public FactMappingValue(String factName, String bindingName, Object rawValue, Operator operator) {
        this(factName, bindingName, rawValue);
        this.operator = operator;
    }

    public String getFactName() {
        return factName;
    }

    public String getBindingName() {
        return bindingName;
    }

    public Object getRawValue() {
        return rawValue;
    }

    public Operator getOperator() {
        return operator;
    }

    public enum Operator {

        equals(Objects::equals),
        less_them((a, b) -> defaultComparator.apply(a, b) < 0),
        great_then((a, b) -> defaultComparator.apply(a, b) > 0);

        final BiFunction<Object, Object, Boolean> compare;

        Operator(BiFunction<Object, Object, Boolean> compare) {
            this.compare = compare;
        }

        public Boolean evaluate(Object resultValue, Object expectedValue) {
            return compare.apply(resultValue, expectedValue);
        }

    }

    private static BiFunction<Object, Object, Integer> defaultComparator = (a, b) -> {
        if (Comparable.class.isAssignableFrom(a.getClass()) && Comparable.class.isAssignableFrom(b.getClass())
                && a.getClass().equals(b.getClass())) {
            Comparable comparableA = (Comparable) a;
            return comparableA.compareTo(b);
        }
        throw new IllegalArgumentException("Object cannot be compared '" + a.getClass().getCanonicalName() + "'");
    };
}
