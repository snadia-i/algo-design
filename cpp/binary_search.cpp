#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <chrono>
#include <string>
#include <sys/stat.h>

using namespace std;
using namespace chrono;

// Performs binary search on a sorted vector of integers.
// Returns the index of 'target' if found, otherwise returns -1.
int binarySearch(const vector<int>& data, int target) {
    int low = 0, high = data.size() - 1;
    while (low <= high) {
        int mid = (low + high) / 2;
        if (data[mid] == target) return mid;
        else if (data[mid] < target) low = mid + 1;
        else high = mid - 1;
    }
    return -1;
}

// Checks if the input vector is sorted in non-decreasing order.
bool isSorted(const vector<int>& data) {
    for (size_t i = 1; i < data.size(); ++i) {
        if (data[i] < data[i - 1]) return false;
    }
    return true;
}

int main() {
    string filename;
    cout << "Enter the CSV file name: ";
    getline(cin, filename); // Get the filename from user input

    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Error opening file: " << filename << "\n";
        return 1;
    }

    vector<int> numbers;
    string line;
    // Read numbers from the CSV file, assuming one number per line or comma-separated
    while (getline(file, line)) {
        stringstream ss(line);
        string value;
        if (getline(ss, value, ',')) {
            try {
                numbers.push_back(stoi(value)); // Convert string to integer and add to vector
            } catch (...) {} // Ignore conversion errors
        }
    }
    file.close();

    if (numbers.empty()) {
        cout << "No numbers found in the file. Exiting.\n";
        return 0;
    }

    // Ensure the numbers are sorted before performing binary search
    if (!isSorted(numbers)) {
        cout << "The file is not sorted. Binary search cannot proceed.\n";
        return 1;
    }

    int n = numbers.size();
    // Select test cases for best, average, and worst case scenarios
    int best = numbers[n / 2];      // Middle element (best case)
    int average = numbers[n / 3];   // Some element (average case)
    int worst = 2000000000;         // Element not in the list (worst case)

    // Create output directory if it doesn't exist
    mkdir("outputs", 0777);
    string outputFile = "outputs/binary_search_" + to_string(n) + ".txt";
    ofstream out(outputFile);
    if (!out.is_open()) {
        cerr << "Failed to create output file.\n";
        return 1;
    }

    // Measure and record the time taken for the best case
    auto start = high_resolution_clock::now();
    binarySearch(numbers, best);
    auto end = high_resolution_clock::now();
    out << "Best case: " << duration_cast<microseconds>(end - start).count() << " µs\n";

    // Measure and record the time taken for the average case
    start = high_resolution_clock::now();
    binarySearch(numbers, average);
    end = high_resolution_clock::now();
    out << "Average case: " << duration_cast<microseconds>(end - start).count() << " µs\n";

    // Measure and record the time taken for the worst case
    start = high_resolution_clock::now();
    binarySearch(numbers, worst);
    end = high_resolution_clock::now();
    out << "Worst case: " << duration_cast<microseconds>(end - start).count() << " µs\n";

    cout << "\nBinary search timings saved to: " << outputFile << "\n";
    out.close();

    return 0;
}