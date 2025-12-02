# 🔢 Modulator — Modular Scientific Calculator (Java + Swing + Plugin Architecture)
<img width="424" height="603" alt="Screenshot 2025-12-01 at 4 17 40 PM" src="https://github.com/user-attachments/assets/716c1378-4749-4dc1-8bac-cdd7ea8e98a2" />
<img width="424" height="603" alt="Screenshot 2025-12-01 at 4 18 27 PM" src="https://github.com/user-attachments/assets/21da660d-098b-4b06-bba2-896872c05d9e" />




Modulator is a **fully extensible, plugin-powered scientific calculator** built in Java.
External developers can add new scientific functions **without modifying any base code**.

The core calculator supports:
- Live expression evaluation (Google Calculator style)
- Parentheses, unary operations, operator precedence
- Modular scientific functions (sin, cos, log, sqrt…)
- Auto-generated UI buttons
- Button paging system (auto-fits new plugins)
- Auto-discovery of new functions at runtime using classpath scanning

---

## ✨ Features

### ✔ Live Calculation
Expressions update in real time as the user types:

```
5 + 3 * 2
→ Result: 11
```

### ✔ Plugin Architecture
Developers can add functions like:

```
cube(2)
log(100)
sin(45)
```

Simply by adding a class that implements `CalcFunction`.

### ✔ UI Auto-Updates
Every new module automatically creates a new button.  
Button pages expand dynamically.

---

## 📦 Project Structure

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
            │       │   └── FunctionRegistry.java     (Internal plugin loader)
            │       │  
            │       ├── internal 
            │       │   ├── AnnotatedFunctionAdapter.java
            │       │   ├── CalcFunction.java
            │       │   └── Function.java
            │       │
            │       ├── functions/
            │       │   ├── SinFunction.java
            │       │   ├── CosFunction.java
            │       │   ├── TanFunction.java
            │       │   ├── SqrtFunction.java
            │       │   ├── LogFunction.java
            │       │   └── (external developers add modules here)
            │       └── LiveCalculator.java           (Base UI + parser)
            └── resources/
```

---

## 🚀 Running the Calculator

### Build
```bash
mvn clean package
```

### Run (classpath)
```bash
java -cp target/classes:<dependencies> modulo.LiveCalculator
```

### Run (fat JAR)
```bash
java -jar target/modulator-1.0-SNAPSHOT-jar-with-dependencies.jar
```

---

## 🧩 Creating a New Function Module


### Step 1 — Create a class in `modulo.functions`

```java
package com.modulo.functions;

import com.modulo.internal.CalcFunction;

public class CubeFunction implements CalcFunction {
    public CubeFunction() {
    }

    @Override
    public String getName() {
        return "cube";
    }

    @Override
    public String getInsertText() {
        return "cube(";
    }

    @Override
    public double evaluate(double x) {
        return x * x * x;
    }
}
```

### Step 2 — Build the project  
### Step 3 — Done. The new function loads automatically:

- A new cube button appears  
- Typing `cube(3)` evaluates to `27`  
- Shown in paging system if needed

---

## 🔍 How Automatic Module Loading Works

We use the Reflections library to scan the classpath:

```java
Reflections reflections = new Reflections("modulo.functions");
Set<Class<? extends CalcFunction>> functions =
    reflections.getSubTypesOf(CalcFunction.class);
```

All classes implementing `CalcFunction` and having a public no-arg constructor are:

✔ Instantiated  
✔ Added to UI  
✔ Added to parser  
✔ Shown automatically  

No edits to base files are ever required.

- Functions need to defined by '(' after their name in the Text Field e.g. cube(
- Calculations need a Math Operator in their Text e.g. '*3'

---

## ⚠ Troubleshooting

### ❗ Modules not loading?

1. Package must be exactly:
```
com.modulo.functions
```

2. Class must be compiled into:
```
target/classes/com/modulo/functions/
```

3. Must have a public no-arg constructor:
```java
public CubeFunction() {}
```

4. FunctionRegistry must point to correct package:
```java
new Reflections("com.modulo.functions");
```

### ❗ SLF4J "NOP" warning  
Safe to ignore — Reflections works without logging dependency.

---

## 🛠 Built With
- Java 17+  
- Swing (UI)  
- Maven  
- Reflections  
- ByteBuddy + Javassist  

---

## 🤝 Contributing

Module developers may:
- Add new scientific functions  
- Add symbolic math operations  
- Extend parser capabilities using plugin models  

Core maintainers may update:
- LiveCalculator.java  
- FunctionRegistry.java  
- AnnotatedFunctionAdapter.java
- CalcFunction.java
- Function.java


---

## 📄 License
This project is free to use, modify, and integrate in commercial or personal projects.

---

## Planned Stuff
- Hot-reload (no restart needed)  
- Module metadata (name, version, author)  
- Categories (Trig, Logic, Algebra)  
- Graphing calculator module  
