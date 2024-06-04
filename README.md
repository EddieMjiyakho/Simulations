# OS2 Assignment README

## Description

The Bar Management System is a Java application that simulates a bar environment with patrons placing drink orders and a barman fulfilling those orders. The system employs multi-threading to handle simultaneous patron activities and asynchronous order processing by the barman.

## Usage
To use this program, follow these steps:
1. Adjust the parameters such as the number of patrons and scheduling algorithm in the Makefile (0=FCFS and 1=SJF) or Main (ScheduingSimulation.java) class as needed.

2. Run the program:
    make run

3. After execution, the program will generate an output file named `reponse_time`, `waiting_time`, `turnaround_time` containing the data for the patron for specified scheduling algorithm and will printout the throughput.

## Output Format
The output files (`reponse_time`, `waiting_time`, `turnaround_time`) containing the data for the patron for specified scheduling algorithm and print throughput value after execution.

## Implementation Details
-Patrons: Patrons are represented as individual threads in the system. They arrive at the bar at staggered times, place drink orders, and wait for their orders to be fulfilled.
-Barman: The barman is represented as a separate thread responsible for processing drink orders. Prepares drinks sequentially and notifies patrons when orders are ready.
-Drink Orders: Each drink order includes the type of drink and the patron who ordered it. Drink orders have different preparation times based on the type of drink.
-Synchronization: Thread safety is ensured using synchronization mechanisms such as synchronized blocks and AtomicBoolean variables.
-Metrics Recording: The system records metrics such as patron arrival time, total time taken to fulfill orders, and waiting time for each patron.
-Main: Contains the main method to initiate and coordinate the bar simulation.

## Author
This program was written by Sakhile Mjiyakho (MJYSAK001).
