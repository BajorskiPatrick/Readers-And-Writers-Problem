# Readers-Writers Problem

## Overview

This program implements the classic synchronization problem known as the "Readers-Writers Problem." It focuses on coordinating access to a shared resource (such as a database or file) between multiple threads categorized as **readers** and **writers**. The core challenge is avoiding data inconsistencies while maximizing resource utilization and preventing starvation.

---

## Problem Description

In the Readers-Writers Problem:

1. **Readers** can read the shared resource concurrently without interfering with each other.
2. **Writers** require exclusive access to the resource, as concurrent writes or simultaneous reads and writes may cause data corruption.
3. A proper synchronization mechanism must:
    - Allow multiple readers to read concurrently.
    - Grant exclusive access to writers.
    - Prevent starvation of both readers and writers.

---

## Implemented Variant

1. **No Starvation**: Both readers and writers are guaranteed to access the shared resource in finite time.
2. **Fairness via FIFO Queue**: Access is granted based on a FIFO queue. Once a writer requests access, no new readers are allowed until the current readers and the waiting writer are finished.
3. **Limited Concurrent Readers**: The user defines a resource access limit—how many readers can simultaneously access the resource. Writers still require exclusive access.

---

## Program Details

### User Interaction

- The user specifies:
    - Maximum number of concurrent readers (resource access limit)
    - Number of reader threads
    - Number of writer threads

### Tools and Technologies

- **Java**
- **Maven** – for project management and build automation
- **SonarQube** – integrated static code analysis
- **Javadoc** – complete code documentation
- **JaCoCo** – code coverage reporting

### Annotations

- `@TestOnly` – Marks methods created solely for testing.
- `@VisibleForTesting` – Marks methods with expanded visibility for testing purposes.

---

## Installation and Execution

1. Clone or download the project source code.
2. Build the project using Maven:
   ```bash
   mvn clean package
   ```
   This will generate a `.jar` file ready for execution.
3. Run the program:
   ```bash
   java -jar main/target/main-1.0-SNAPSHOT-jar-with-dependencies.jar lib_capacity writers_num readers_num
   ```

    - Parameters:
        - `lib_capacity`: Maximum number of readers allowed concurrently
        - `writers_num`: Number of writer threads
        - `readers_num`: Number of reader threads

    - All parameters are optional. If not provided, the program will prompt for them at runtime.