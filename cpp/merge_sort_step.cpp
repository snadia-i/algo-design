#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <iomanip>
#include <chrono>

struct Tuple {
    int number;
    std::string text;
    Tuple(int n = 0, const std::string& t = "") : number(n), text(t) {}
};

// Format array as [number/text, ...]
std::string formatArray(const std::vector<Tuple>& array) {
    std::ostringstream oss;
    oss << "[";
    for (size_t i = 0; i < array.size(); ++i) {
        oss << array[i].number << "/" << array[i].text;
        if (i != array.size() - 1) oss << ", ";
    }
    oss << "]";
    return oss.str();
}

// Merge function for Tuple array
void merge(std::vector<Tuple>& array, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;
    std::vector<Tuple> L(n1), R(n2);

    for (int i = 0; i < n1; ++i) L[i] = array[left + i];
    for (int j = 0; j < n2; ++j) R[j] = array[mid + 1 + j];

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) {
        if (L[i].number < R[j].number) {
            array[k++] = L[i++];
        } else if (L[i].number > R[j].number) {
            array[k++] = R[j++];
        } else {
            if (L[i].text <= R[j].text) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }
        }
    }
    while (i < n1) array[k++] = L[i++];
    while (j < n2) array[k++] = R[j++];
}

// Merge sort function for Tuple array
void doMergeSort(std::vector<Tuple>& array, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        doMergeSort(array, left, mid);
        doMergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }
}

// Modified merge to log steps
void mergeWithSteps(std::vector<Tuple>& array, int left, int mid, int right, std::ofstream& ofs) {
    int n1 = mid - left + 1;
    int n2 = right - mid;
    std::vector<Tuple> L(n1), R(n2);

    for (int i = 0; i < n1; ++i) L[i] = array[left + i];
    for (int j = 0; j < n2; ++j) R[j] = array[mid + 1 + j];

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) {
        if (L[i].number < R[j].number) {
            array[k++] = L[i++];
        } else if (L[i].number > R[j].number) {
            array[k++] = R[j++];
        } else {
            if (L[i].text <= R[j].text) {
                array[k++] = L[i++];
            } else {
                array[k++] = R[j++];
            }
        }
    }
    while (i < n1) array[k++] = L[i++];
    while (j < n2) array[k++] = R[j++];
}

// Modified merge sort to log steps
void doMergeSortWithSteps(std::vector<Tuple>& array, int left, int right, std::ofstream& ofs) {
    if (left < right) {
        int mid = (left + right) / 2;
        doMergeSortWithSteps(array, left, mid, ofs);
        doMergeSortWithSteps(array, mid + 1, right, ofs);
        mergeWithSteps(array, left, mid, right, ofs);
        ofs << formatArray(array) << "\n";
    }
}

int main() {
    std::string filePath;
    int startRow, endRow;

    std::cout << "Enter CSV file path: ";
    std::getline(std::cin, filePath);
    std::cout << "Enter start row begin at 1: ";
    std::cin >> startRow;
    std::cout << "Enter end row: ";
    std::cin >> endRow;

    if (startRow < 1 || endRow < startRow) {
        std::cout << "Invalid row range.\n";
        return 1;
    }

    std::vector<Tuple> tupleList;
    std::ifstream infile(filePath);
    if (!infile) {
        std::cout << "Error reading file: " << filePath << "\n";
        return 1;
    }
    std::string line;
    int currentRow = 0;
    while (std::getline(infile, line)) {
        ++currentRow;
        if (currentRow < startRow) continue;
        if (currentRow > endRow) break;
        std::istringstream iss(line);
        std::string numStr, text;
        if (std::getline(iss, numStr, ',') && std::getline(iss, text)) {
            try {
                int number = std::stoi(numStr);
                // Remove leading/trailing spaces
                text.erase(0, text.find_first_not_of(" \t\r\n"));
                text.erase(text.find_last_not_of(" \t\r\n") + 1);
                tupleList.emplace_back(number, text);
            } catch (...) {
                std::cout << "Invalid number format in file.\n";
                return 1;
            }
        }
    }

    int n = tupleList.size();
    std::string outputFileName = "merge_sort_step_" + std::to_string(startRow) + "_" + std::to_string(endRow) + ".txt";

    auto startTime = std::chrono::high_resolution_clock::now();
    std::ofstream ofs(outputFileName);
    if (!ofs) {
        std::cout << "Error writing output file: " << outputFileName << "\n";
        return 1;
    }
    ofs << formatArray(tupleList) << "\n";
    doMergeSortWithSteps(tupleList, 0, n - 1, ofs);
    ofs.close();
    auto endTime = std::chrono::high_resolution_clock::now();

    double runningTimeMs = std::chrono::duration<double, std::milli>(endTime - startTime).count();
    std::cout << "Sorted steps saved to " << outputFileName << "\n";
    std::cout << "Running time: " << std::fixed << std::setprecision(3) << runningTimeMs << " ms\n";
    return 0;
}
