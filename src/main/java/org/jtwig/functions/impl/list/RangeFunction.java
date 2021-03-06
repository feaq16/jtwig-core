package org.jtwig.functions.impl.list;

import org.jtwig.functions.FunctionRequest;
import org.jtwig.functions.SimpleJtwigFunction;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class RangeFunction extends SimpleJtwigFunction {
    @Override
    public String name() {
        return "range";
    }

    @Override
    public Object execute(FunctionRequest request) {
        request.minimumNumberOfArguments(2)
                .maximumNumberOfArguments(3);

        int step = 1;
        if (request.getNumberOfArguments() > 2) {
            step = intValue(request.getArguments().get(2));
        }
        int min = intValue(request.getArguments().get(0));
        int max = intValue(request.getArguments().get(1));

        return new RangeIterable(min, max, step);
    }

    protected int intValue(Object param) {
        if (param instanceof BigDecimal) {
            return ((BigDecimal) param).intValue();
        } else {
            return (Integer) param;
        }
    }

    public static class RangeIterable implements Iterable<Integer> {

        private int step;

        private int from;
        private int to;

        public RangeIterable(int from, int to, int step) {
            this.from = from;
            this.to = to;
            this.step = step;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new RangeIterator(this.from, this.to, this.step);
        }
    }

    public static class RangeIterator implements Iterator<Integer> {
        private int step;
        private int max;
        private int min;

        private Integer current;

        public RangeIterator(int min, int max, int step) {
            this.step = step;
            this.min = min;
            this.max = max;

            if (step < 0) {
                current = max;
            } else {
                current = min;
            }
        }

        @Override
        public boolean hasNext() {
            if (this.step < 0) {
                return this.min < this.current;
            } else {
                return this.max > this.current;
            }
        }

        @Override
        public Integer next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No such elements");
            }
            int val = this.current;
            this.current += step;
            return val;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Operation not supported");
        }
    }
}
