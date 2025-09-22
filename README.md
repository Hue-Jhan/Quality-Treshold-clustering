# Quality Treshold clustering Algorithm

Quality Treshold Clustering algorithm in java (University Project). The server fetches data from a database, calculates clusters given a radius and saves them on a file.

# 💻 Code

<img src="media/qt3.png" align="right" width=230>

The javadoc are included in the ```code/javadoc``` folder, all the writings, as well as comments, print statements and the User Manual are written in italian for obvious reasons.

To run the project, simply execute the start.bat file located in the executables folder. This script will:

- Prompt you to enter your MySQL password in order to establish the connection.

- Create a MySQL database and populate it with sample data.

- Generate a dedicated account for the algorithm.

Once the database setup is complete, two terminals will open: Server terminal (running on port 8080), and Client terminal, which is the main interface for user interaction. In the client terminal, you will be prompted with two options:

<img src="media/qt1.png" align="right" width=280>

- Load data from the database.

- Load clusters from a file.

### 1) Load Data from Database

This operation will calculate clusters using data from a database. Here is the flow:

- If you provide the table name, the server will attempt to connect to MySQL and retrieve the data;

- After successfully loading the data, the terminal will ask you to enter a radius, which will be used to compute the clusters;

- The calculated clusters (including their centroids) will then be displayed. You will also be asked whether you want to repeat the operation with a new radius;

- If you type n, the program will prompt you to provide a filename under which the clusters will be saved; 

- Afterwards, it will ask whether you want to perform another operation or exit the application.

You can use any extension you want for the filename, although "dmp" is preferred.

<img src="media/qt2.png" width=450>

### 2) Load Clusters from File

This operation will display pre-calculated clusters from a file:

- Type the file name with extension;

- The clusters (centroids) will be displayed;

- Then it will ask whether you want to perform another operation or exit the application.

If you choose to perform another operation you will be prompted back to the menu, otherwise the client terminal will close. The server will of course still remain active.

# 📊 Clustering Algorithm

<img src="media/" align="right" width=200>

The algorithm is a quality treshold clustering algorithm, it works like this:

-
-
-
-

