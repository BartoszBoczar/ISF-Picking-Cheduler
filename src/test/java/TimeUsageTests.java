import org.json.simple.parser.ParseException;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;

public class TimeUsageTests {

    public static final long MAX_EXECUTION_TIME = 20000000000L;

    public static final String ADVANCED_ALLOCATION = "D:\\zadanie-java\\self-test-data\\advanced-allocation";
    public static final String ADVANCED_OPTIMIZE_ORDER_COUNT = "D:\\zadanie-java\\self-test-data\\advanced-optimize-order-count";
    public static final String ADVANCED_OPTIMIZE_ORDER_VALUE = "D:\\zadanie-java\\self-test-data\\advanced-optimize-order-value";
    public static final String ANY_ORDER_LENGTH_IS_OK = "D:\\zadanie-java\\self-test-data\\any-order-length-is-ok";
    public static final String COMPLETE_BY = "D:\\zadanie-java\\self-test-data\\complete-by";
    public static final String ISF_END_TIME = "D:\\zadanie-java\\self-test-data\\isf-end-time";
    public static final String LOGIC_BOMB = "D:\\zadanie-java\\self-test-data\\logic-bomb";
    public static final String OPTIMIZE_ORDER_COUNT = "D:\\zadanie-java\\self-test-data\\optimize-order-count";
    public static final String OPTIMIZE_ORDER_VALUE = "D:\\zadanie-java\\self-test-data\\optimize-order-value";

    @Test
    public void advancedAllocation() {
        long execturionTime = performTest(ADVANCED_ALLOCATION);
        Assert.assertTrue(execturionTime < MAX_EXECUTION_TIME);
    }

    @Test
    public void advancedOptimizeOrderCount() {
        long execturionTime = performTest(ADVANCED_OPTIMIZE_ORDER_COUNT);
    }

    @Test
    public void advancedOptimizeOrderValue() {
        long execturionTime = performTest(ADVANCED_OPTIMIZE_ORDER_VALUE);
    }

    @Test
    public void anyOrderLengthIsOk() {
        long execturionTime = performTest(ANY_ORDER_LENGTH_IS_OK);
    }

    @Test
    public void completeBy() {
        long execturionTime = performTest(COMPLETE_BY);
    }

    @Test
    public void isfEndTime() {
        long execturionTime = performTest(ISF_END_TIME);
    }

    @Test
    public void logicBomb() {
        long execturionTime = performTest(LOGIC_BOMB);
    }

    @Test
    public void optimizeOrderCount() {
        long execturionTime = performTest(OPTIMIZE_ORDER_COUNT);
    }

    @Test
    public void optimizeOrderValue() {
        long execturionTime = performTest(OPTIMIZE_ORDER_VALUE);
    }

    private long performTest(String path) {
        long startTime = System.nanoTime();

        Warehouse warehouse;
        try {
            warehouse = new Warehouse(path + "\\store.json", path + "\\orders.json");
            warehouse.preparePickerSchedule(Warehouse.MAXIMISE_VALUE);
        } catch(IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Incorrect file structure");
            e.printStackTrace();
        }

        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}
