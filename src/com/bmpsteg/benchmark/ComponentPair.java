package com.bmpsteg.benchmark;

/**
 * Created by orsic on 16.01.16..
 */
public class ComponentPair {
    public int componentsToUse;
    public int bitsPerComponent;

    public ComponentPair(int componentsToUse, int bitsPerComponent) {
        this.componentsToUse = componentsToUse;
        this.bitsPerComponent = bitsPerComponent;
    }

    @Override
    public String toString() {
        return componentsToUse + "_components-" + bitsPerComponent + "_bits";
    }
}
