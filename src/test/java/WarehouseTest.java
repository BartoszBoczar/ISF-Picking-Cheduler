import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WarehouseTest {
    @Test
    public void shouldThrowIOException() {
        IOException exception = assertThrows(IOException.class,
                ()->{new Warehouse("", "");} );
    }

    @Test
    public void shouldThrowClassCastException() {
        ClassCastException exception = assertThrows(ClassCastException.class,
                ()->{new Warehouse("D:\\zadanie-java\\empty-orders.json", "D:\\zadanie-java\\empty-store.json");} );
    }

}
