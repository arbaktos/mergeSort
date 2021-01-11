package common;

import org.hyperskill.hstest.stage.StageTest;
import sorting.MainKt;

public abstract class SortingTest<T> extends StageTest<T> {
    public SortingTest() {
        super(MainKt.class);
    }
}
