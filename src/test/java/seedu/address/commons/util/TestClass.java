package seedu.address.commons.util;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Test class that contains random Json objects.
 */

public class TestClass implements Serializable {

    private static final int DEFAULT_FIRST_ELEMENT = 123;
    private static final String DEFAULT_SECOND_ELEMENT = "two";

    private int firstElement;
    private String secondElement;

    public TestClass() {
            this.firstElement = DEFAULT_FIRST_ELEMENT;
            this.secondElement = DEFAULT_SECOND_ELEMENT;
            }

    public TestClass(int firstElement, String secondElement) {
            this.firstElement = firstElement;
            this.secondElement = secondElement;
             }

    public int getFirstElement() {
        return firstElement;
    }

    public String getSecondElement() {
        return secondElement;
    }
    public void setFirstElement(int firstElement) {
        this.firstElement = firstElement;
    }
    public void setSecondElement(String secondElement){
        this.secondElement = secondElement;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof TestClass)) { // this handles null as well.
            return false;
        }

        TestClass o = (TestClass) other;

        return Objects.equals(firstElement, o.firstElement) && Objects.equals(secondElement, o.secondElement);
                }

    @Override
    public int hashCode() {
        return Objects.hash(firstElement, secondElement);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FirstElement : " + firstElement + "\n");
        sb.append("SecondElement : " + secondElement + "\n");
        return sb.toString();
    }
}

