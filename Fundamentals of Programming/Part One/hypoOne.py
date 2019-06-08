#
# Author: Liam Pilling
# Date Last Modified: 28/05/2018
#
# Description: This file is a program to confirm the first hypothesis stated
#              in Hypothesis.txt. It takes the data from the two datasets
#              above and compares the populations living in mining towns from
#              2011 to 2016. It outputs the results of the data to the
#              terminal and generates the file 'HypoOneGraph.png', which is a
#              graph of the populations.
#

import matplotlib.pyplot as plt
plt.switch_backend('agg') #Added this due to problems running matplotlib
import re
import numpy as np

# Regex to find the post codes we are working with
regPostCode = re.compile(r'''(
                         (POA6271|POA6225|POA6751)
                         )''', re.VERBOSE)

fileOne = "2011Census_B01_WA_POA_short.csv"
fileTwo = "2016Census_G01_WA_POA.csv"


print('\nTesting Hypothesis one: There are less people living')
print('in mining towns in 2016 then there were living in 2011.\n')
print('Test towns: ')
print('    Collie:   6225')
print('    Capel:    6271')
print('    Dampier:  6713\n')

# Create this list for the graphing done below
townlist = ['Collie',
            'Capel',
            'Dampier']

# These are the arrays of the populations
poparrayOne = np.zeros(3)
poparrayTwo = np.zeros(3)

fileObj = open(fileOne)
i = 0
for line in fileObj.readlines():
    line = line.strip()
    searchLine = regPostCode.search(line)
    if searchLine:
        values = line.split(',')
        poparrayOne[i] = int(values[3]) # saving the populations into the array
        i += 1
fileObj.close()

print('Total population in 2011:', poparrayOne.sum())

fileObj = open(fileTwo)
i = 0
for line in fileObj.readlines():
    line = line.strip()
    searchLine = regPostCode.search(line)
    if searchLine:
        values = line.split(',')
        poparrayTwo[i] = int(values[3])
        i += 1
fileObj.close()

print('Total population in 2016:', poparrayTwo.sum())
print('\nAs we can see there are', poparrayOne.sum() - poparrayTwo.sum(),
      'less people living in mining')
print('areas in 2016 compared to 2011')

# This area list is only used for graphing purposes
area = [1,2,3]
plt.title('Comparing populations in 2011 and 2016')
plt.xlabel('Town | Red=2011, Blue=2016')
plt.ylabel('Populations')
# Using comprehensions to graph multiple bars in one graph
plt.bar([x - 0.2 for x in area], poparrayOne, 0.4, align ='center',
        color='blue')
plt.bar([x + 0.2 for x in area], poparrayTwo, 0.4, align ='center',
        color='red')
plt.xticks(area, townlist)
plt.savefig('HypoOneGraph.png')
