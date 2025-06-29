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
        // Split the line by commas and convert to integers, ignoring any non-integer values
        if (getline(ss, value, ',')) {
            try {
                numbers.push_back(stoi(value));
            } catch (...) {}
        }
    }
    file.close();

    // Check if the numbers vector is empty
    if (numbers.empty()) {
        cout << "No numbers found in the file. Exiting.\n";
        return 0;
    }

    // Ensure the numbers are sorted before performing binary search
    if (!isSorted(numbers)) {
        cout << "The file is not sorted. Binary search cannot be performed. Exiting.";
        return 1;
    }

    // Calculate the best, average, and worst cases
    int n = numbers.size();
    int best = numbers[(n - 1) / 2];      // Middle element (best case)
    int average = numbers[(n - 1) / 3];   // Some element (average case)
    int worst = INT_MAX;         // Element not in the list (worst case)

    // Create output directory if it doesn't exist
    string outputFile = "binary_search_" + to_string(n) + ".txt";
    ofstream out(outputFile);

    // set the output precision for floating-point numbers
    out << fixed << setprecision(3);

    // Measure and record the time taken for the best case
    auto start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) { // Perform n searches to get a measurable time
        binarySearch(numbers, best);
    }
    auto end = high_resolution_clock::now();
    out << "Best case: " << duration<double, milli>(end - start).count() << " ms\n";

    // Measure and record the time taken for the average case
    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) { // Perform n searches to get a measurable time
        binarySearch(numbers, average);
    }
    end = high_resolution_clock::now();
    out << "Average case: " << duration<double, milli>(end - start).count() << " ms\n";

    // Measure and record the time taken for the worst case
    start = high_resolution_clock::now();
    for (int i = 0; i < n; ++i) { // Perform n searches to get a measurable time
        binarySearch(numbers, worst);
    }
    end = high_resolution_clock::now();
    out << "Worst case: " << duration<double, milli>(end - start).count() << " ms\n";

    // Output the results
    cout << "Binary search timings saved to: " << outputFile << "\n";
    out.close();

    return 0;
}