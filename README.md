# FileCompare
Simple utility for performing file comparisons.

The goal is to eventually be able to do fuzzy comparisons and find similar files on disk or in compressed archives.

Currently searches 2 given paths for exactly duplicated files and outputs the duplicate pairs to System.out in JSON format.

Usage: `java -jar FileCompare.jar ./dir1 ./dir2`
