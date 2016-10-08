# Mesh Quadrats

Mesh quadrat is a command line tool that splits up rectangular 3D meshes into a number of specified qaudrats and calculates the area of each quadrat.


## Version

The current version is 2.0

## Usage

This tool was written in Scala, which requires Java (6.0 or later) to be installed. If your system does not have Java, 
you can install it here: http://www.oracle.com/technetwork/java/javase/downloads/index.html

The command line usage for this application is specified as:

```
mesh_quadrats 1.0
Usage: java -Xmx4096m -jar mesh_quadrats [options] <file>...

  -d, --dim <value>   the dimensions of the input files WLH (width-length-height)
  -s, --size <value>  the size of a quadrat (standard is metres, but depends on the mesh units)
  --verbose           verbose is a flag
  --append            append is a flag
  -o, --out <value>   output file to which to write (it has to be a .csv file)
  <file>...           input mesh files (.x3d or .obj) 
        
```

## What does it do?
The general gist of the algorithm are:  
1. Read in a list of X3D or OBJ mesh files
2. Find the maximum bounding rectangle that fits inside all the meshes.  
3. Subdivide the each mesh into the specified number of quadrats
4. Calculate the area face where the centroid fits into of each quadrat, using the dimensions specified.  
5. Prints the output to a .csv file.  

### Output 
The output CSV file contains information on the maximum inside bounding box of the given aligned meshes and
also the dimensions of the quadrats using the given ratio.  
  
It also contains both the 3D and the 2D areas of the faces found in each quadrat in order to calculate rugosity.

