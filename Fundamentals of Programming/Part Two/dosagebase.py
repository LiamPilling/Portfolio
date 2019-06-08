#
# Author: based on the Prac06 code, modified by Liam Pilling
# Date Last Modified: 28/05/2018
#
# Description: This is a file to see if the given parameters for a description
#              of a drug has an effective result. The file takes in seven
#              command line parameters from the sweep.sh file, and runs a
#              sumulation of the drugs effect over 200 hours. If the simulation
#              is deemed successful it will print the simulation details and
#              graph them to a file with the format 'dosage_I' + interval + '_D'
#              + dosage + '.png. Eg, 'dosage_I1_D100000.png'.
#

import matplotlib.pyplot as plt
plt.switch_backend('agg')
import math
import numpy as np
import sys

# Process command line arguments
# Use defaults if not enough command line arguments
if(len(sys.argv) < 6):
    print('argv too short, usage: python3 <interval> <dosage>')
    print('Using default values for interval (8) and dosage (100)')
    Vinterval = 8
    Vdosage = 100
    Vname = "drug"
    Vhalf = 1.0
    Veffect = 1.0
    Vtoxic = 100
    Vrate = 1.0
else:
    Vinterval = int(sys.argv[1])  # The interval of the drug
    Vdosage = int(sys.argv[2])    # The dosage of the drug
    Vname = sys.argv[3]           # The name of the drug
    Vhalf = float(sys.argv[4])    # The half life of the drug
    Veffect = float(sys.argv[5])  # The effective concentation of the drug
    Vtoxic = float(sys.argv[6])   # The toxic concentration of the drug
    Vrate = float(sys.argv[7])    # The absorption rate of the drug

half_life = Vhalf         # Command line half-life
MEC = Veffect             # Effective concentration
MTC = Vtoxic              # Toxic concentration
volume = 3000             # blood plasma volume
Vdosage *= 1000           # dosage 100mg
absorption_fraction = Vrate
drug_in_system = 0        # initial amount of drug in system
ln05 = math.log(0.5)
elimination_constant = -ln05/half_life
pulse = 0
entering = absorption_fraction * pulse * Vdosage
elimination = elimination_constant * drug_in_system
concentration = drug_in_system/volume

simulation_time = 200     # simulation time 200
time_step_size = 1        # time step = 1 hour
num_steps = int(simulation_time/time_step_size)
cumulative_time = 0.0     # initial time = 0

values = np.empty(num_steps)

for time_step in range (num_steps):
    values[time_step] = concentration
    if (time_step % Vinterval == 0):
        pulse = 1
    else:
        pulse = 0
    entering = absorption_fraction * pulse * Vdosage
    elimination = elimination_constant * drug_in_system
    drug_in_system = drug_in_system - elimination + entering
    concentration = drug_in_system / volume

times = np.linspace(0, simulation_time - time_step_size, num_steps)

MECline = np.full(num_steps, MEC)
MTCline = np.full(num_steps, MTC)

effective = 0
while effective < len(values) and values[effective] < MEC:
    effective += 1
# Here we are only printing if the values are deemed to be effective and they
# they are below the toxic concentration level and the average concentration 
# after the effective time is higher than the effective concentration
if (effective < len(values) and values[effective:].max() < MTC
    and values[effective:].mean()) > MEC:
    print('\nPARAMETERS\n')
    print(Vname, ' half-life:\t',half_life)
    print('Effective concentration:\t', MEC)
    print('Toxic concentration: \t', MTC)
    print('Blood plasma volume: \t', volume)
    print('Absorption fraction: \t', absorption_fraction)
    print('Initial amt drug in system: \t', drug_in_system)
    print('Elimination constant: \t', elimination_constant)
    print('Simulation time (hrs): \t', simulation_time)
    print('Time step size: \t', time_step_size)
    print('Number of timesteps: \t', num_steps)

    print('\nSUCCESSFUL RESULTS\n')
    print('Effective at : \t', effective)
    print('DOSAGE: \t', Vdosage)
    print('INTERVALS: \t', Vinterval)
    print('Minimum value: \t', values.min())
    print('Maximum value: \t', values.max())
    print('Average value: \t', values.mean())

    print(len(values))
    print(effective)

    print('Minimum post-effective value: \t', values[effective:].min())
    print('Maximum post-effective value: \t', values[effective:].max())
    print('Average post-effective value: \t', values[effective:].mean())
    # Here we plot the successful graph and add the important values to the
    # labels
    plt.figure()
    plt.title(Vname + ' Concentration | Interval: ' + str(Vinterval) + 
              ' | Dosage: ' + str(Vdosage))
    plt.xlabel('Time (hours) | Effective at: ' + str(effective))
    plt.ylabel('Concentration | Average after effective: ' + 
               str(values[effective:].mean()))
    plt.plot(times, values, '-', times, MECline, 'g-', times, MTCline, 'r-')
    plt.savefig(Vname + 'dosage_' + 'I' + str(Vinterval) + '_D' +
                 str(Vdosage) + '.png')

