#
# Author: Liam Pilling
# Date Last Modified: 28/05/2018
#
# Description: This file is a program to confirm the third hypothesis stated
#              in Hypothesis.txt. It takes the data from the two datasets and
#              uses it to compare increase in growth of senoirs in south west WA
#              comprared to the growth of all of WA. It displays the results to
#              the terminal and generates the file 'HypoThreeGraph.png', which
#              is a graph of the populations of seniors in south west WA in 2011
#              and in 2016.
#

import matplotlib.pyplot as plt
plt.switch_backend('agg') #Added this due to problems running matplotlib
import re
import numpy as np

# Regex to find the post codes we are working with
regPostCode = re.compile(r'''(
                         (POA6280|POA6285|POA6330)
                         )''', re.VERBOSE)

fileOne = "2011Census_B01_WA_POA_short.csv"
fileTwo = "2016Census_G01_WA_POA.csv"

print("Testing Hypothesis three: The rate of seniors living in SW ")
print("Australia in 2016 compared to 2011 (ages 65+) will be very high")
print("Test locations:\n    Busselton: 6280\n    Margaret River: 6285")
print("    Albany: 6751\n")

# Create this list for the graphing done below
townlist = ['Busselton',
            'Margaret River',
            'Albany']

# These are the arrays of populations of seniors in the south west
poparrayOne = np.zeros(3)
poparrayTwo = np.zeros(3)
# These are the total populations of WA in 2011 and 2016
popTotalOne = 0
popTotalTwo = 0

fileObj = open(fileOne)
i = 0
fileObj.readline() # Skip the first line since its just the descriptions
for line in fileObj.readlines():
    line = line.strip()
    searchLine = regPostCode.search(line)
    values = line.split(',')
    if searchLine:
        poparrayOne[i] = int(values[30]) + int(values[33]) + int(values[36])
        i += 1
    popTotalOne += int(values[3]) # Adding the populations of every line
fileObj.close()

print("The total number of seniors in 2011: ", poparrayOne.sum())

fileObj = open(fileTwo)
i = 0
fileObj.readline() # Skip the first line since its just the descriptions
for line in fileObj.readlines():
    line = line.strip()
    searchLine = regPostCode.search(line)
    values = line.split(',')
    if searchLine:
        poparrayTwo[i] = int(values[30]) + int(values[33]) + int(values[36])
        i += 1
    popTotalTwo += int(values[3]) # Adding the populations of every line
fileObj.close()

print("The total number of seniors in 2016: ", poparrayTwo.sum())
print("\nAs we can see the increased rate of seniors in SW Australia is very")
print("high, with a",
      (poparrayTwo.sum()/poparrayOne.sum()) * 100 - 100, "% jump in population")
print("This is compared to the", (popTotalTwo/popTotalOne) * 100 - 100,
        "% jump in total population in ")
print("all of WA")

# This area list is only used for graphing purposes
area = [1,2,3]

# Here we are graphing the populations of seniors in the south western towns
plt.title('Comparing seniors populations in 2011 and 2016 in the South West')
plt.xlabel('Town | Red=2011, Blue=2016')
plt.ylabel('Populations')
# Using comprehensions to graph multiple bars in one graph
plt.bar([x - 0.2 for x in area], poparrayOne, 0.4, align ='center',
                color='blue')
plt.bar([x + 0.2 for x in area], poparrayTwo, 0.4, align ='center',
                color='red')
plt.xticks(area, townlist)
plt.savefig('HypoThreeGraph.png')
