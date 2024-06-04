def read_file(filename): ##Python Script to calculate the averages from the text files
    data = []
    with open(filename, 'r') as file:
        for line in file:
            data.append(int(line.strip()))
    return data

def calculate_average(data):
    if not data:
        return 0
    return sum(data) / len(data)

def extract_data(algorithm):
    turnaround_file = f"turnaround_time_{algorithm}.txt"
    waiting_file = f"waiting_time_{algorithm}.txt"
    response_file = f"response_time_{algorithm}.txt"
    
    turnaround_time = read_file(turnaround_file)
    waiting_time = read_file(waiting_file)
    response_time = read_file(response_file)
    
    return turnaround_time, waiting_time, response_time

def main():
    algorithms = ['0', '1']  # Algorithm identifiers
    for algorithm in algorithms:
        turnaround_time, waiting_time, response_time = extract_data(algorithm)
        
        avg_turnaround = calculate_average(turnaround_time)
        avg_waiting = calculate_average(waiting_time)
        avg_response = calculate_average(response_time)
        
        print(f"Algorithm {algorithm}:")
        print(f"  Average Turnaround Time: {avg_turnaround} milliseconds")
        print(f"  Average Waiting Time: {avg_waiting} milliseconds")
        print(f"  Average Response Time: {avg_response} milliseconds")
        print()

if __name__ == "__main__":
    main()
