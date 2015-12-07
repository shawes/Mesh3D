# Mesh Quadrats

Mesh_quadrat is a command line tool that splits up rectangular 3D meshes into a number of specified qaudrats and calculates the area of each quadrat.

The command line usage for this application is specified as:

```
Usage: mesh_quadrat [OPTIONS] outputfile [input] ...

OPTIONS

--length n  The number to subdivide the rectangular mesh's length.

--width n   The number to subdivide the rectangular mesh's width.

--dim XYZ   The order of the axis (it assumes XYZ)


PARAMETERS

outputfile  Output file to which to write (a .csv)

input       Input .x3d files to read. If not specified, use stdin. (May be
            specified multiple times.)
            
```

The general gist of the algorithm are:  
1. Read in a list of X3D files that specify retangular meshes.    
2. Find the maximum bounding retangule that fits inside all the meshes.  
3. Subdivide the each mesh into the specified number of quadrats using coordinate geometry.  
4. Calculate the area face where the centroid fits into of each quadrat, using the dimensions specified.  
5. Prints the output to a .csv file.  

### Output 
The output CSV file contains information on the maximium inside bounding box of the given aligned meshes and
also the dimensions of the quadrats using the given ratio.  
  
It also contains both the 3D and the 2D areas of the faces found in each quadrat in order to calculate rugosity


