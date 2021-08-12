# management

This is a EMPLOYEE DEPARTMENT MANAGEMENT SYSTEM application source code;

Let me walk you through the implementation of the system.

================================================================================================================================



Installations and configurations:

Step 1. Download Postgresql latest version build. Link for the postgresql download: https://www.postgresql.org/download/

Step 2. During the installation you can set username and password to the database.Do remember them for the further program. 
        Go to the main page of the repository or click here: https://github.com/theakhileshjoshi/management.git
        go to the green button named "code" and download the zip file.

Step 3. Go to the file and right click on it it and extract it.

Step 4. You will get a folder named management management-master Open the project in your Java IDE(eclipse in my case).

Step 5. Navigate through the following folders management-master\management-master\src\management\HomePage.java

Step 6. Open the above mentioned file "HomePage.java" in your java IDE and run it.

===============================================================================================================================


Walk through the UI(Usr Interface):
Run the HomePage and you will encounter Four buttons

        1. Connect
        2. Employees
        3. Departments
        4. Exit

1. Connect to the Database/ Create a database by Logging in to your postgresql inside the app:
       This is the first thing you have to do every time you have to make changes to the database.
       Click on the Connect button and you will be taken to the page where you can Initialize and
       Create the database if the database is not present.
       Type in your Username and Password of the Postgresql(Note: If you haven't set a username its generally "postgres")
       Press Check/Initialize connection button.
       If the prompt says connection to the database successful. you will be redirected to the home page again.

2. Employee Button: This section is all about Adding employees, Alloting them a department, Changing their departments and Searching/ Updating the employee database.
        You will encounter a Tabular view on the left hand side which is list of employees in the department and you can sort the department using  the table index . Click the top of the table to sort the columns in Ascending or Decending order .
        
        1. Create Employee: This page asks you detials for the employee to be added.
        2. Add Employee to a Department: This Page will ask you details abut the employee and department in which he is to be added.
        3. Search/Delete: This Page will give you a search engine to search for the employee details using any of the employee details like Employee ID, First Name, Last Name or Age.
        4. Back: This button will take you to the Last Page i.e the HomePage.

3. Department: 
        
        
  
        
        
