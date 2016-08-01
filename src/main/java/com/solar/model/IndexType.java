package com.solar.model;

public class IndexType {

    private boolean indexed;

    private boolean stored;

    private boolean tokenized;

    public IndexType() {

    }

    public IndexType(boolean indexed, boolean stored, boolean tokenized) {
        this.indexed = indexed;
        this.stored = stored;
        this.tokenized = tokenized;
    }

    public boolean isIndexed() {
        return indexed;
    }

    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    public boolean isStored() {
        return stored;
    }

    public void setStored(boolean stored) {
        this.stored = stored;
    }

    public boolean isTokenized() {
        return tokenized;
    }

    public void setTokenized(boolean tokenized) {
        this.tokenized = tokenized;
    }

}
