#include <iostream>
#include <fstream>
#include <vector>
#include <string>
#include <sstream>
#include <algorithm>
#include <sys/stat.h>

using namespace std;

int main() {
    try {
        string filename;
        int target;
        
        cout << "Enter the CSV file name: ";
        getline(cin, filename); // Get the filename from user input
        
        if (filename.empty()) {
            cout << "No file name provided. Exiting." << endl;
            return 0;
        }
        
        cout << "Enter the target value to search for: ";
        string targetInput;
        getline(cin, targetInput); // Get the target value as a string
        
        try {
            target = stoi(targetInput); // Convert target input to integer
            if (target < 0) {
                cout << "Invalid target value. Exiting." << endl;
                return 0;
            }
        } catch (const invalid_argument& e) {
            cout << "Invalid input for target value. Please enter a valid integer." << endl;
            return 0;
        } catch (const out_of_range& e) {
            cout << "Target value out of range. Please enter a valid integer." << endl;
            return 0;
        }
        
        vector<int> numbers;
        vector<string> names;
        
        ifstream file(filename);
        if (!file.is_open()) {
            cout << "File not found: " << filename << endl;
            return 0;
        }
        
        string line;
        // Read each line from the CSV file and parse number and name
        while (getline(file, line)) {
            stringstream ss(line);
            string numberStr, name;
            
            if (getline(ss, numberStr, ',') && getline(ss, name)) {
                // Trim whitespace from number and name
                numberStr.erase(0, numberStr.find_first_not_of(" \t"));
                numberStr.erase(numberStr.find_last_not_of(" \t") + 1);
                name.erase(0, name.find_first_not_of(" \t"));
                name.erase(name.find_last_not_of(" \t") + 1);
                
                try {
                    numbers.push_back(stoi(numberStr)); // Convert and store number
                    names.push_back(name);              // Store name
                } catch (const invalid_argument& e) {
                    cout << "Error parsing number in CSV: " << numberStr << endl;
                    return 0;
                }
            }
        }
        file.close();
        
        if (numbers.empty()) {
            cout << "No data found in the file." << endl;
            return 0;
        }
        
        // Check if the numbers are sorted in non-decreasing order
        for (size_t i = 1; i < numbers.size(); i++) {
            if (numbers[i] < numbers[i - 1]) {
                cout << "The numbers in the file are not sorted. Please provide a sorted file." << endl;
                return 0;
            }
        }
        
        // Create output directory if it doesn't exist
        string outputFile = "binary_search_step_" + to_string(target) + ".txt";
        ofstream out(outputFile);
        
        if (!out.is_open()) {
            cout << "Error creating output file: " << outputFile << endl;
            return 0;
        }
        
        int low = 0, high = numbers.size() - 1;
        
        // Perform binary search and record each step
        while (low <= high) {
            int mid = (low + high) / 2;
            string step = to_string(mid) + ": " + to_string(numbers[mid]) + "/" + names[mid];
            
            out << step << endl;    // Write step to output file
            cout << step << endl;   // Print step to console
            
            if (numbers[mid] == target) {
                out.close();
                // cout << "Target found at index: " << mid << " (" << names[mid] << ")" << endl;
                cout << "Binary search steps recorded in: " << outputFile << endl;
                return 0;
            } else if (numbers[mid] < target) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        // Target not found, record -1
        out << "-1" << endl;
        out.close();
        cout << "Target not found. Binary search steps recorded in: " << outputFile << endl;
        
    } catch (const exception& e) {
        cout << "An error occurred: " << e.what() << endl;
        return 1;
    }
    
    return 0;
}