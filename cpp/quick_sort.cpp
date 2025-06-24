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
};

void quickSort(std::vector<Data>& arr, int low, int high);
int partition(std::vector<Data>& arr, int low, int high);

void quickSort(std::vector<Data>& arr, int low, int high) {
    if (low < high) {
        int pi = partition(arr, low, high);
        quickSort(arr, low, pi - 1);
        quickSort(arr, pi + 1, high);
    }
}

int partition(std::vector<Data>& arr, int low, int high) {
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

int main() {
    std::string filePath;
    std::cout << "Enter the CSV file path: ";
    std::getline(std::cin, filePath);

    std::vector<Data> dataList;
    std::ifstream infile(filePath);
    if (!infile) {
        std::cout << "Error reading file: cannot open file\n";
        return 1;
    }
    std::string line;
    while (std::getline(infile, line)) {
        std::istringstream ss(line);
        std::string numberStr, letters;
        if (std::getline(ss, numberStr, ',') && std::getline(ss, letters)) {
            try {
                int number = std::stoi(numberStr);
                dataList.push_back({number, letters});
            } catch (...) {
                std::cout << "Error reading file: invalid number format\n";
                return 1;
            }
        }
    }
    infile.close();

    auto startTime = std::chrono::high_resolution_clock::now();
    quickSort(dataList, 0, static_cast<int>(dataList.size()) - 1);
    auto endTime = std::chrono::high_resolution_clock::now();
    double durationMs = std::chrono::duration<double, std::milli>(endTime - startTime).count();
    std::cout << "Sorting runtime: " << durationMs << " ms\n";

    std::string outputFileName = "quick_sort_" + std::to_string(dataList.size()) + ".csv";
    std::ofstream outfile(outputFileName);
    if (!outfile) {
        std::cout << "Error writing file: cannot open file\n";
        return 1;
    }
    for (const auto& d : dataList) {
        outfile << d.number << "," << d.letters << "\n";
    }
    outfile.close();

    return 0;
}
