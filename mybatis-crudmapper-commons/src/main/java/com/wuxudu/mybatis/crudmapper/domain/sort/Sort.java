package com.wuxudu.mybatis.crudmapper.domain.sort;

public final class Sort {

    private final String fieldName;
    private final boolean isAsc;

    Sort(String fieldName, boolean isAsc) {
        this.fieldName = fieldName;
        this.isAsc = isAsc;
    }

    public static Builder by(String fieldName) {
        return new Builder(fieldName);
    }

    public String getFieldName() {
        return fieldName;
    }

    public boolean isAsc() {
        return isAsc;
    }

    public static class Builder {

        private final String fieldName;

        Builder(String fieldName) {
            this.fieldName = fieldName;
        }

        public Sort asc() {
            return new Sort(this.fieldName, true);
        }

        public Sort desc() {
            return new Sort(this.fieldName, false);
        }
    }
}
