#include <iostream>
#include <fstream>
#include <sstream>
#include <vector>
#include <string>
#include <chrono>

// Define a tuple struct to hold an integer and a string
struct Tuple {
    int number;
    std::string text;
};

// Merge function for Tuple array
void merge(Tuple* array, int left, int mid, int right) {
    int n1 = mid - left + 1;
    int n2 = right - mid;

    Tuple* L = new Tuple[n1];
    Tuple* R = new Tuple[n2];

    for (int i = 0; i < n1; ++i) L[i] = array[left + i];
    for (int j = 0; j < n2; ++j) R[j] = array[mid + 1 + j];

    int i = 0, j = 0, k = left;
    while (i < n1 && j < n2) {
        // Compare by number first, then by text if numbers are equal
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

    delete[] L;
    delete[] R;
}

// Merge sort function for Tuple array
void doMergeSort(Tuple* array, int left, int right) {
    if (left < right) {
        int mid = (left + right) / 2;
        doMergeSort(array, left, mid);
        doMergeSort(array, mid + 1, right);
        merge(array, left, mid, right);
    }
}

int main() {
    std::string filePath;
    std::cout << "Enter CSV file path: ";
    std::getline(std::cin, filePath);

    std::vector<Tuple> tupleList;
    std::ifstream infile(filePath);
    if (!infile) {
        std::cout << "Error reading file: " << filePath << std::endl;
        return 1;
    }
    std::string line;
    while (std::getline(infile, line)) {
        std::istringstream iss(line);
        std::string numStr, text;
        if (std::getline(iss, numStr, ',') && std::getline(iss, text)) {
            try {
                int number = std::stoi(numStr);
                tupleList.push_back({number, text});
            } catch (...) {
                std::cout << "Invalid number format in file." << std::endl;
                return 1;
            }
        }
    }
    infile.close();

    int n = tupleList.size();
    Tuple* array = new Tuple[n];
    for (int i = 0; i < n; ++i) array[i] = tupleList[i];

    std::string outputFileName = "merge_sort_" + std::to_string(n) + ".csv";

    auto startTime = std::chrono::high_resolution_clock::now();
    doMergeSort(array, 0, n - 1);
    auto endTime = std::chrono::high_resolution_clock::now();

    std::ofstream outfile(outputFileName);
    if (!outfile) {
        std::cout << "Error writing output file: " << outputFileName << std::endl;
        delete[] array;
        return 1;
    }
    for (int i = 0; i < n; ++i) {
        outfile << array[i].number << "," << array[i].text << "\n";
    }
    outfile.close();

    std::chrono::duration<double, std::milli> runningTimeMs = endTime - startTime;
    std::cout << "Sorted data saved to " << outputFileName << std::endl;
    std::cout << "Running time: " << runningTimeMs.count() << " ms" << std::endl;

    delete[] array;
    return 0;
}
