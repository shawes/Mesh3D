#Codebook for the outputted .csv file

##Variable names

|Column Name| Description | Notes |
|-----------|-------------|-------|
| mesh_name | The name of the processed mesh file |
| quadrat_size_m | The size of the fitted quadrats | In the base unit of the mesh, which should be metres |
| quadrat_rel_x | The relative x coordinates of the quadrat |
| quadrat_rel_y | The relative y coordinates of the quadrat |
| quadrat_rel_z_avg | The average of the relative z coordinates of the quadrat |Not implemented yet |
| quadrat_rel_z_stddev | The standard deviation of the relative z coordinates of the quadrat | Not implemented yet |
| quadrat_abs_x | The absolute x coordinates of the quadrat |
| quadrat_abs_y | The absolute x coordinates of the quadrat |
| quadrat_abs_z | The absolute x coordinates of the quadrat |
| num_faces | The number of faces in the quadrat |
| num_vertices | The number of vertices in the quadrat |
| 3d_surface_area | The area of the faces in the quadrat |
| 2d_surface_area | The area of the faces in the quadrat without the Z component |
| surface_rugosity | Surface rugosity is calculated as 3d_surface_area/2d_surface_area |
