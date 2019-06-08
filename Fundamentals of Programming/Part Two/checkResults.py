#
# Author: Liam Pilling
# Date Last Modified: 28/05/2018
#
# Description: This file will read the given results of the NiLess or PolyJuice
#              drugs using regex and output to the screen the test with the 
#              lowest effective time with its dosage and interval.
#

import re
import sys
# The file name should be given through the command line
filename = sys.argv[1]
fileobj = open(filename)

# This is the regex to find the values we need in the results files
regEffective = re.compile(r'''(
                 (Effective\sat\s:\s+)
                 (\d+)
                 )''', re.VERBOSE)
regDosage = re.compile(r'''(
                       (DOSAGE:\s+)
                       (\d+)
                       )''', re.VERBOSE)
regIntervals = re.compile(r'''(
                       (INTERVALS:\s+)
                       (\d+)
                       )''', re.VERBOSE)

times = []
dosages = []
intervals = []
# Here we are reading the files to find the lowest time and the corresponding
# dosages and intervals
for text in fileobj.readlines():
    text = text.strip()
    effectLine = regEffective.search(text)
    dosageLine = regDosage.search(text)
    intervalLine = regIntervals.search(text)
    if effectLine:
        times.append(int(effectLine.group(3)))
    if dosageLine:
        dosages.append(int(dosageLine.group(3)))
    if intervalLine:
        intervals.append(int(intervalLine.group(3)))

# Here we display on the terminal the most efficient result with the lowest
# time
lowIndex = times.index(min(times))
print('LOWEST TIME: ', times[lowIndex])
print('DOSAGE:      ', dosages[lowIndex])
print('INTERVALS:   ', intervals[lowIndex])
        
