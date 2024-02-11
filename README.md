## Summary
A Java project that accepts annotated classes, interprets the annotations as mappings between objects and a database.
The application also transforms them to Java files that incorporate the ORM functionality directly into the source code.

## Key Features
1. Custom Annotations Support: The project supports custom annotations that define mappings between Java objects and database tables.
   Users can annotate their Java classes with these custom annotations to specify how each class and its fields should be mapped to the database.

2. ORM Functionality Generation: Based on the custom annotations provided, the tool generates ORM functionality for the annotated Java class.
   This includes methods for basic database CRUD operations (Create, Read, Update, Delete), as well as mapping logic to translate Java objects to database records and vice versa.
 
3. Flexibility and Customization: The generated ORM functionality is customizable and can be tailored to suit specific project requirements.
   For example the users have the flexibility to choose their preferred database type from (MySQL, Derby DB, MariaDB, and H2).
