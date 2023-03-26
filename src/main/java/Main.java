import org.json.simple.parser.ParseException;
import java.io.IOException;

public class Main {
    public static void main(String args[]) {
        Warehouse warehouse;
        try {
            warehouse = new Warehouse(args[0], args[1]);
            warehouse.preparePickerSchedule(Warehouse.MAXIMISE_PICKED_ORDERS_NUMBER);
        } catch(IOException e) {
            System.out.println("File not found");
            e.printStackTrace();
        } catch (ParseException | ClassCastException e) {
            System.out.println("Incorrect file structure");
            e.printStackTrace();
        }
        catch(IllegalStateException e) {
            System.out.println("At least one order and one picker must be present");
        }
    }
}
