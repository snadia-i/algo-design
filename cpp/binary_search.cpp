#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <chrono>
#include <string>
#include <sys/stat.h>
#include <iomanip>

using namespace std;
using namespace chrono;

// Function to perform binary search on a sorted vector
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

// Function to check if the vector is sorted
bool isSorted(const vector<int>& data) {
    for (size_t i = 1; i < data.size(); ++i) {
        if (data[i] < data[i - 1]) return false;
    }
    return true;
}

int main() {
    string filename;
    cout << "Enter the CSV file name: ";
    // Read the filename from user input
    getline(cin, filename);

    if (filename.empty()) {
        cout << "No file name provided. Exiting.\n";
        return 0;
    }

    vector<int> numbers;
    string line;
    ifstream file(filename);
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
        cout << "The file is not sorted. Binary search cannot be performed. Exiting.";
        return 1;
    }

    int n = numbers.size();
    // Select test cases for best, average, and worst case scenarios
    int best = numbers[(n - 1) / 2];      // Middle element (best case)
    int average = numbers[(n - 1) / 3];   // Some element (average case)
    int worst = INT_MAX;         // Element not in the list (worst case)

    // Create output directory if it doesn't exist
    string outputFile = "binary_search_" + to_string(n) + ".txt";
    ofstream out(outputFile);

    out << fixed << setprecision(3); // Set precision for output

    // Measure and record the time taken for the best case
    auto start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) { // Repeat to get a measurable time
        binarySearch(numbers, best);
    }
    auto end = high_resolution_clock::now();
    out << "Best case: " << duration<double, milli>(end - start).count() << " ms\n";

    // Measure and record the time taken for the average case
    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) { // Repeat to get a measurable time
        binarySearch(numbers, average);
    }
    end = high_resolution_clock::now();
    out << "Average case: " << duration<double, milli>(end - start).count() << " ms\n";

    // Measure and record the time taken for the worst case
    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) { // Repeat to get a measurable time
        binarySearch(numbers, worst);
    }
    end = high_resolution_clock::now();
    out << "Worst case: " << duration<double, milli>(end - start).count() << " ms\n";

    cout << "Binary search timings saved to: " << outputFile << "\n";
    out.close();

    return 0;
}