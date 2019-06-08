#
# Author: Liam Pilling
# Date Last Modified: 28/05/2018
#
# Description: This file is a program to confirm the second hypothesis stated
#              in Hypothesis.txt. It takes the data from the 2016 dataset and
#              uses it to compare populations of men and women in different 
#              areas of WA. It outputs the results of the data to the terminal
#              and generates the files 'HypoTwoPopGraph', which is a graph of
#              the populations of men and women, and 'HypoTwoRatioGraph', which
#              is a graph of the ratios of men to women.
#

import matplotlib.pyplot as plt
plt.switch_backend('agg') #Added this due to problems running matplotlib
import re
import numpy as np

# Regex to find the post codes we are working with
northPostCode = re.compile(r'''(
                           (POA6530|POA6707|POA6725)
                           )''', re.VERBOSE)
perthPostCode = re.compile(r'''(
                           (POA6021|POA6056|POA6164)
                           )''', re.VERBOSE)

fileTwo = "2016Census_G01_WA_POA.csv"

print("Testing Hypothesis two: There is a higher ratio of men to women ")
print("living in northern WA than in the city in 2016.")
print("Test towns:\nNorthern WA:\n    Geraldton: 6530\n    Exmouth: 6707")
print("    Broome: 6725\nPerth city suburbs:\n    Stirling: 6021")
print("    Midland: 6056\n    Cockburn: 6164\n")

# Create this list for the graphing done below
townlist = ['Geraldton',
            'Exmouth',
            'Broome',
            'Stirling',
            'Midland',
            'Cockburn']

# These are the arrays of the populations and an array of the ratios of men to
# women
popArrayMale = np.zeros(6)
popArrayFemale = np.zeros(6)
popArrayRatios = np.zeros(6)

#These are the total populations of men and women for 2016 in northern WA
totalPopMale = 0
totalPopFemale = 0
fileObj = open(fileTwo)
i = 0
for line in fileObj.readlines():
    line = line.strip()
    searchLine = northPostCode.search(line)
    if searchLine:
        values = line.split(',')
        totalPopMale += int(values[1])
        totalPopFemale += int(values[2])
        popArrayMale[i] = int(values[1])
        popArrayFemale[i] = int(values[2])
        popArrayRatios[i] = float(values[1])/float(values[2])
        i += 1
fileObj.close()
ratioOne = float(totalPopMale / totalPopFemale)
print("Population of northern WA men:  ", totalPopMale)
print("Population of northern WA women:", totalPopFemale)
print("First ratio is ", ratioOne, "\n")

#These are the total populations of men and women for 2016 in Perth
totalPopMale = 0
totalPopFemale = 0
fileObj = open(fileTwo)
i = 3
for line in fileObj.readlines():
    line = line.strip()
    searchLine = perthPostCode.search(line)
    if searchLine:
        values = line.split(',')
        totalPopMale += int(values[1])
        totalPopFemale += int(values[2])
        popArrayMale[i] = int(values[1])
        popArrayFemale[i] = int(values[2])
        popArrayRatios[i] = float(values[1])/float(values[2])
        i += 1
fileObj.close()
ratioTwo = float(totalPopMale / totalPopFemale)
print("Population of perth WA men:  ", totalPopMale)
print("Population of perth WA women:", totalPopFemale)
print("Second ratio is ", ratioTwo, "\n")

print("As we can see there the ratio of men to women in northern wa is ")
print("slightly igher than Perth in 2016")

# This area list is only used for graphing purposes
area = [1,2,3,4,5,6]

# Here we are graphing the populations
plt.figure()
plt.title('Comparing men/women populations in 2016')
plt.xlabel('City/Suburb | Red=Male, Blue=Female')
plt.ylabel('Populations')
# Using comprehensions to graph multiple bars in one graph
plt.bar([x - 0.2 for x in area], popArrayMale, 0.4, align ='center',
        color='red')
plt.bar([x + 0.2 for x in area], popArrayFemale, 0.4, align ='center',
        color='blue')
plt.xticks(area, townlist)
plt.savefig('HypoTwoPopGraph.png')

#Here we are graphing the rations of men to women
plt.figure()
plt.title('Comparing men/women ratios in 2016')
plt.xlabel('City/Suburb')
plt.ylabel('Ratios')
plt.bar(area, popArrayRatios, 0.8, align ='center', color='blue')
plt.xticks(area, townlist)
plt.savefig('HypoTwoRatioGraph.png')
