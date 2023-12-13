# 112-Final-Project
# To compile, run "javac Main.java" in the command line. To run, run "java Main {theta} < {filepath}" in the command line. File path must be the absolute file path.

Theta is a threshold value from 0-2 that determines how accurate the program will run. 0 is a brute force calculation with no approximations for efficiency. 2 reprents maximum efficiency with much approximation. Adjust theta higher or lower to make the graphics smoother depending on how many bodies are being simulated.

Note that depending on the size of the galaxy, theta value selected and device RAM, the program will run at different speeds. A device with at least 32 gigs of RAM will simulate any of the given galaxies seamlessly with a theta value of 1.5 or higher(heavy approximation). Devices with less RAM will experience lag when simulating large galaxies. A low theta value will result in heavy lag unless the simulation is being run on a super computer.

We credit StdDraw.java to https://introcs.cs.princeton.edu/java/stdlib/StdDraw.java.html.
