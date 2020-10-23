# PerspectiveProjection
Graphics Project Implementing an interactive demostration of Perspective Projection to render 3-Dimensional Objects


# Introduction
The goal of this project was to implement Perspective Projection. This would allow the program to take an input of three-dimensional endpoints and output them into two-dimensional coordinates in order for them to be scan converted onto a screen. 


# User Guide
Once the application is running, there are 3 core areas to be aware of.
* Parameters
* Transformations
* Global Control



### Parameters
The parameter fields allow the user to input the necessary values to create transformations. 
These fields are: 
* Translation Factors
* Scaling Factors
* Angle of Rotation
Additionally, this region includes an input field for the number of lines inside the programs input file which will be discussed later in this file.

### Transformations
The transformation buttons allow the user to create transformations for the displayed shape. When clicked, each will pull the needed parameters from the fields above to use as input for their corresponding functions. 
These functions are:
*Scale
* Translate
* X-Axis Rotate
* Y-Axis Rotate
* Z-Axos Rotate 
Internally, these controls call functions that will generate a 4x4 Transformation Matrix and multiply it by the program’s [Global Transformation Matrix] (GTM). In this way the program allows the user to concatenate multiple transformations into the GTM.

### Global Controls
The global control area calls for initialization, applies transformations and resets internal data. The program requires an input file containing line data in the form of pairs of 3-dimensional endpoints separated by spaces on each line – ‘X1 Y1 Z1 X2 Y2 Z2’.
* **Load Line Data** 
  * reads the points into an internal data structure.  
* **Apply Transformations** 
  * will apply the GTM to the stored 3-dimensional endpoints.
* **Draw Lines** 
  * will iterate through all the endpoints and call the Perspective Projection function on each before scan converting the resultant lines.
* **Reset Matrix** 
  * will reset the GTM to the 4x4 identity matrix. Clear will reset the visual canvas, clearing all previously generated lines.
  
  
  
## Example
![Image tunnel](/Images/tunnel.PNG)
  
