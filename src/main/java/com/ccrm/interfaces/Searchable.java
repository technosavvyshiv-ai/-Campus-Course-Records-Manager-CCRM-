package com.ccrm.interfaces;

import java.util.List;


public interface Searchable<T> {
    
    List<T> searchByField(String field, String value);
    List<T> searchByCriteria(SearchCriteria criteria);

    List<T> filter(Predicate<T> predicate);

    class SearchCriteria {
        private String field;
        private String value;
        private SearchOperator operator;

        public SearchCriteria(String field, String value, SearchOperator operator) {
            this.field = field;
            this.value = value;
            this.operator = operator;
        }

        public String getField() { return field; }
        public String getValue() { return value; }
        public SearchOperator getOperator() { return operator; }
    }

    enum SearchOperator {
        EQUALS, CONTAINS, STARTS_WITH, ENDS_WITH, GREATER_THAN, LESS_THAN
    }

    @FunctionalInterface
    interface Predicate<T> {
        boolean test(T item);
    }
}
