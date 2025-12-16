# 🔢 Modulator — Modular Scientific Calculator (Java + Swing + Plugin Architecture)

<img width="424" height="663" alt="Screenshot 2025-12-03 at 4 05 50 PM" src="https://github.com/user-attachments/assets/bd93505a-ecb2-46df-a927-045426e407e7" />
<img width="425" height="663" alt="Screenshot 2025-12-03 at 4 07 34 PM" src="https://github.com/user-attachments/assets/031cb1d4-f120-4c6d-8f53-bdc4f383c13e" />


Modulator is a **fully extensible, plugin-powered scientific calculator** built in Java.  
External developers can add new scientific functions **without modifying any base code**.

The core calculator supports:

- Live expression evaluation (Google Calculator style)
- Parentheses, unary operations, operator precedence
- Modular scientific functions (sin, cos, log, sqrt…)
- Auto-generated UI buttons
- Button paging system (auto-fits new plugins)
- Auto-discovery of new functions at runtime using classpath scanning
- Annotation-based function modules (no interfaces needed)

---

## ✨ Features

### ✔ Live Calculation
Real-time evaluation as the user types:

```
5 + 3 * 2
→ Result: 11
```

### ✔ Plugin Architecture
Drop in new modules → Calculator picks them up automatically.

### ✔ Annotation-Powered Functions
Developers can now add functions using **only a class + an annotation**, no interface required.

---

### Example: Creating a function using only an annotation

```java
package com.modulo.functions;

import com.modulo.internal.Function;

@Function(name = "NPrime", insert = "NPrime(")
public class NPrime {

    public double run(double x) {
        int n = (int) x;
        if (n <= 0) return 2;

        int count = 0;
        int num = 1;

        while (count < n) {
            num++;
            if (isPrime(num)) count++;
        }

        return num;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;

        for (int i = 2; i <= Math.sqrt(n); i++) {
            if (n % i == 0) return false;
        }

        return true;
    }
}
```

Typing:

```
NPrime(5)
```

→ Returns:

```
11
```

---

## 📦 Project Structure (Updated)

```
modulator/
│
├── pom.xml
│
└── src/
    └── main/
        └── java/
            ├── com/
            │   └── modulo/
            │       ├── Registry/
            │       │   └── FunctionRegistry.java
            │       │
            │       ├── internal/
            │       │   ├── AnnotatedFunctionAdapter.java
            │       │   ├── CalcFunction.java
            │       │   ├── Function.java
            │       │   └── FunctionUtils.java
            │       │
            │       ├── functions/
            │       │   ├── SinFunction.java
            │       │   ├── CosFunction.java
            │       │   ├── TanFunction.java
            │       │   ├── SqrtFunction.java
            │       │   ├── LogFunction.java
            │       │   ├── NPrime.java
            │       │   └── (your modules go here)
            │       │
            │       └── LiveCalculator.java
            │
            └── resources/
```

---

## 🧩 Creating Modules

### 1. Classic Interface-Based Module

```java
public class CubeFunction implements CalcFunction {
    public CubeFunction() {}

    @Override
    public String getName() { return "cube"; }

    @Override
    public String getInsertText() { return "cube("; }

    @Override
    public double execute(double... args) { return Math.pow(args[0], 3); }
}
```

### 2. Annotation-Based Module (NEW)

```java
@Function(name="cube", insert="cube(")
public class Cube {
    public double run(double x) {
        return x * x * x;
    }
}
```

### 3. Multi-Argument Function (NEW)

```java
@Function(name="max", insert="max(")
public class Max {
    public double run(double a, double b) {
        return Math.max(a, b);
    }
}
```

---

## 🔍 How Module Loading Works

Modulator automatically:

- Scans for classes implementing `CalcFunction`
- Scans for `@Function` annotated classes
- Wraps annotated classes using `AnnotatedFunctionAdapter`
- Adds UI button
- Adds parser support
- Ensures modules never touch `LiveCalculator.java`

Zero modification to core code.

---

## ⚠ Troubleshooting

### Annotated functions not loading?

1. Must be inside:
```
com.modulo.functions
```

2. Must contain:
```
public double run(double... args)
// OR
public double run(double x)
// OR
public double run(double x, double y)
```

3. Must have:
```
@Function(...)
```

4. Must have a no-arg constructor.

### SLF4J Warning
Safe to ignore.

---

## 🛠 Built With

- Java 17+
- Swing
- Maven
- Reflections
- ByteBuddy
- Javassist
