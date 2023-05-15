package implementation;

import interfaces.SomeInterface;

public class B implements SomeInterface {
    @Override
    public void doSomething() {
        System.out.print("B");
    }
}
