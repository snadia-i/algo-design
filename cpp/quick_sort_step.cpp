#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <chrono>

struct Data {
    int number;
    std::string letters;

    bool operator<=(const Data& other) const {
        return number <= other.number;
    }
    std::string toString() const {
        return std::to_string(number) + "/" + letters;
    }
};

std::vector<std::string> sortingSteps;

std::string arraySliceToString(const std::vector<Data>& arr, int start, int end) {
    std::ostringstream oss;
    oss << "[";
    for (int i = start; i <= end; ++i) {
        oss << arr[i].toString();
        if (i < end) oss << ", ";
    }
    oss << "]";
    return oss.str();
}

int partition(std::vector<Data>& arr, int low, int high, int startRow, int endRow) {
    Data pivot = arr[high];
    int i = low - 1;
    for (int j = low; j < high; ++j) {
        if (arr[j] <= pivot) {
            ++i;
            std::swap(arr[i], arr[j]);
        }
    }
    std::swap(arr[i + 1], arr[high]);
    return i + 1;
}

void quickSort(std::vector<Data>& arr, int low, int high, int startRow, int endRow) {
    if (low < high) {
        int pi = partition(arr, low, high, startRow, endRow);
        sortingSteps.push_back("pi=" + std::to_string(pi) + " " + arraySliceToString(arr, startRow, endRow));
        quickSort(arr, low, pi - 1, startRow, endRow);
        quickSort(arr, pi + 1, high, startRow, endRow);
    }
}

int main() {
    std::string filePath;
    std::cout << "Enter the CSV file path: ";
    std::getline(std::cin, filePath);

    std::vector<Data> dataList;
    std::ifstream infile(filePath);
    if (!infile) {
        std::cout << "Error reading file: cannot open file." << std::endl;
        return 1;
    }
    std::string line;
    while (std::getline(infile, line)) {
        std::istringstream iss(line);
        std::string numStr, letters;
        if (std::getline(iss, numStr, ',') && std::getline(iss, letters)) {
            try {
                int number = std::stoi(numStr);
                dataList.push_back({number, letters});
            } catch (...) {
                // Ignore invalid lines
            }
        }
    }
    infile.close();

    int startRowInput, endRowInput;
    std::cout << "Enter start row: ";
    std::cin >> startRowInput;
    std::cout << "Enter end row: ";
    std::cin >> endRowInput;

    int startRow = startRowInput - 1;
    int endRow = endRowInput - 1;

    if (startRow < 0 || endRow >= (int)dataList.size() || startRow > endRow) {
        std::cout << "Invalid start or end row." << std::endl;
        return 1;
    }

    sortingSteps.push_back(arraySliceToString(dataList, startRow, endRow));

    auto startTime = std::chrono::high_resolution_clock::now();
    quickSort(dataList, startRow, endRow, startRow, endRow);
    auto endTime = std::chrono::high_resolution_clock::now();
    double durationMs = std::chrono::duration<double, std::milli>(endTime - startTime).count();

    std::string outputFileName = "quick_sort_step_" + std::to_string(startRow + 1) + "_" + std::to_string(endRow + 1) + ".txt";
    std::ofstream outfile(outputFileName);
    if (!outfile) {
        std::cout << "Error writing file: cannot open file." << std::endl;
        return 1;
    }
    outfile << "Sorting steps for elements from row " << (startRow + 1) << " to " << (endRow + 1) << ":\n";
    for (const auto& step : sortingSteps) {
        outfile << step << "\n";
    }
    outfile.close();

    return 0;
}
