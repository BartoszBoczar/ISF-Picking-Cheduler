This program solves the problem of assigning workers (called pickers) in a store to pick up orders. Each order takes a specific amount of time to pick. A picker can only take one order at a time. Each order must be completed before their specified time.

The program takes to arguments: store path and orders path. Store path leads to a json file with all pickers, time at which the picking begins and time at which the picking ends. The second file contains a path to the file with all the orders.

The class containing most of the functionalities is a Warehouse. This class loads all the pickers and orders from their files, stores them and performs the assignment process.

Before the assignment the final time of each order is calculated. It is the time at which orders must be picked in order to complete it before expiring.

The assignment is performed by sorting all the orders by their final time. Then the copies of the orders are being created. Each copy is a mutated version of one of the previous copies. The mutation takes place by swapping places of 2 random orders. Each copy is checked whether it's the best solution. The program is either maximising the number of orders picked or the value of orders picked (maximising value by default).

There are 2 test files. TimeUsageTests perform each individual set of written test cases and check whether each one didn't take over 20s. The WarehouseTests contain a few tests checking if the Warehouse throws correct exceptions. 