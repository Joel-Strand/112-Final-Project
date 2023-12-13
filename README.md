# COSC-112 Group 7: The Barnes-Hut Tree Algorithm
## Running the Program
To compile, run "javac Main.java" in the command line. To run, run "java Main {theta} < {filepath}" in the command line. The file path must be the absolute file path. 

## When the simulation runs, you have numerous controls:
1.  To add new bodies, click and hold within the window of the progam to spawn bodies with random masses, velocities, and colors. New bodies will spawn at your mouse location. Bodies will stop spawning when you release the mouse. <br />
    a. Use the SHIFT Key while creating new bodies to create bodies with a much larger mass  
2. To create a black hole, press "Q" with your mouse within the window of the program. Multiple can be created by holding the mouse down. A black hole will spawn at your mouse location.
3. Arrow Keys: <br />
    a. Use the LEFT arrow key to set the simulation in reverse time. <br />
    b. Use the RIGHT arrow key to set the simulation to advance time forward. <br />
    c. Use the UP arrow key to increase time by 5%. Hold key to compound effect. <br />
    d. Use the UP arrow key to decrease time by 5%. Hold key to compound effect. <br />
    Note: Program runs at a default deltaTime value of 0.1. LEFT AND RIGHT keys return deltaTime to 0.1 with the resepctive sign depending on key pressed.

## Input files and format:
There are multiple curated sample files availble for use in /texts. To create custom files, the following format MUST be in place, otherwise resulting in a parsing error. 

1. First Line: integer of total bodies in universe
2. Second Line: radius of universe
3. Every Line Following: {X coordinate} {Y coordinate} {VelocityX} {Velocity Y} {Mass} {int for Red Color} {int for Green Color} {int for Blue Color} <br />
    Examples: 1.88068E06 -2.32834E06 1.26796E05 -4.89028E04 1.30749E24  255 255 0 <br />
             -8.57582E05 3.38400E05 5.69846E03 5.98528E02 5.40000E18  175 255 100 <br />
    Note: All values seperated by a white space. Any values EXCEPT Color values can be formatted with 'E' for exponential arithmetic.


## A note about theta: 
Theta is the threshold value from 0-2 that determines how accurately the program calculates the force vectors acting upon each body in the simulation. 0 is a brute force calculation with no algorithmic approximations for efficiency. 2 represents maximum efficiency with much approximation. Adjust theta higher or lower to make the graphics smoother depending on how many bodies are being simulated. 

Note that depending on the size of the galaxy, theta value selected and device RAM, the program will run at different speeds. A device with at least 16 gigs of RAM will simulate any of the given galaxies seamlessly with a theta value of 1.5 or higher(heavy approximation). Devices with less RAM will experience lag when simulating large galaxies. A low theta value(little approximation) will result in heavy lag unless the simulation is being run on a super computer. A theta value of 0(brute force) will most likely cause the program to crash.

## A note about Github User u/chindesaurus: 
We credit StdDraw.java and all of the .txt files found within /texts to u/chindesaurus on GitHub (Repo Link: https://github.com/chindesaurus/BarnesHut-N-Body). The entirety of these files have been created by him, and we implemented StdDraw as it contained numerous utilities required to correctly display the graphics of the simluation.