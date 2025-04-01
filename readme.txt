# Readers-Writers Problem


## Overview
This program implements the classic synchronization problem known as the "Readers-Writers Problem." The problem focuses
on coordinating access to a shared resource (such as a database or file) between multiple threads, categorized as
**readers** and **writers**. The key challenge is to avoid data inconsistencies and ensure optimal resource utilization
without causing starvation.


## Problem Description
In the Readers-Writers Problem:

1. **Readers** can simultaneously read the shared resource without interfering with one another.

2. **Writers**, however, require exclusive access to the shared resource to write, as concurrent writes or simultaneous
reads and writes may lead to data corruption.

3. A synchronization mechanism must:
   - Allow multiple readers to access the resource concurrently.
   - Ensure that a writer has exclusive access.
   - Prevent starvation of readers or writers.


## Implemented variant of the Problem

1. **Prevent starvation of readers and writers**: Readers and writers are ensured to get access to shared resource in finite time.

2. **Fairness - FIFO queue**: Readers and writers get access to shared resource based on an FIFO queue. It means that
whenever any writer request access to resource, readers are stopped from getting access to resource, until every reader
with access and then writer 'leaves'.

3. **Limited access to resource**: Program asks its user to enter resource limit. Limit is represented by amount of
readers that can read resource at the same time. It does not change the fact that writer requires exclusive
access to resource.


## Program Details

### Interaction with user
- User provides resources access limit
- User provides how many readers will be created
- User provides how many writers will be created

### Tools and Technologies
- Language: **Java**
- **Maven** and **xml** - program is made as Maven project
- **SonarQube** - project has complex SonarQube report
- **javadoc** - project has full javadoc documentation
- **jacoco** - project has full jacoco test coverage report

### Annotations
- Methods created only for testing purpose were annotated as @TestOnly
- Methods which visibility is wider only for testing purpose were annotated as @VisibleForTesting

### Installation and Execution
1. First you need to download source code of the project.
2. Then you need to perform this maven command in terminal:
   ```bash
   mvn clean package
   ```
   This will generate .jar package which will allow you to run program.
3. At the end you need to perform this command in terminal:
   ```bash
   java -jar main/target/main-1.0-SNAPSHOT-jar-with-dependencies.jar lib_capacity writers_num readers_num
   ```
   Parameters lib_capacity, writers_num and readers_num are optional. If you do not pass them
   this way, program will ask for them later.