import java.io.FileReader;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import lombok.Getter;
import lombok.Setter;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.*;

@Getter
@Setter
public class Warehouse {
    private List<Order> orders;
    private List<Picker> pickers;
    private LocalTime pickingStartTime;
    private LocalTime pickingEndTime;
    private Random rnd;

    // Maximise value by default
    private int chosenStrategy = 1;
    public static final int MAXIMISE_PICKED_ORDERS_NUMBER = 0;
    public static final int MAXIMISE_VALUE = 1;
    public static final int NUMBER_OF_GENERATED_MUTATIONS = 8000;
    public static final int NUMBER_OF_PERFORMED_MUTATIONS_PER_SOLUTION = 3;



    public Warehouse(String pathStore, String pathOrders) throws ParseException, IOException, ClassCastException {
        JSONParser parser = new JSONParser();
        orders = new ArrayList<>();
        pickers = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        //Read orders
        JSONArray jsonOrders = (JSONArray) parser.parse(new FileReader(pathOrders));
        for(int i = 0; i < jsonOrders.size(); i++) {
            JSONObject jsonOrder = (JSONObject) jsonOrders.get(i);
            Order orderToAdd = new Order();

            orderToAdd.setOrderId((String) jsonOrder.get("orderId"));
            orderToAdd.setOrderValue(Double.parseDouble((String) jsonOrder.get("orderValue")));
            orderToAdd.setPickingTime(Duration.parse((String) jsonOrder.get("pickingTime")));
            orderToAdd.setCompleteBy(LocalTime.parse(((String) jsonOrder.get("completeBy")), formatter));
            orderToAdd.setFinalTime(orderToAdd.getCompleteBy().minusMinutes(orderToAdd.getPickingTime().toMinutes()));

            orders.add(orderToAdd);
        }

        //Read pickers
        JSONObject jsonStore = (JSONObject) parser.parse(new FileReader(pathStore));
        JSONArray jsonPickers = (JSONArray) jsonStore.get("pickers");
        pickingStartTime = LocalTime.parse((String) jsonStore.get("pickingStartTime"), formatter);
        pickingEndTime = LocalTime.parse(((String) jsonStore.get("pickingEndTime")), formatter);

        for(int i = 0; i < jsonPickers.size(); i++) {
            Picker pickerToAdd = new Picker();
            pickerToAdd.setPickerId((String) jsonPickers.get(i));
            pickerToAdd.setAvailabilityTime(pickingStartTime);

            pickers.add(pickerToAdd);
        }
    }

    public void preparePickerSchedule(int strategyToUse) throws IllegalStateException {
        // Check if there are present orders and pickers
        if(orders.size() < 1 | pickers.size() < 1) {
            throw new IllegalStateException();
        }
        chosenStrategy = strategyToUse;
        rnd = new Random();
        Collections.sort(orders, Comparator.comparing(Order::getFinalTime));
        List<Assignment> result = getBestSolution();
        for(Assignment assignment: result) {
            System.out.println(assignment.getPicker().getPickerId() + " " + assignment.getOrder().getOrderId() + " " + assignment.getPicker().getAvailabilityTime());
            assignment.getPicker().increaseAvailabilityTime(assignment.getOrder().getPickingTime());
        }
        System.out.println();
    }

    private List<Assignment> getBestSolution() {
        List<Assignment> bestAssignedSolution = new ArrayList<>();
        double bestValue = 0;
        List<List<Order>> generatedSolutions = new ArrayList<>();
        // Add sorted orders as a base solution
        generatedSolutions.add(orders);
        for(int i = 0; i < NUMBER_OF_GENERATED_MUTATIONS; i++) {
            generatedSolutions.add(performMutation(generatedSolutions.get(rnd.nextInt(generatedSolutions.size()))));
        }

        for(List<Order> currentSolution: generatedSolutions) {
            List<Assignment> assignments = new ArrayList<>();
            List<Picker> currentPickers = new ArrayList<>();
            double currentValue = 0;
            for(Picker picker: pickers) {
                currentPickers.add(picker);
            }

            for(int i = 0; i < currentSolution.size(); i++) {
                Picker currentPicker = findPickerWithLowestAvailabilityTime(currentPickers);
                // Assign picker if possible
                if(currentSolution.get(i).getFinalTime().compareTo(currentPicker.getAvailabilityTime()) >= 0
                        & currentPicker.getAvailabilityTime().plusMinutes(currentSolution.get(i).getPickingTime().toMinutes()).compareTo(pickingEndTime) <= 0) {
                    assignments.add(new Assignment(currentSolution.get(i), currentPicker));
                    currentPicker.increaseAvailabilityTime(currentSolution.get(i).getPickingTime());
                    currentValue += currentSolution.get(i).getOrderValue();
                }
            }
            // Choose best solution based on the strategy
            if(chosenStrategy == MAXIMISE_VALUE) {
                if(bestValue >= currentValue) {
                    if(assignments.size() > bestAssignedSolution.size()) {
                        bestValue = currentValue;
                        bestAssignedSolution = assignments;
                    }
                }
            } else {
                if(assignments.size() > bestAssignedSolution.size()) {
                    bestAssignedSolution = assignments;
                }
            }
        }
        for(Assignment assignment: bestAssignedSolution) {
            assignment.getPicker().setAvailabilityTime(pickingStartTime);
        }
        return bestAssignedSolution;
    }

    private List<Order> performMutation(List<Order> solutionToMutate) {
        List<Order> result = new ArrayList<>();
        for(Order order: solutionToMutate) {
            result.add(order);
        }
        for(int i = 0; i < rnd.nextInt(NUMBER_OF_PERFORMED_MUTATIONS_PER_SOLUTION) + 1; i++) {
            int index1 = rnd.nextInt(result.size());
            int index2 = rnd.nextInt(result.size());
            if(index1 != index2) {
                Order order2 = result.get(index2);
                result.set(index2, result.get(index1));
                result.set(index1, order2);
            }
        }
        return result;
    }

    private Picker findPickerWithLowestAvailabilityTime(List<Picker> pickers) {
        Picker result = pickers.get(0);
        for(int i = 1; i < pickers.size(); i++) {
            if(result.getAvailabilityTime().compareTo(pickers.get(i).getAvailabilityTime()) > 0) {
                result = pickers.get(i);
            }
        }
        return result;
    }
}
