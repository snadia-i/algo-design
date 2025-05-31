#include <iostream>
#include <fstream>
#include <vector>
#include <sstream>
#include <chrono>
#include <string>
#include <sys/stat.h>

using namespace std;
using namespace chrono;

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

bool isSorted(const vector<int>& data) {
    for (size_t i = 1; i < data.size(); ++i) {
        if (data[i] < data[i - 1]) return false;
    }
    return true;
}

int main() {
    string filename;
    cout << "Enter the CSV file name: ";
    getline(cin, filename);

    ifstream file(filename);
    if (!file.is_open()) {
        cerr << "Error opening file: " << filename << "\n";
        return 1;
    }

    vector<int> numbers;
    string line;
    while (getline(file, line)) {
        stringstream ss(line);
        string value;
        if (getline(ss, value, ',')) {
            try {
                numbers.push_back(stoi(value));
            } catch (...) {}
        }
    }
    file.close();

    if (numbers.empty()) {
        cout << "No numbers found in the file. Exiting.\n";
        return 0;
    }

    if (!isSorted(numbers)) {
        cout << "The file is not sorted. Binary search cannot proceed.\n";
        return 1;
    }

    int n = numbers.size();
    int best = numbers[n / 2];
    int average = numbers[n / 3];
    int worst = 2000000000;

    mkdir("outputs", 0777);
    string outputFile = "outputs/binary_search_" + to_string(n) + ".txt";
    ofstream out(outputFile);
    if (!out.is_open()) {
        cerr << "Failed to create output file.\n";
        return 1;
    }

    auto start = high_resolution_clock::now();
    binarySearch(numbers, best);
    auto end = high_resolution_clock::now();
    out << "Best case: " << duration_cast<microseconds>(end - start).count() << " µs\n";

    start = high_resolution_clock::now();
    binarySearch(numbers, average);
    end = high_resolution_clock::now();
    out << "Average case: " << duration_cast<microseconds>(end - start).count() << " µs\n";

    start = high_resolution_clock::now();
    binarySearch(numbers, worst);
    end = high_resolution_clock::now();
    out << "Worst case: " << duration_cast<microseconds>(end - start).count() << " µs\n";

    cout << "\nBinary search timings saved to: " << outputFile << "\n";
    out.close();

    return 0;
}