# Mesh3D

Mesh3D is a command line tool that splits up rectangular 3D meshes into a number of specified qaudrats and calculates the area of each quadrat.

The command line usage for this application is specified as:
```

Usage: mesh3d [OPTIONS] outputfile [input] ...

OPTIONS

--dim n     Calculate the 3D (3) or 2D (2) area of the faces.

--length n  The number to subdivide the rectangular mesh's length.

--width n   The number to subdivide the rectangular mesh's width.

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
5. Print the output to a .csv file.  

