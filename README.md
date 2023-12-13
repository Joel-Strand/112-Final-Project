# 112-Final-Project
# To compile, run "javac Main.java" in the command line. To run, run "java Main {theta} < {filepath}" in the command line. When the simulation runs, you have three controls:
  1. To add new bodies, click and hold at point you want to spawn bodies at. New bodies will spawn at your mouse location. Bodies will stop spawning when you release the mouse. 
  2. To create a black hole, hold down "q". A black hole will spawn at your mouse location and will go away when release "q".
  3. Use the left arrow key to move backwards in time and the right arrow key to move forwards in time. The program will run normally when you release the arrow keys.

Theta is a threshold value from 0-2 that determines how accurate the program will run. 0 is a brute force calculation with no approximations for efficiency. 2 represents maximum efficiency with much approximation. Adjust theta higher or lower to make the graphics smoother depending on how many bodies are being simulated. 

Note that depending on the size of the galaxy, theta value selected and device RAM, the program will run at different speeds. A device with at least 16 gigs of RAM will simulate any of the given galaxies seamlessly with a theta value of 1.5 or higher(heavy approximation). Devices with less RAM will experience lag when simulating large galaxies. A low theta value(little approximation) will result in heavy lag unless the simulation is being run on a super computer. A theta value of 0(brute force) will most likely cause the program to crash.

We credit StdDraw.java to https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html.
